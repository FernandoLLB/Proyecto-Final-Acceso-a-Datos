package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import es.fempa.acd.demosecurityproductos.service.AlumnoService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/secretaria")
@PreAuthorize("hasRole('SECRETARIA')")
public class SecretariaController {

    private final AcademiaService academiaService;
    private final AlumnoService alumnoService;
    private final SecurityUtils securityUtils;

    public SecretariaController(AcademiaService academiaService,
                               AlumnoService alumnoService,
                               SecurityUtils securityUtils) {
        this.academiaService = academiaService;
        this.alumnoService = alumnoService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        Long academiaId = securityUtils.getAcademiaIdActual();

        if (academiaId != null) {
            Map<String, Object> stats = academiaService.obtenerEstadisticasAcademia(academiaId);
            List<Alumno> ultimosAlumnos = alumnoService.obtenerUltimosRegistrados(academiaId, 5);

            model.addAttribute("stats", stats);
            model.addAttribute("ultimosAlumnos", ultimosAlumnos);
        }

        model.addAttribute("usuario", usuario);
        return "secretaria/dashboard";
    }
}
