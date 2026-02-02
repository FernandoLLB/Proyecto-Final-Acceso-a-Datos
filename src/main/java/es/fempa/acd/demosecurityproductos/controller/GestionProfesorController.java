package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.service.ProfesorService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import es.fempa.acd.demosecurityproductos.repository.ProfesorRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/profesores")
@PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO', 'SECRETARIA')")
public class GestionProfesorController {

    private final ProfesorService profesorService;
    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;
    private final ProfesorRepository profesorRepository;
    private final AcademiaService academiaService;

    public GestionProfesorController(ProfesorService profesorService,
                                    UsuarioService usuarioService,
                                    SecurityUtils securityUtils,
                                    ProfesorRepository profesorRepository,
                                    AcademiaService academiaService) {
        this.profesorService = profesorService;
        this.usuarioService = usuarioService;
        this.securityUtils = securityUtils;
        this.profesorRepository = profesorRepository;
        this.academiaService = academiaService;
    }

    @GetMapping
    public String listarProfesores(@RequestParam(required = false, defaultValue = "true") Boolean soloActivos,
                                   Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        List<Profesor> profesores;

        if (academiaId == null) {
            // Si es ADMIN sin academia, mostrar todos
            if (soloActivos) {
                profesores = profesorRepository.findAll().stream()
                    .filter(p -> p.getUsuario() != null && p.getUsuario().getActivo())
                    .toList();
            } else {
                profesores = profesorRepository.findAll();
            }
        } else {
            if (soloActivos) {
                profesores = profesorRepository.findByAcademiaIdAndUsuarioActivo(academiaId, true);
            } else {
                profesores = profesorService.listarPorAcademia(academiaId);
            }
        }

        model.addAttribute("profesores", profesores);
        model.addAttribute("soloActivos", soloActivos);
        return "admin/profesores-lista";
    }

    @GetMapping("/nuevo")
    public String nuevoProfesorForm(Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();

        // Si es ADMIN, cargar todas las academias activas para que pueda elegir
        if (usuario.getRol() == Rol.ADMIN) {
            List<Academia> academias = academiaService.listarActivas();
            model.addAttribute("academias", academias);
        }

        model.addAttribute("profesor", new Profesor());
        model.addAttribute("usuario", new Usuario());
        return "admin/profesor-nuevo";
    }

    @PostMapping("/crear")
    public String crearProfesor(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String nombre,
                               @RequestParam String apellidos,
                               @RequestParam(required = false) String especialidad,
                               @RequestParam(required = false) String biografia,
                               @RequestParam(required = false) Long academiaId,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Academia academiaAsignar = null;

            // Determinar la academia a asignar
            if (usuario.getRol() == Rol.ADMIN) {
                // Si es ADMIN, debe proporcionar academiaId o puede ser null
                if (academiaId != null) {
                    academiaAsignar = academiaService.obtenerPorId(academiaId);
                }
            } else {
                // Si no es ADMIN, usar su propia academia
                if (usuario.getAcademia() == null) {
                    redirectAttributes.addFlashAttribute("error", "No se pudo identificar la academia");
                    return "redirect:/profesores";
                }
                academiaAsignar = usuario.getAcademia();
            }

            // Crear usuario con rol PROFESOR
            Usuario nuevoUsuario = usuarioService.crearUsuario(username, password, email, Rol.PROFESOR);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidos(apellidos);
            nuevoUsuario.setAcademia(academiaAsignar);
            nuevoUsuario.setEmailVerificado(true); // Verificar email automáticamente al crear desde admin
            usuarioService.actualizar(nuevoUsuario);

            // Crear profesor
            Profesor profesor = new Profesor();
            profesor.setUsuario(nuevoUsuario);
            profesor.setAcademia(academiaAsignar);
            profesor.setEspecialidad(especialidad);
            profesor.setBiografia(biografia);
            profesor.setFechaContratacion(LocalDate.now());

            profesorRepository.save(profesor);

            redirectAttributes.addFlashAttribute("success", "Profesor creado exitosamente");
            return "redirect:/profesores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());

            // Recargar academias si es ADMIN
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            if (usuario.getRol() == Rol.ADMIN) {
                List<Academia> academias = academiaService.listarActivas();
                model.addAttribute("academias", academias);
            }

            model.addAttribute("profesor", new Profesor());
            model.addAttribute("usuario", new Usuario());
            return "admin/profesor-nuevo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarProfesorForm(@PathVariable Long id, Model model) {
        try {
            Profesor profesor = profesorService.obtenerPorId(id);
            model.addAttribute("profesor", profesor);
            return "admin/profesor-editar";
        } catch (IllegalArgumentException e) {
            return "redirect:/profesores";
        }
    }

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
            Profesor profesor = profesorService.obtenerPorId(id);

            // Actualizar usuario asociado
            Usuario usuario = profesor.getUsuario();
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            usuarioService.actualizar(usuario);

            // Actualizar profesor
            profesor.setEspecialidad(especialidad);
            profesor.setBiografia(biografia);
            profesorRepository.save(profesor);

            redirectAttributes.addFlashAttribute("success", "Profesor actualizado exitosamente");
            return "redirect:/profesores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            Profesor profesor = profesorService.obtenerPorId(id);
            model.addAttribute("profesor", profesor);
            return "admin/profesor-editar";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarProfesor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            profesorService.eliminarProfesor(id);
            redirectAttributes.addFlashAttribute("success", "Profesor desactivado exitosamente");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar el profesor: " + e.getMessage());
        }
        return "redirect:/profesores";
    }

    @PostMapping("/{id}/reactivar")
    public String reactivarProfesor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Profesor profesor = profesorService.obtenerPorId(id);
            Usuario usuario = profesor.getUsuario();

            if (usuario != null) {
                usuario.setActivo(true);
                usuarioService.actualizar(usuario);
                redirectAttributes.addFlashAttribute("success", "Profesor reactivado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo encontrar el usuario del profesor");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al reactivar el profesor: " + e.getMessage());
        }
        return "redirect:/profesores";
    }
}
