package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.model.Matricula;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.AlumnoService;
import es.fempa.acd.demosecurityproductos.service.MatriculaService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/alumno")
@PreAuthorize("hasRole('ALUMNO')")
public class AlumnoController {

    private final AlumnoService alumnoService;
    private final MatriculaService matriculaService;
    private final SecurityUtils securityUtils;

    public AlumnoController(AlumnoService alumnoService, MatriculaService matriculaService, SecurityUtils securityUtils) {
        this.alumnoService = alumnoService;
        this.matriculaService = matriculaService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();

        if (usuario != null) {
            try {
                Alumno alumno = alumnoService.obtenerPorUsuarioId(usuario.getId());
                model.addAttribute("alumno", alumno);

                // Obtener matrículas del alumno usando métodos específicos
                List<Matricula> todasMatriculas = matriculaService.obtenerMisMatriculas(alumno.getId());
                List<Matricula> matriculasActivas = matriculaService.obtenerMisMatriculasActivas(alumno.getId());

                model.addAttribute("matriculas", todasMatriculas);
                model.addAttribute("matriculasActivas", matriculasActivas);
                model.addAttribute("totalCursos", todasMatriculas.size());
                model.addAttribute("cursosActivos", matriculasActivas.size());

            } catch (IllegalArgumentException e) {
                // Alumno no encontrado, se maneja en la vista
                model.addAttribute("error", "Perfil de alumno no encontrado");
            }
        }

        model.addAttribute("usuario", usuario);
        return "alumno/dashboard";
    }
}
