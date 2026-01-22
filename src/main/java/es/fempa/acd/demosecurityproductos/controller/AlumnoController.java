package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.AlumnoService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alumno")
@PreAuthorize("hasRole('ALUMNO')")
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final SecurityUtils securityUtils;

    public AlumnoController(AlumnoService alumnoService, SecurityUtils securityUtils) {
        this.alumnoService = alumnoService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();

        if (usuario != null) {
            try {
                Alumno alumno = alumnoService.obtenerPorUsuarioId(usuario.getId());
                model.addAttribute("alumno", alumno);
            } catch (IllegalArgumentException e) {
                // Alumno no encontrado, se maneja en la vista
                model.addAttribute("error", "Perfil de alumno no encontrado");
            }
        }

        model.addAttribute("usuario", usuario);
        return "alumno/dashboard";
    }
}
