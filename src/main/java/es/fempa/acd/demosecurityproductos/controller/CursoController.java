package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Curso;
import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.service.CursoService;
import es.fempa.acd.demosecurityproductos.service.ProfesorService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/secretaria/cursos")
@PreAuthorize("hasRole('SECRETARIA')")
public class CursoController {

    private final CursoService cursoService;
    private final ProfesorService profesorService;
    private final SecurityUtils securityUtils;

    public CursoController(CursoService cursoService,
                          ProfesorService profesorService,
                          SecurityUtils securityUtils) {
        this.cursoService = cursoService;
        this.profesorService = profesorService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public String listarCursos(Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            return "redirect:/error";
        }

        List<Curso> cursos = cursoService.listarPorAcademia(academiaId);
        model.addAttribute("cursos", cursos);
        return "secretaria/cursos-lista";
    }

    @GetMapping("/nuevo")
    public String nuevoCursoForm(Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        System.out.println("=== DEBUG: Formulario Nuevo Curso ===");
        System.out.println("Academia ID: " + academiaId);

        List<Profesor> profesores = profesorService.listarPorAcademia(academiaId);
        System.out.println("Número de profesores encontrados: " + (profesores != null ? profesores.size() : 0));

        if (profesores != null && !profesores.isEmpty()) {
            System.out.println("Lista de profesores:");
            for (Profesor p : profesores) {
                System.out.println("  - ID: " + p.getId() +
                                 ", Nombre: " + p.getUsuario().getNombre() +
                                 " " + p.getUsuario().getApellidos() +
                                 ", Academia: " + p.getAcademia().getId());
            }
        } else {
            System.out.println("⚠️ ADVERTENCIA: No hay profesores disponibles para esta academia");
        }

        model.addAttribute("curso", new Curso());
        model.addAttribute("profesores", profesores);
        model.addAttribute("academia", securityUtils.getUsuarioAutenticado().getAcademia());
        return "secretaria/curso-nuevo";
    }

    @PostMapping("/crear")
    public String crearCurso(@Valid @ModelAttribute Curso curso,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            Long academiaId = securityUtils.getAcademiaIdActual();
            List<Profesor> profesores = profesorService.listarPorAcademia(academiaId);
            model.addAttribute("profesores", profesores);
            model.addAttribute("academia", securityUtils.getUsuarioAutenticado().getAcademia());
            return "secretaria/curso-nuevo";
        }

        try {
            curso.setAcademia(securityUtils.getUsuarioAutenticado().getAcademia());
            cursoService.crear(curso);
            redirectAttributes.addFlashAttribute("success", "Curso creado exitosamente");
            return "redirect:/secretaria/cursos";
        } catch (IllegalArgumentException e) {
            Long academiaId = securityUtils.getAcademiaIdActual();
            List<Profesor> profesores = profesorService.listarPorAcademia(academiaId);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("profesores", profesores);
            model.addAttribute("academia", securityUtils.getUsuarioAutenticado().getAcademia());
            return "secretaria/curso-nuevo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarCursoForm(@PathVariable Long id, Model model) {
        try {
            Curso curso = cursoService.obtenerPorId(id);
            Long academiaId = securityUtils.getAcademiaIdActual();
            List<Profesor> profesores = profesorService.listarPorAcademia(academiaId);

            model.addAttribute("curso", curso);
            model.addAttribute("profesores", profesores);
            return "secretaria/curso-editar";
        } catch (IllegalArgumentException e) {
            return "redirect:/secretaria/cursos";
        }
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarCurso(@PathVariable Long id,
                                 @Valid @ModelAttribute Curso curso,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            Long academiaId = securityUtils.getAcademiaIdActual();
            List<Profesor> profesores = profesorService.listarPorAcademia(academiaId);
            model.addAttribute("profesores", profesores);
            model.addAttribute("curso", curso);
            return "secretaria/curso-editar";
        }

        try {
            cursoService.actualizar(id, curso);
            redirectAttributes.addFlashAttribute("success", "Curso actualizado exitosamente");
            return "redirect:/secretaria/cursos";
        } catch (IllegalArgumentException e) {
            Long academiaId = securityUtils.getAcademiaIdActual();
            List<Profesor> profesores = profesorService.listarPorAcademia(academiaId);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("profesores", profesores);
            model.addAttribute("curso", curso);
            return "secretaria/curso-editar";
        }
    }

    @PostMapping("/{id}/activar")
    public String activarCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cursoService.activar(id);
            redirectAttributes.addFlashAttribute("success", "Curso activado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/secretaria/cursos";
    }

    @PostMapping("/{id}/desactivar")
    public String desactivarCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cursoService.desactivar(id);
            redirectAttributes.addFlashAttribute("success", "Curso desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/secretaria/cursos";
    }
}
