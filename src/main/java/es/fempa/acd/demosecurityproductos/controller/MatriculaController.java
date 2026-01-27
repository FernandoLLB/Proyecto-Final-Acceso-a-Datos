package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.model.Curso;
import es.fempa.acd.demosecurityproductos.model.Matricula;
import es.fempa.acd.demosecurityproductos.service.AlumnoService;
import es.fempa.acd.demosecurityproductos.service.CursoService;
import es.fempa.acd.demosecurityproductos.service.MatriculaService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/secretaria/matriculas")
@PreAuthorize("hasRole('SECRETARIA')")
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final CursoService cursoService;
    private final AlumnoService alumnoService;
    private final SecurityUtils securityUtils;

    public MatriculaController(MatriculaService matriculaService,
                              CursoService cursoService,
                              AlumnoService alumnoService,
                              SecurityUtils securityUtils) {
        this.matriculaService = matriculaService;
        this.cursoService = cursoService;
        this.alumnoService = alumnoService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/curso/{cursoId}")
    public String listarMatriculasPorCurso(@PathVariable Long cursoId, Model model) {
        try {
            Curso curso = cursoService.obtenerPorId(cursoId);
            List<Matricula> matriculas = matriculaService.listarPorCurso(cursoId);
            long matriculasActivas = matriculaService.contarMatriculasActivasCurso(cursoId);

            model.addAttribute("curso", curso);
            model.addAttribute("matriculas", matriculas);
            model.addAttribute("matriculasActivas", matriculasActivas);
            return "secretaria/matriculas-curso";
        } catch (IllegalArgumentException e) {
            return "redirect:/secretaria/cursos";
        }
    }

    @GetMapping("/nuevo/curso/{cursoId}")
    public String nuevaMatriculaForm(@PathVariable Long cursoId, Model model) {
        try {
            Curso curso = cursoService.obtenerPorId(cursoId);
            Long academiaId = securityUtils.getAcademiaIdActual();
            List<Alumno> alumnos = alumnoService.listarPorAcademiaYEstado(academiaId, "ACTIVO");

            model.addAttribute("curso", curso);
            model.addAttribute("alumnos", alumnos);
            return "secretaria/matricula-nueva";
        } catch (IllegalArgumentException e) {
            return "redirect:/secretaria/cursos";
        }
    }

    @PostMapping("/crear")
    public String crearMatricula(@RequestParam Long cursoId,
                                @RequestParam Long alumnoId,
                                @RequestParam(required = false) String observaciones,
                                RedirectAttributes redirectAttributes) {
        try {
            matriculaService.matricular(alumnoId, cursoId, observaciones);
            redirectAttributes.addFlashAttribute("success", "Alumno matriculado exitosamente");
            return "redirect:/secretaria/matriculas/curso/" + cursoId;
        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/secretaria/matriculas/nuevo/curso/" + cursoId;
        }
    }

    @PostMapping("/{id}/completar")
    public String completarMatricula(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Matricula matricula = matriculaService.obtenerPorId(id);
            Long cursoId = matricula.getCurso().getId();

            matriculaService.completar(id);
            redirectAttributes.addFlashAttribute("success", "Matrícula marcada como completada");
            return "redirect:/secretaria/matriculas/curso/" + cursoId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/secretaria/cursos";
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarMatricula(@PathVariable Long id,
                                   @RequestParam(required = false) String motivo,
                                   RedirectAttributes redirectAttributes) {
        try {
            Matricula matricula = matriculaService.obtenerPorId(id);
            Long cursoId = matricula.getCurso().getId();

            matriculaService.cancelar(id, motivo);
            redirectAttributes.addFlashAttribute("success", "Matrícula cancelada");
            return "redirect:/secretaria/matriculas/curso/" + cursoId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/secretaria/cursos";
        }
    }
}
