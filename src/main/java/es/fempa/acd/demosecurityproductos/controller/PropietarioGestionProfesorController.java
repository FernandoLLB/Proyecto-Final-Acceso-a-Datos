package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Propietario;
import es.fempa.acd.demosecurityproductos.service.ProfesorService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import es.fempa.acd.demosecurityproductos.service.PropietarioService;
import es.fempa.acd.demosecurityproductos.repository.ProfesorRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de profesores por parte de los propietarios.
 * Los propietarios pueden crear y gestionar profesores únicamente para sus propias academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Controller
@RequestMapping("/propietario/profesores")
@PreAuthorize("hasRole('PROPIETARIO')")
public class PropietarioGestionProfesorController {

    private final ProfesorService profesorService;
    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;
    private final ProfesorRepository profesorRepository;
    private final AcademiaService academiaService;
    private final PropietarioService propietarioService;

    public PropietarioGestionProfesorController(ProfesorService profesorService,
                                               UsuarioService usuarioService,
                                               SecurityUtils securityUtils,
                                               ProfesorRepository profesorRepository,
                                               AcademiaService academiaService,
                                               PropietarioService propietarioService) {
        this.profesorService = profesorService;
        this.usuarioService = usuarioService;
        this.securityUtils = securityUtils;
        this.profesorRepository = profesorRepository;
        this.academiaService = academiaService;
        this.propietarioService = propietarioService;
    }

    /**
     * Lista los profesores de las academias del propietario autenticado.
     */
    @GetMapping
    public String listarProfesores(@RequestParam(required = false, defaultValue = "true") Boolean soloActivos,
                                   Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        // Obtener IDs de las academias del propietario
        List<Long> academiaIds = propietarioService.obtenerAcademiasActivas(propietario.getId())
                .stream()
                .map(Academia::getId)
                .toList();

        // Obtener profesores de esas academias
        List<Profesor> profesores;
        if (academiaIds.isEmpty()) {
            profesores = List.of();
        } else {
            profesores = profesorRepository.findAll().stream()
                    .filter(p -> p.getAcademia() != null && academiaIds.contains(p.getAcademia().getId()))
                    .filter(p -> !soloActivos || (p.getUsuario() != null && p.getUsuario().getActivo()))
                    .toList();
        }

        model.addAttribute("profesores", profesores);
        model.addAttribute("soloActivos", soloActivos);
        return "propietario/profesores-lista";
    }

    /**
     * Muestra el formulario para crear un nuevo profesor.
     */
    @GetMapping("/nuevo")
    public String nuevoProfesorForm(Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        // Cargar solo las academias activas del propietario
        List<Academia> academias = propietarioService.obtenerAcademiasActivas(propietario.getId());

        if (academias.isEmpty()) {
            model.addAttribute("error", "No tienes academias activas. Debes tener al menos una academia para crear profesores.");
        }

        model.addAttribute("academias", academias);
        model.addAttribute("profesor", new Profesor());
        model.addAttribute("usuario", new Usuario());
        return "propietario/profesor-nuevo";
    }

