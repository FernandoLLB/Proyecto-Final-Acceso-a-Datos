package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Propietario;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import es.fempa.acd.demosecurityproductos.service.PropietarioService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestión de propietarios y sus academias.
 * Un propietario puede tener múltiples academias en el modelo SaaS.
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Controller
@RequestMapping("/propietario")
@PreAuthorize("hasRole('PROPIETARIO')")
public class PropietarioController {

    private final AcademiaService academiaService;
    private final PropietarioService propietarioService;
    private final SecurityUtils securityUtils;

    /**
     * Constructor del controlador de propietarios.
     *
     * @param academiaService servicio de gestión de academias
     * @param propietarioService servicio de gestión de propietarios
     * @param securityUtils utilidades de seguridad
     */
    public PropietarioController(AcademiaService academiaService,
                                PropietarioService propietarioService,
                                SecurityUtils securityUtils) {
        this.academiaService = academiaService;
        this.propietarioService = propietarioService;
        this.securityUtils = securityUtils;
    }

    /**
     * Muestra el dashboard del propietario con vista consolidada de sus academias.
     *
     * @param model modelo para pasar datos a la vista
     * @param session sesión HTTP para manejar la academia seleccionada
     * @return nombre de la vista del dashboard
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        // Obtener todas las academias del propietario
        List<Academia> academias = propietarioService.obtenerAcademiasActivas(propietario.getId());
        model.addAttribute("academias", academias);
        model.addAttribute("totalAcademias", propietarioService.contarAcademias(propietario.getId()));
        model.addAttribute("propietario", propietario);

        // Si hay una academia seleccionada en sesión, mostrar sus estadísticas
        Long academiaSeleccionadaId = (Long) session.getAttribute("academiaSeleccionadaId");
        if (academiaSeleccionadaId != null) {
            try {
                Map<String, Object> stats = academiaService.obtenerEstadisticasAcademia(academiaSeleccionadaId);
                Academia academiaSeleccionada = academiaService.obtenerPorId(academiaSeleccionadaId);
                model.addAttribute("stats", stats);
                model.addAttribute("academiaSeleccionada", academiaSeleccionada);
            } catch (Exception e) {
                session.removeAttribute("academiaSeleccionadaId");
            }
        }

        model.addAttribute("usuario", usuario);
        return "propietario/dashboard";
    }

    /**
     * Selecciona una academia para trabajar con ella.
     *
     * @param academiaId ID de la academia a seleccionar
     * @param session sesión HTTP
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección al dashboard
     */
    @PostMapping("/seleccionar-academia")
    public String seleccionarAcademia(@RequestParam Long academiaId,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            // Verificar que la academia pertenece al propietario
            Academia academia = academiaService.obtenerPorId(academiaId);
            if (!academia.getPropietario().getId().equals(propietario.getId())) {
                throw new RuntimeException("No tienes acceso a esta academia");
            }

            session.setAttribute("academiaSeleccionadaId", academiaId);
            redirectAttributes.addFlashAttribute("success", "Academia seleccionada: " + academia.getNombre());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al seleccionar academia: " + e.getMessage());
        }
        return "redirect:/propietario/dashboard";
    }

    /**
     * Lista todas las academias del propietario (solo visualización).
     * El propietario NO puede crear ni editar academias, solo visualizarlas.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de lista de academias
     */
    @GetMapping("/academias/lista")
    public String listarAcademias(Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        model.addAttribute("academias", propietarioService.obtenerAcademias(propietario.getId()));
        model.addAttribute("propietario", propietario);
        return "propietario/academias-lista";
    }

    /**
     * Muestra el detalle de una academia del propietario (solo lectura).
     *
     * @param id identificador de la academia
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de detalle
     */
    @GetMapping("/academias/{id}/detalle")
    public String verDetalleAcademia(@PathVariable Long id, Model model) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            Academia academia = academiaService.obtenerPorId(id);

            // Verificar que la academia pertenece al propietario
            if (!academia.getPropietario().getId().equals(propietario.getId())) {
                throw new RuntimeException("No tienes acceso a esta academia");
            }

            model.addAttribute("academia", academia);
            model.addAttribute("propietario", propietario);
            return "propietario/academia-detalle";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/propietario/academias/lista";
        }
    }
}
