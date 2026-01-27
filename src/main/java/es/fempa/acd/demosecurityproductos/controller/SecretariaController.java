package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import es.fempa.acd.demosecurityproductos.service.AlumnoService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/secretaria")
@PreAuthorize("hasRole('SECRETARIA')")
public class SecretariaController {

    private final AcademiaService academiaService;
    private final AlumnoService alumnoService;
    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;

    public SecretariaController(AcademiaService academiaService,
                               AlumnoService alumnoService,
                               UsuarioService usuarioService,
                               SecurityUtils securityUtils) {
        this.academiaService = academiaService;
        this.alumnoService = alumnoService;
        this.usuarioService = usuarioService;
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

    // CRUD de Alumnos
    @GetMapping("/alumnos")
    public String listarAlumnos(@RequestParam(required = false) String estado, Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            return "redirect:/error";
        }

        List<Alumno> alumnos;
        if (estado != null && !estado.isEmpty()) {
            alumnos = alumnoService.listarPorAcademiaYEstado(academiaId, estado);
        } else {
            alumnos = alumnoService.listarPorAcademia(academiaId);
        }

        model.addAttribute("alumnos", alumnos);
        model.addAttribute("estadoFiltro", estado);
        return "secretaria/alumnos-lista";
    }

    @GetMapping("/alumnos/nuevo")
    public String nuevoAlumnoForm(Model model) {
        model.addAttribute("alumno", new Alumno());
        model.addAttribute("usuario", new Usuario());
        return "secretaria/alumno-nuevo";
    }

    @PostMapping("/alumnos/crear")
    public String crearAlumno(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String email,
                             @RequestParam String nombre,
                             @RequestParam String apellidos,
                             @RequestParam(required = false) String observaciones,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        try {
            Long academiaId = securityUtils.getAcademiaIdActual();
            Usuario usuario = securityUtils.getUsuarioAutenticado();

            if (academiaId == null || usuario == null || usuario.getAcademia() == null) {
                redirectAttributes.addFlashAttribute("error", "No se pudo identificar la academia");
                return "redirect:/secretaria/alumnos";
            }

            // Crear usuario
            Usuario nuevoUsuario = usuarioService.crearUsuario(username, password, email, Rol.ALUMNO);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidos(apellidos);
            nuevoUsuario.setAcademia(usuario.getAcademia());

            // Crear alumno
            Alumno alumno = new Alumno();
            alumno.setUsuario(nuevoUsuario);
            alumno.setAcademia(usuario.getAcademia());
            alumno.setObservaciones(observaciones);
            alumno.setEstadoMatricula("ACTIVO");

            alumnoService.crear(alumno);

            redirectAttributes.addFlashAttribute("success", "Alumno creado exitosamente");
            return "redirect:/secretaria/alumnos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "secretaria/alumno-nuevo";
        }
    }

    @GetMapping("/alumnos/{id}/editar")
    public String editarAlumnoForm(@PathVariable Long id, Model model) {
        try {
            Alumno alumno = alumnoService.obtenerPorId(id);
            model.addAttribute("alumno", alumno);
            return "secretaria/alumno-editar";
        } catch (IllegalArgumentException e) {
            return "redirect:/secretaria/alumnos";
        }
    }

    @PostMapping("/alumnos/{id}/actualizar")
    public String actualizarAlumno(@PathVariable Long id,
                                  @RequestParam String nombre,
                                  @RequestParam String apellidos,
                                  @RequestParam String email,
                                  @RequestParam String estadoMatricula,
                                  @RequestParam(required = false) String observaciones,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        try {
            Alumno alumno = alumnoService.obtenerPorId(id);

            // Actualizar usuario asociado
            Usuario usuario = alumno.getUsuario();
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);

            // Actualizar alumno
            alumno.setEstadoMatricula(estadoMatricula);
            alumno.setObservaciones(observaciones);

            alumnoService.actualizar(alumno);

            redirectAttributes.addFlashAttribute("success", "Alumno actualizado exitosamente");
            return "redirect:/secretaria/alumnos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            Alumno alumno = alumnoService.obtenerPorId(id);
            model.addAttribute("alumno", alumno);
            return "secretaria/alumno-editar";
        }
    }

    @PostMapping("/alumnos/{id}/activar")
    public String activarAlumno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            alumnoService.activar(id);
            redirectAttributes.addFlashAttribute("success", "Alumno activado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/secretaria/alumnos";
    }

    @PostMapping("/alumnos/{id}/desactivar")
    public String desactivarAlumno(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            alumnoService.desactivar(id);
            redirectAttributes.addFlashAttribute("success", "Alumno dado de baja exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/secretaria/alumnos";
    }
}
