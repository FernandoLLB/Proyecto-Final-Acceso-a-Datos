package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Curso;
import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.ReservaAula;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.CursoService;
import es.fempa.acd.demosecurityproductos.service.ProfesorService;
import es.fempa.acd.demosecurityproductos.service.ReservaAulaService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profesor")
@PreAuthorize("hasRole('PROFESOR')")
public class ProfesorController {

    private final ProfesorService profesorService;
    private final CursoService cursoService;
    private final ReservaAulaService reservaAulaService;
    private final SecurityUtils securityUtils;

    public ProfesorController(ProfesorService profesorService,
                             CursoService cursoService,
                             ReservaAulaService reservaAulaService,
                             SecurityUtils securityUtils) {
        this.profesorService = profesorService;
        this.cursoService = cursoService;
        this.reservaAulaService = reservaAulaService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();

        if (usuario != null) {
            try {
                // Obtener información del profesor
                Profesor profesor = profesorService.obtenerPorUsuarioId(usuario.getId());
                model.addAttribute("profesor", profesor);

                // Obtener cursos asignados al profesor
                List<Curso> cursosAsignados = cursoService.listarPorProfesor(profesor.getId());
                model.addAttribute("cursos", cursosAsignados);
                model.addAttribute("totalCursos", cursosAsignados.size());

                // Obtener cursos activos
                List<Curso> cursosActivos = cursoService.listarActivosPorProfesor(profesor.getId());
                model.addAttribute("cursosActivos", cursosActivos.size());

                // Obtener reservas creadas por el profesor
                List<ReservaAula> reservasCreadas = reservaAulaService.listarPorUsuarioCreador(usuario.getId());
                model.addAttribute("reservas", reservasCreadas);
                model.addAttribute("totalReservas", reservasCreadas.size());

                // Obtener solo reservas activas
                List<ReservaAula> reservasActivas = reservaAulaService.listarActivasPorUsuarioCreador(usuario.getId());
                model.addAttribute("reservasActivas", reservasActivas.size());

            } catch (IllegalArgumentException e) {
                // Profesor no encontrado - inicializar valores por defecto
                model.addAttribute("profesor", null);
                model.addAttribute("cursos", List.of());
                model.addAttribute("totalCursos", 0);
                model.addAttribute("cursosActivos", 0);
                model.addAttribute("reservas", List.of());
                model.addAttribute("totalReservas", 0);
                model.addAttribute("reservasActivas", 0);
                model.addAttribute("error", "Perfil de profesor no encontrado. Por favor, contacte con el administrador para que cree su perfil.");
            } catch (Exception e) {
                // Error inesperado
                model.addAttribute("profesor", null);
                model.addAttribute("cursos", List.of());
                model.addAttribute("totalCursos", 0);
                model.addAttribute("cursosActivos", 0);
                model.addAttribute("reservas", List.of());
                model.addAttribute("totalReservas", 0);
                model.addAttribute("reservasActivas", 0);
                model.addAttribute("error", "Error al cargar los datos: " + e.getMessage());
            }
        }

        model.addAttribute("usuario", usuario);
        return "profesor/dashboard";
    }
}
