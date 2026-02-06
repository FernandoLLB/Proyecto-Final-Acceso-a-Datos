package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.service.ProfesorService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import es.fempa.acd.demosecurityproductos.repository.ProfesorRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la gestión de profesores por parte de las secretarias.
 * Las secretarias pueden crear y gestionar profesores de su academia.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Controller
@RequestMapping("/secretaria/profesores")
@PreAuthorize("hasRole('SECRETARIA')")
public class SecretariaGestionProfesorController {

    private final ProfesorService profesorService;
    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;
    private final ProfesorRepository profesorRepository;

    public SecretariaGestionProfesorController(ProfesorService profesorService,
                                              UsuarioService usuarioService,
                                              SecurityUtils securityUtils,
                                              ProfesorRepository profesorRepository) {
        this.profesorService = profesorService;
        this.usuarioService = usuarioService;
        this.securityUtils = securityUtils;
        this.profesorRepository = profesorRepository;
    }

    /**
     * Lista los profesores de la academia de la secretaria autenticada.
     */
    @GetMapping
    public String listarProfesores(@RequestParam(required = false, defaultValue = "true") Boolean soloActivos,
                                   Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();

        if (academiaId == null) {
            model.addAttribute("error", "No se pudo identificar la academia");
            model.addAttribute("profesores", List.of());
            return "secretaria/profesores-lista";
        }

        // Obtener profesores de la academia
        List<Profesor> profesores = profesorRepository.findAll().stream()
                .filter(p -> p.getAcademia() != null && p.getAcademia().getId().equals(academiaId))
                .filter(p -> !soloActivos || (p.getUsuario() != null && p.getUsuario().getActivo()))
                .toList();

        model.addAttribute("profesores", profesores);
        model.addAttribute("soloActivos", soloActivos);
        return "secretaria/profesores-lista";
    }

    /**
     * Muestra el formulario para crear un nuevo profesor.
     */
    @GetMapping("/nuevo")
    public String nuevoProfesorForm(Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();

        if (academiaId == null) {
            model.addAttribute("error", "No se pudo identificar la academia");
            return "redirect:/secretaria/profesores";
        }

        model.addAttribute("profesor", new Profesor());
        model.addAttribute("usuario", new Usuario());
        return "secretaria/profesor-nuevo";
    }

    /**
     * Crea un nuevo profesor asignado a la academia de la secretaria.
     */
    @PostMapping("/crear")
    public String crearProfesor(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String nombre,
                               @RequestParam String apellidos,
                               @RequestParam(required = false) String especialidad,
                               @RequestParam(required = false) String biografia,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            Long academiaId = securityUtils.getAcademiaIdActual();
            Usuario usuarioSecretaria = securityUtils.getUsuarioAutenticado();

            if (academiaId == null || usuarioSecretaria == null || usuarioSecretaria.getAcademia() == null) {
                redirectAttributes.addFlashAttribute("error", "No se pudo identificar la academia");
                return "redirect:/secretaria/profesores";
            }

            // Crear usuario con rol PROFESOR
            Usuario nuevoUsuario = usuarioService.crearUsuario(username, password, email, Rol.PROFESOR);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidos(apellidos);
            nuevoUsuario.setAcademia(usuarioSecretaria.getAcademia());
            nuevoUsuario.setEmailVerificado(true); // Verificar email automáticamente
            usuarioService.actualizar(nuevoUsuario);

            // Crear profesor
            Profesor profesor = new Profesor();
            profesor.setUsuario(nuevoUsuario);
            profesor.setAcademia(usuarioSecretaria.getAcademia());
            profesor.setEspecialidad(especialidad);
            profesor.setBiografia(biografia);
            profesor.setFechaContratacion(LocalDate.now());

            profesorRepository.save(profesor);

            redirectAttributes.addFlashAttribute("success", "Profesor creado exitosamente");
            return "redirect:/secretaria/profesores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("profesor", new Profesor());
            model.addAttribute("usuario", new Usuario());
            return "secretaria/profesor-nuevo";
        }
    }

    /**
     * Muestra el formulario para editar un profesor.
     */
    @GetMapping("/{id}/editar")
    public String editarProfesorForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Long academiaId = securityUtils.getAcademiaIdActual();
            Profesor profesor = profesorService.obtenerPorId(id);

            // Verificar que el profesor pertenece a la academia de la secretaria
            if (profesor.getAcademia() == null || !profesor.getAcademia().getId().equals(academiaId)) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar este profesor");
                return "redirect:/secretaria/profesores";
            }

            model.addAttribute("profesor", profesor);
            return "secretaria/profesor-editar";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Profesor no encontrado");
            return "redirect:/secretaria/profesores";
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
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        try {
            Long academiaId = securityUtils.getAcademiaIdActual();
            Profesor profesor = profesorService.obtenerPorId(id);

            // Verificar que el profesor pertenece a la academia de la secretaria
            if (profesor.getAcademia() == null || !profesor.getAcademia().getId().equals(academiaId)) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar este profesor");
                return "redirect:/secretaria/profesores";
            }

            // Actualizar usuario asociado
            Usuario usuarioProfesor = profesor.getUsuario();
            usuarioProfesor.setNombre(nombre);
            usuarioProfesor.setApellidos(apellidos);
            usuarioProfesor.setEmail(email);
            usuarioService.actualizar(usuarioProfesor);

            // Actualizar profesor
            profesor.setEspecialidad(especialidad);
            profesor.setBiografia(biografia);
            profesorRepository.save(profesor);

            redirectAttributes.addFlashAttribute("success", "Profesor actualizado exitosamente");
            return "redirect:/secretaria/profesores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            Profesor profesor = profesorService.obtenerPorId(id);
            model.addAttribute("profesor", profesor);
            return "secretaria/profesor-editar";
        }
    }

    /**
     * Desactiva un profesor.
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarProfesor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Long academiaId = securityUtils.getAcademiaIdActual();
            Profesor profesor = profesorService.obtenerPorId(id);

            // Verificar que el profesor pertenece a la academia de la secretaria
            if (profesor.getAcademia() == null || !profesor.getAcademia().getId().equals(academiaId)) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar este profesor");
                return "redirect:/secretaria/profesores";
            }

            profesorService.eliminarProfesor(id);
            redirectAttributes.addFlashAttribute("success", "Profesor desactivado exitosamente");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar el profesor: " + e.getMessage());
        }
        return "redirect:/secretaria/profesores";
    }

    /**
     * Reactiva un profesor.
     */
    @PostMapping("/{id}/reactivar")
    public String reactivarProfesor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Long academiaId = securityUtils.getAcademiaIdActual();
            Profesor profesor = profesorService.obtenerPorId(id);

            // Verificar que el profesor pertenece a la academia de la secretaria
            if (profesor.getAcademia() == null || !profesor.getAcademia().getId().equals(academiaId)) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para reactivar este profesor");
                return "redirect:/secretaria/profesores";
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
        return "redirect:/secretaria/profesores";
    }
}
