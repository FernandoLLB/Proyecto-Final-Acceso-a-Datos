package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Aula;
import es.fempa.acd.demosecurityproductos.model.ReservaAula;
import es.fempa.acd.demosecurityproductos.service.AulaService;
import es.fempa.acd.demosecurityproductos.service.ReservaAulaService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para la gestión de aulas por parte de secretarias.
 * Maneja las operaciones CRUD de aulas para usuarios con rol SECRETARIA.
 * Solo accesible por usuarios con rol SECRETARIA.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Controller
@RequestMapping("/secretaria/aulas")
@PreAuthorize("hasRole('SECRETARIA')")
public class AulaController {

    private final AulaService aulaService;
    private final ReservaAulaService reservaAulaService;
    private final SecurityUtils securityUtils;

    /**
     * Constructor del controlador de aulas.
     *
     * @param aulaService servicio de gestión de aulas
     * @param reservaAulaService servicio de gestión de reservas
     * @param securityUtils utilidades de seguridad
     */
    public AulaController(AulaService aulaService,
                         ReservaAulaService reservaAulaService,
                         SecurityUtils securityUtils) {
        this.aulaService = aulaService;
        this.reservaAulaService = reservaAulaService;
        this.securityUtils = securityUtils;
    }

    /**
     * Lista todas las aulas de la academia del usuario autenticado.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de lista de aulas
     */
    @GetMapping
    public String listarAulas(Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            return "redirect:/error";
        }

        List<Aula> aulas = aulaService.listarPorAcademia(academiaId);
        model.addAttribute("aulas", aulas);
        return "secretaria/aulas-lista";
    }

    /**
     * Muestra el formulario para crear una nueva aula.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista del formulario
     */
    @GetMapping("/nueva")
    public String nuevaAulaForm(Model model) {
        model.addAttribute("aula", new Aula());
        model.addAttribute("academia", securityUtils.getUsuarioAutenticado().getAcademia());
        return "secretaria/aula-nueva";
    }

    /**
     * Procesa la creación de una nueva aula.
     *
     * @param aula datos del aula a crear
     * @param result resultado de la validación
     * @param redirectAttributes atributos para redirección
     * @param model modelo para pasar datos a la vista
     * @return redirección o vista del formulario si hay errores
     */
    @PostMapping("/crear")
    public String crearAula(@Valid @ModelAttribute Aula aula,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("academia", securityUtils.getUsuarioAutenticado().getAcademia());
            return "secretaria/aula-nueva";
        }

        try {
            aula.setAcademia(securityUtils.getUsuarioAutenticado().getAcademia());
            aulaService.crear(aula);
            redirectAttributes.addFlashAttribute("success", "Aula creada exitosamente");
            return "redirect:/secretaria/aulas";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("academia", securityUtils.getUsuarioAutenticado().getAcademia());
            return "secretaria/aula-nueva";
        }
    }

    /**
     * Muestra el formulario para editar un aula existente.
     *
     * @param id identificador del aula
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista del formulario de edición
     */
    @GetMapping("/{id}/editar")
    public String editarAulaForm(@PathVariable Long id, Model model) {
        try {
            Aula aula = aulaService.obtenerPorId(id);
            model.addAttribute("aula", aula);
            return "secretaria/aula-editar";
        } catch (IllegalArgumentException e) {
            return "redirect:/secretaria/aulas";
        }
    }

    /**
     * Procesa la actualización de un aula existente.
     *
     * @param id identificador del aula
     * @param aula datos actualizados del aula
     * @param result resultado de la validación
     * @param redirectAttributes atributos para redirección
     * @param model modelo para pasar datos a la vista
     * @return redirección o vista del formulario si hay errores
     */
    @PostMapping("/{id}/actualizar")
    public String actualizarAula(@PathVariable Long id,
                                @Valid @ModelAttribute Aula aula,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("aula", aula);
            return "secretaria/aula-editar";
        }

        try {
            aulaService.actualizar(id, aula);
            redirectAttributes.addFlashAttribute("success", "Aula actualizada exitosamente");
            return "redirect:/secretaria/aulas";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("aula", aula);
            return "secretaria/aula-editar";
        }
    }

    /**
     * Activa un aula previamente desactivada.
     *
     * @param id identificador del aula
     * @param redirectAttributes atributos para redirección
     * @return redirección a la lista de aulas
     */
    @PostMapping("/{id}/activar")
    public String activarAula(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            aulaService.activar(id);
            redirectAttributes.addFlashAttribute("success", "Aula activada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/secretaria/aulas";
    }

    /**
     * Desactiva un aula.
     *
     * @param id identificador del aula
     * @param redirectAttributes atributos para redirección
     * @return redirección a la lista de aulas
     */
    @PostMapping("/{id}/desactivar")
    public String desactivarAula(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            aulaService.desactivar(id);
            redirectAttributes.addFlashAttribute("success", "Aula desactivada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/secretaria/aulas";
    }
}
