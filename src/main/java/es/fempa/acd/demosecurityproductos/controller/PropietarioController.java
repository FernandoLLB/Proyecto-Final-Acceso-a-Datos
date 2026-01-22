package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/propietario")
@PreAuthorize("hasRole('PROPIETARIO')")
public class PropietarioController {

    private final AcademiaService academiaService;
    private final SecurityUtils securityUtils;

    public PropietarioController(AcademiaService academiaService, SecurityUtils securityUtils) {
        this.academiaService = academiaService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        Long academiaId = securityUtils.getAcademiaIdActual();

        if (academiaId != null) {
            Map<String, Object> stats = academiaService.obtenerEstadisticasAcademia(academiaId);
            model.addAttribute("stats", stats);
        }

        model.addAttribute("usuario", usuario);
        return "propietario/dashboard";
    }
}
