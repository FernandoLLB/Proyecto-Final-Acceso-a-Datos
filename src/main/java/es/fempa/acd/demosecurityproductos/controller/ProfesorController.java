package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.ProfesorService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profesor")
@PreAuthorize("hasRole('PROFESOR')")
public class ProfesorController {

    private final ProfesorService profesorService;
    private final SecurityUtils securityUtils;

    public ProfesorController(ProfesorService profesorService, SecurityUtils securityUtils) {
        this.profesorService = profesorService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();

        if (usuario != null) {
            try {
                Profesor profesor = profesorService.obtenerPorUsuarioId(usuario.getId());
                model.addAttribute("profesor", profesor);
            } catch (IllegalArgumentException e) {
                // Profesor no encontrado, se maneja en la vista
                model.addAttribute("error", "Perfil de profesor no encontrado");
            }
        }

        model.addAttribute("usuario", usuario);
        return "profesor/dashboard";
    }
}
