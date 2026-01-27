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

@Controller
@RequestMapping("/profesores")
@PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO', 'SECRETARIA')")
public class GestionProfesorController {

    private final ProfesorService profesorService;
    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;
    private final ProfesorRepository profesorRepository;

    public GestionProfesorController(ProfesorService profesorService,
                                    UsuarioService usuarioService,
                                    SecurityUtils securityUtils,
                                    ProfesorRepository profesorRepository) {
        this.profesorService = profesorService;
        this.usuarioService = usuarioService;
        this.securityUtils = securityUtils;
        this.profesorRepository = profesorRepository;
    }

    @GetMapping
    public String listarProfesores(Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            // Si es ADMIN sin academia, mostrar todos
            model.addAttribute("profesores", profesorRepository.findAll());
        } else {
            model.addAttribute("profesores", profesorService.listarPorAcademia(academiaId));
        }
        return "admin/profesores-lista";
    }

    @GetMapping("/nuevo")
    public String nuevoProfesorForm(Model model) {
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
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();

            if (usuario.getAcademia() == null && usuario.getRol() != Rol.ADMIN) {
                redirectAttributes.addFlashAttribute("error", "No se pudo identificar la academia");
                return "redirect:/profesores";
            }

            // Crear usuario con rol PROFESOR
            Usuario nuevoUsuario = usuarioService.crearUsuario(username, password, email, Rol.PROFESOR);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidos(apellidos);
            nuevoUsuario.setAcademia(usuario.getAcademia());
            usuarioService.actualizar(nuevoUsuario);

            // Crear profesor
            Profesor profesor = new Profesor();
            profesor.setUsuario(nuevoUsuario);
            profesor.setAcademia(usuario.getAcademia());
            profesor.setEspecialidad(especialidad);
            profesor.setBiografia(biografia);
            profesor.setFechaContratacion(LocalDate.now());

            profesorRepository.save(profesor);

            redirectAttributes.addFlashAttribute("success", "Profesor creado exitosamente");
            return "redirect:/profesores";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
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
            Profesor profesor = profesorService.obtenerPorId(id);

            // Eliminar el profesor y el usuario asociado
            profesorRepository.delete(profesor);

            redirectAttributes.addFlashAttribute("success", "Profesor eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el profesor: " + e.getMessage());
        }
        return "redirect:/profesores";
    }
}