    /**
     * Crea un nuevo profesor asignado a una academia del propietario.
     */
    @PostMapping("/crear")
    public String crearProfesor(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String nombre,
                               @RequestParam String apellidos,
                               @RequestParam(required = false) String especialidad,
                               @RequestParam(required = false) String biografia,
                               @RequestParam Long academiaId,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            // Verificar que la academia pertenece al propietario
            Academia academia = academiaService.obtenerPorId(academiaId);
            if (!academia.getPropietario().getId().equals(propietario.getId())) {
                throw new IllegalArgumentException("No tienes permisos para asignar profesores a esta academia");
            }

            // Crear usuario con rol PROFESOR
            Usuario nuevoUsuario = usuarioService.crearUsuario(username, password, email, Rol.PROFESOR);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidos(apellidos);
            nuevoUsuario.setAcademia(academia);
            nuevoUsuario.setEmailVerificado(true); // Verificar email automáticamente
            usuarioService.actualizar(nuevoUsuario);

            // Crear profesor
            Profesor profesor = new Profesor();
            profesor.setUsuario(nuevoUsuario);
            profesor.setAcademia(academia);
            profesor.setEspecialidad(especialidad);
            profesor.setBiografia(biografia);
            profesor.setFechaContratacion(LocalDate.now());

            profesorRepository.save(profesor);

            redirectAttributes.addFlashAttribute("success", "Profesor creado exitosamente");
            return "redirect:/propietario/profesores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());

            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
            List<Academia> academias = propietarioService.obtenerAcademiasActivas(propietario.getId());
            model.addAttribute("academias", academias);
            model.addAttribute("profesor", new Profesor());
            model.addAttribute("usuario", new Usuario());
            return "propietario/profesor-nuevo";
        }
    }

    /**
     * Muestra el formulario para editar un profesor.
     */
    @GetMapping("/{id}/editar")
    public String editarProfesorForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            Profesor profesor = profesorService.obtenerPorId(id);

            // Verificar que el profesor pertenece a una academia del propietario
            if (profesor.getAcademia() == null ||
                !profesor.getAcademia().getPropietario().getId().equals(propietario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar este profesor");
                return "redirect:/propietario/profesores";
            }

            List<Academia> academias = propietarioService.obtenerAcademiasActivas(propietario.getId());
            model.addAttribute("profesor", profesor);
            model.addAttribute("academias", academias);
            return "propietario/profesor-editar";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Profesor no encontrado");
            return "redirect:/propietario/profesores";
        }
    }

    /**
     * Actualiza los datos de un profesor.
     */
    @PostMapping("/{id}/actualizar")
    public String actualizarProfesor(@PathVariable Long id,
                                    @RequestParam String nombre,
                                    @RequestParam String apellidos,
                                    @RequestParam String email,
                                    @RequestParam(required = false) String especialidad,
                                    @RequestParam(required = false) String biografia,
                                    @RequestParam Long academiaId,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            Profesor profesor = profesorService.obtenerPorId(id);

            // Verificar que el profesor pertenece a una academia del propietario
            if (profesor.getAcademia() == null ||
                !profesor.getAcademia().getPropietario().getId().equals(propietario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar este profesor");
                return "redirect:/propietario/profesores";
            }

            // Verificar que la nueva academia también pertenece al propietario
            Academia academia = academiaService.obtenerPorId(academiaId);
            if (!academia.getPropietario().getId().equals(propietario.getId())) {
                throw new IllegalArgumentException("No tienes permisos para asignar profesores a esta academia");
            }

            // Actualizar usuario asociado
            Usuario usuarioProfesor = profesor.getUsuario();
            usuarioProfesor.setNombre(nombre);
            usuarioProfesor.setApellidos(apellidos);
            usuarioProfesor.setEmail(email);
            usuarioProfesor.setAcademia(academia);
            usuarioService.actualizar(usuarioProfesor);

            // Actualizar profesor
            profesor.setEspecialidad(especialidad);
            profesor.setBiografia(biografia);
            profesor.setAcademia(academia);
            profesorRepository.save(profesor);

            redirectAttributes.addFlashAttribute("success", "Profesor actualizado exitosamente");
            return "redirect:/propietario/profesores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
            Profesor profesor = profesorService.obtenerPorId(id);
            List<Academia> academias = propietarioService.obtenerAcademiasActivas(propietario.getId());
            model.addAttribute("profesor", profesor);
            model.addAttribute("academias", academias);
            return "propietario/profesor-editar";
        }
    }

    /**
     * Desactiva un profesor.
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarProfesor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            Profesor profesor = profesorService.obtenerPorId(id);

            // Verificar que el profesor pertenece a una academia del propietario
            if (profesor.getAcademia() == null ||
                !profesor.getAcademia().getPropietario().getId().equals(propietario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar este profesor");
                return "redirect:/propietario/profesores";
            }

            profesorService.eliminarProfesor(id);
            redirectAttributes.addFlashAttribute("success", "Profesor desactivado exitosamente");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar el profesor: " + e.getMessage());
        }
        return "redirect:/propietario/profesores";
    }

    /**
     * Reactiva un profesor.
     */
    @PostMapping("/{id}/reactivar")
    public String reactivarProfesor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            Profesor profesor = profesorService.obtenerPorId(id);

            // Verificar que el profesor pertenece a una academia del propietario
            if (profesor.getAcademia() == null ||
                !profesor.getAcademia().getPropietario().getId().equals(propietario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para reactivar este profesor");
                return "redirect:/propietario/profesores";
            }

            Usuario usuarioProfesor = profesor.getUsuario();
            if (usuarioProfesor != null) {
                usuarioProfesor.setActivo(true);
                usuarioService.actualizar(usuarioProfesor);
                redirectAttributes.addFlashAttribute("success", "Profesor reactivado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo encontrar el usuario del profesor");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al reactivar el profesor: " + e.getMessage());
        }
        return "redirect:/propietario/profesores";
    }
}
