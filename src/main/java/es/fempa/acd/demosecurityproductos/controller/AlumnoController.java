package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.model.Matricula;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.AlumnoService;
import es.fempa.acd.demosecurityproductos.service.MatriculaService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/alumno")
@PreAuthorize("hasRole('ALUMNO')")
public class AlumnoController {

    private static final Logger logger = LoggerFactory.getLogger(AlumnoController.class);

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
                logger.info("Cargando dashboard para alumno con usuario ID: {}", usuario.getId());

                Alumno alumno = alumnoService.obtenerPorUsuarioId(usuario.getId());
                model.addAttribute("alumno", alumno);

                logger.info("Alumno encontrado: ID={}, nombre={}", alumno.getId(), alumno.getUsuario().getNombre());

                // Obtener matrículas del alumno usando métodos específicos
                List<Matricula> todasMatriculas = new ArrayList<>();
                List<Matricula> matriculasActivas = new ArrayList<>();

                try {
                    todasMatriculas = matriculaService.obtenerMisMatriculas(alumno.getId());
                    logger.info("Matrículas totales encontradas: {}", todasMatriculas.size());

                    matriculasActivas = matriculaService.obtenerMisMatriculasActivas(alumno.getId());
                    logger.info("Matrículas activas encontradas: {}", matriculasActivas.size());

                    // Log de cada matrícula para debugging
                    for (int i = 0; i < todasMatriculas.size(); i++) {
                        Matricula m = todasMatriculas.get(i);
                        logger.info("Matrícula {}: ID={}, Curso={}, Estado={}",
                                    i+1, m.getId(),
                                    m.getCurso() != null ? m.getCurso().getNombre() : "NULL",
                                    m.getEstado() != null ? m.getEstado().name() : "NULL");
                    }

                } catch (Exception e) {
                    logger.error("Error al obtener matrículas para alumno ID {}: {}", alumno.getId(), e.getMessage(), e);
                    model.addAttribute("error", "Error al cargar las matrículas: " + e.getMessage());
                }

                model.addAttribute("matriculas", todasMatriculas);
                model.addAttribute("matriculasActivas", matriculasActivas);
                model.addAttribute("totalCursos", todasMatriculas.size());
                model.addAttribute("cursosActivos", matriculasActivas.size());

            } catch (IllegalArgumentException e) {
                // Alumno no encontrado, se maneja en la vista
                logger.error("Perfil de alumno no encontrado para usuario ID {}: {}", usuario.getId(), e.getMessage());
                model.addAttribute("error", "Perfil de alumno no encontrado");
            } catch (Exception e) {
                logger.error("Error inesperado en dashboard de alumno para usuario {}: {}", usuario.getUsername(), e.getMessage(), e);
                model.addAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            }
        } else {
            logger.warn("Usuario no autenticado intentando acceder al dashboard de alumno");
        }

        model.addAttribute("usuario", usuario);
        return "alumno/dashboard";
    }
}
