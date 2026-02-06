package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Aula;
import es.fempa.acd.demosecurityproductos.model.Curso;
import es.fempa.acd.demosecurityproductos.model.ReservaAula;
import es.fempa.acd.demosecurityproductos.service.AulaService;
import es.fempa.acd.demosecurityproductos.service.CursoService;
import es.fempa.acd.demosecurityproductos.service.ReservaAulaService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/secretaria/reservas")
@PreAuthorize("hasRole('SECRETARIA')")
public class ReservaAulaController {

    private final ReservaAulaService reservaAulaService;
    private final AulaService aulaService;
    private final CursoService cursoService;
    private final SecurityUtils securityUtils;

    public ReservaAulaController(ReservaAulaService reservaAulaService,
                                AulaService aulaService,
                                CursoService cursoService,
                                SecurityUtils securityUtils) {
        this.reservaAulaService = reservaAulaService;
        this.aulaService = aulaService;
        this.cursoService = cursoService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public String listarReservas(@RequestParam(required = false) Long aulaId,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                                Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            return "redirect:/error";
        }

        List<ReservaAula> reservas;

        if (aulaId != null && fecha != null) {
            LocalDateTime fechaInicio = fecha.atStartOfDay();
            LocalDateTime fechaFin = fecha.atTime(LocalTime.MAX);
            reservas = reservaAulaService.listarPorAulaYFechas(aulaId, fechaInicio, fechaFin);
        } else if (fecha != null) {
            LocalDateTime fechaInicio = fecha.atStartOfDay();
            LocalDateTime fechaFin = fecha.atTime(LocalTime.MAX);
            reservas = reservaAulaService.listarPorAcademiaYFechas(academiaId, fechaInicio, fechaFin);
        } else {
            reservas = reservaAulaService.listarActivasPorAcademia(academiaId);
        }

        List<Aula> aulas = aulaService.listarActivasPorAcademia(academiaId);

        model.addAttribute("reservas", reservas);
        model.addAttribute("aulas", aulas);
        model.addAttribute("aulaIdFiltro", aulaId);
        model.addAttribute("fechaFiltro", fecha);

        return "secretaria/reservas-lista";
    }

    @GetMapping("/nueva")
    public String nuevaReservaForm(Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        List<Aula> aulas = aulaService.listarActivasPorAcademia(academiaId);
        List<Curso> cursos = cursoService.listarActivosPorAcademia(academiaId);

        model.addAttribute("reserva", new ReservaAula());
        model.addAttribute("aulas", aulas);
        model.addAttribute("cursos", cursos);
        return "secretaria/reserva-nueva";
    }

    @PostMapping("/crear")
    public String crearReserva(@ModelAttribute("reserva") ReservaAula reserva,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        Long academiaId = securityUtils.getAcademiaIdActual();
        List<Aula> aulas = aulaService.listarActivasPorAcademia(academiaId);
        List<Curso> cursos = cursoService.listarActivosPorAcademia(academiaId);
        model.addAttribute("aulas", aulas);  // siempre, haya errores o no
        model.addAttribute("cursos", cursos);

        if (result.hasErrors()) {
            System.out.println(">>> Hay errores de validación en ReservaAula");
            result.getAllErrors().forEach(e -> System.out.println(">>> " + e));
            return "secretaria/reserva-nueva";
        }


        try {
            reservaAulaService.crear(reserva);
            redirectAttributes.addFlashAttribute("success", "Reserva creada exitosamente");
            return "redirect:/secretaria/reservas";
        } catch (IllegalStateException | IllegalArgumentException e) {
            // En este bloque también se sigue manteniendo 'reserva' en el modelo
            model.addAttribute("error", e.getMessage());
            return "secretaria/reserva-nueva";
        }
    }


    @GetMapping("/{id}/editar")
    public String editarReservaForm(@PathVariable Long id, Model model) {
        try {
            ReservaAula reserva = reservaAulaService.obtenerPorId(id);
            Long academiaId = securityUtils.getAcademiaIdActual();
            List<Aula> aulas = aulaService.listarActivasPorAcademia(academiaId);
            List<Curso> cursos = cursoService.listarActivosPorAcademia(academiaId);

            model.addAttribute("reserva", reserva);
            model.addAttribute("aulas", aulas);
            model.addAttribute("cursos", cursos);
            return "secretaria/reserva-editar";
        } catch (IllegalArgumentException e) {
            return "redirect:/secretaria/reservas";
        }
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarReserva(@PathVariable Long id,
                                   @Valid @ModelAttribute ReservaAula reserva,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (result.hasErrors()) {
            Long academiaId = securityUtils.getAcademiaIdActual();
            List<Aula> aulas = aulaService.listarActivasPorAcademia(academiaId);
            List<Curso> cursos = cursoService.listarActivosPorAcademia(academiaId);
            model.addAttribute("aulas", aulas);
            model.addAttribute("cursos", cursos);
            model.addAttribute("reserva", reserva);
            return "secretaria/reserva-editar";
        }

        try {
            reservaAulaService.actualizar(id, reserva);
            redirectAttributes.addFlashAttribute("success", "Reserva actualizada exitosamente");
            return "redirect:/secretaria/reservas";
        } catch (IllegalStateException | IllegalArgumentException e) {
            Long academiaId = securityUtils.getAcademiaIdActual();
            List<Aula> aulas = aulaService.listarActivasPorAcademia(academiaId);
            List<Curso> cursos = cursoService.listarActivosPorAcademia(academiaId);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("aulas", aulas);
            model.addAttribute("cursos", cursos);
            model.addAttribute("reserva", reserva);
            return "secretaria/reserva-editar";
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaAulaService.cancelar(id);
            redirectAttributes.addFlashAttribute("success", "Reserva cancelada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/secretaria/reservas";
    }
}
