package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AcademiaController {

    private final AcademiaService academiaService;

    public AcademiaController(AcademiaService academiaService) {
        this.academiaService = academiaService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = academiaService.obtenerEstadisticasGlobales();
        model.addAttribute("stats", stats);
        model.addAttribute("academias", academiaService.listarTodas());
        return "admin/dashboard";
    }

    @GetMapping("/academias/lista")
    public String listarAcademias(Model model) {
        model.addAttribute("academias", academiaService.listarTodas());
        return "admin/academias-lista";
    }

    @GetMapping("/academias/nueva")
    public String nuevaAcademiaForm(Model model) {
        model.addAttribute("academia", new Academia());
        return "admin/academia-nueva";
    }

    @PostMapping("/academias/crear")
    public String crearAcademia(@ModelAttribute Academia academia) {
        academiaService.crear(academia);
        return "redirect:/admin/academias/lista";
    }

    @GetMapping("/academias/{id}/editar")
    public String editarAcademiaForm(@PathVariable Long id, Model model) {
        Academia academia = academiaService.obtenerPorId(id);
        model.addAttribute("academia", academia);
        return "admin/academia-editar";
    }

    @PostMapping("/academias/{id}/actualizar")
    public String actualizarAcademia(@PathVariable Long id, @ModelAttribute Academia academia) {
        academiaService.actualizar(id, academia);
        return "redirect:/admin/academias/lista";
    }

    @PostMapping("/academias/{id}/activar")
    public String activarAcademia(@PathVariable Long id) {
        academiaService.activar(id);
        return "redirect:/admin/academias/lista";
    }

    @PostMapping("/academias/{id}/desactivar")
    public String desactivarAcademia(@PathVariable Long id) {
        academiaService.desactivar(id);
        return "redirect:/admin/academias/lista";
    }
}
