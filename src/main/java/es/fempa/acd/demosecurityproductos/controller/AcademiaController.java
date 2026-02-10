package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import es.fempa.acd.demosecurityproductos.service.PropietarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador para la gestión de academias por parte del administrador.
 * Solo accesible por usuarios con rol ADMIN.
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AcademiaController {

    private final AcademiaService academiaService;
    private final PropietarioService propietarioService;

    /**
     * Constructor del controlador de academias.
     *
     * @param academiaService servicio de gestión de academias
     * @param propietarioService servicio de gestión de propietarios
     */
    public AcademiaController(AcademiaService academiaService, PropietarioService propietarioService) {
        this.academiaService = academiaService;
        this.propietarioService = propietarioService;
    }

    /**
     * Muestra el dashboard del administrador con estadísticas globales.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista del dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = academiaService.obtenerEstadisticasGlobales();

        // Añadir estadísticas de propietarios
        stats.put("totalPropietarios", propietarioService.contarActivos());

        model.addAttribute("stats", stats);
        model.addAttribute("academias", academiaService.listarTodas());
        model.addAttribute("propietarios", propietarioService.listarActivos());
        return "admin/dashboard";
    }

    /**
     * Lista todas las academias del sistema.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de lista de academias
     */
    @GetMapping("/academias/lista")
    public String listarAcademias(Model model) {
        model.addAttribute("academias", academiaService.listarTodas());
        return "admin/academias-lista";
    }

    /**
     * Muestra el formulario para crear una nueva academia.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista del formulario
     */
    @GetMapping("/academias/nueva")
    public String nuevaAcademiaForm(Model model) {
        model.addAttribute("academia", new Academia());
        model.addAttribute("propietarios", propietarioService.listarActivos());
        return "admin/academia-nueva";
    }

    /**
     * Procesa la creación de una nueva academia.
     *
     * @param academia datos de la academia a crear
     * @param propietarioId ID del propietario al que asignar la academia
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección a la lista de academias
     */
    @PostMapping("/academias/crear")
    public String crearAcademia(@ModelAttribute Academia academia,
                                @RequestParam Long propietarioId,
                                org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            es.fempa.acd.demosecurityproductos.model.Propietario propietario = propietarioService.obtenerPorId(propietarioId);
            academia.setPropietario(propietario);
            academiaService.crear(academia);
            redirectAttributes.addFlashAttribute("success", "Academia creada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la academia: " + e.getMessage());
        }
        return "redirect:/admin/academias/lista";
    }

    /**
     * Muestra el formulario para editar una academia existente.
     *
     * @param id identificador de la academia
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista del formulario de edición
     */
    @GetMapping("/academias/{id}/editar")
    public String editarAcademiaForm(@PathVariable Long id, Model model) {
        Academia academia = academiaService.obtenerPorId(id);
        model.addAttribute("academia", academia);
        model.addAttribute("propietarios", propietarioService.listarActivos());
        return "admin/academia-editar";
    }

    /**
     * Procesa la actualización de una academia.
     *
     * @param id identificador de la academia
     * @param academia datos actualizados de la academia
     * @param propietarioId ID del propietario
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección a la lista de academias
     */
    @PostMapping("/academias/{id}/actualizar")
    public String actualizarAcademia(@PathVariable Long id,
                                     @ModelAttribute Academia academia,
                                     @RequestParam Long propietarioId,
                                     org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            es.fempa.acd.demosecurityproductos.model.Propietario propietario = propietarioService.obtenerPorId(propietarioId);
            academia.setPropietario(propietario);
            academiaService.actualizar(id, academia);
            redirectAttributes.addFlashAttribute("success", "Academia actualizada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la academia: " + e.getMessage());
        }
        return "redirect:/admin/academias/lista";
    }

    /**
     * Activa una academia desactivada.
     *
     * @param id identificador de la academia
     * @return redirección a la lista de academias
     */
    @PostMapping("/academias/{id}/activar")
    public String activarAcademia(@PathVariable Long id) {
        academiaService.activar(id);
        return "redirect:/admin/academias/lista";
    }

    /**
     * Desactiva una academia activa.
     *
     * @param id identificador de la academia
     * @return redirección a la lista de academias
     */
    @PostMapping("/academias/{id}/desactivar")
    public String desactivarAcademia(@PathVariable Long id) {
        academiaService.desactivar(id);
        return "redirect:/admin/academias/lista";
    }
}
