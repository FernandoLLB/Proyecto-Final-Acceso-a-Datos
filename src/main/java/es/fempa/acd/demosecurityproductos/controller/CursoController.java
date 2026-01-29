package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.exception.CursoConMatriculasException;
import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Curso;
import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.service.CursoService;
import es.fempa.acd.demosecurityproductos.service.ProfesorService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/secretaria/cursos")
@PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIA')")
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
    public String crearCurso(@RequestParam String nombre,
                            @RequestParam(required = false) String descripcion,
                            @RequestParam Integer duracionHoras,
                            @RequestParam(required = false) BigDecimal precio,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                            @RequestParam(required = false) String categoria,
                            @RequestParam Long profesorId,
                            @RequestParam(required = false) Integer plazasDisponibles,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        System.out.println("=== DEBUG: Crear Curso ===");
        System.out.println("Nombre: " + nombre);
        System.out.println("Profesor ID: " + profesorId);
        System.out.println("Duración: " + duracionHoras);
        System.out.println("Fecha Inicio: " + fechaInicio);
        System.out.println("Fecha Fin: " + fechaFin);
        System.out.println("Precio: " + precio);
        System.out.println("Categoría: " + categoria);
        System.out.println("Plazas: " + plazasDisponibles);

        try {
            // Obtener la academia del usuario autenticado
            Academia academia = securityUtils.getUsuarioAutenticado().getAcademia();
            System.out.println("Academia obtenida: " + (academia != null ? academia.getId() : "NULL"));

            // Obtener el profesor
            Profesor profesor = profesorService.obtenerPorId(profesorId);
            System.out.println("Profesor obtenido: " + (profesor != null ? profesor.getId() : "NULL"));

            // Crear el curso manualmente
            Curso curso = new Curso();
            curso.setNombre(nombre);
            curso.setDescripcion(descripcion);
            curso.setDuracionHoras(duracionHoras);
            curso.setPrecio(precio);
            curso.setFechaInicio(fechaInicio);
            curso.setFechaFin(fechaFin);
            curso.setCategoria(categoria);
            curso.setProfesor(profesor);
            curso.setPlazasDisponibles(plazasDisponibles);
            curso.setAcademia(academia);

            System.out.println("Curso creado en memoria, intentando guardar...");
            Curso cursoGuardado = cursoService.crear(curso);
            System.out.println("✅ Curso guardado exitosamente con ID: " + cursoGuardado.getId());

            redirectAttributes.addFlashAttribute("success", "Curso creado exitosamente");
            return "redirect:/secretaria/cursos";
        } catch (Exception e) {
            System.err.println("❌ ERROR al crear curso: " + e.getMessage());
            e.printStackTrace();

            Long academiaId = securityUtils.getAcademiaIdActual();
            List<Profesor> profesores = profesorService.listarPorAcademia(academiaId);
            model.addAttribute("error", "Error al crear el curso: " + e.getMessage());
            model.addAttribute("profesores", profesores);
            model.addAttribute("academia", securityUtils.getUsuarioAutenticado().getAcademia());
            model.addAttribute("curso", new Curso());
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

    @PostMapping("/{id}/eliminar")
    public String eliminarCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cursoService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Curso eliminado exitosamente");
        } catch (CursoConMatriculasException e) {
            // Crear mensaje con enlace directo a las matrículas
            String mensajeHtml = e.getMessage() +
                " <a href='/secretaria/matriculas/curso/" + e.getCursoId() +
                "' class='alert-link'><strong>Click aquí para ver y cancelar las matrículas</strong></a>";
            redirectAttributes.addFlashAttribute("errorHtml", mensajeHtml);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el curso: " + e.getMessage());
        }
        return "redirect:/secretaria/cursos";
    }
}
