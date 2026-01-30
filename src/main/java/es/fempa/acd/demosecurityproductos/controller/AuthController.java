package es.fempa.acd.demosecurityproductos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.fempa.acd.demosecurityproductos.dto.RegistroAlumnoDTO;
import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import es.fempa.acd.demosecurityproductos.service.AlumnoService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;

import java.util.List;

@Controller
public class AuthController {

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	AcademiaService academiaService;

	@Autowired
	AlumnoService alumnoService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("registroDTO", new RegistroAlumnoDTO());
        List<Academia> academias = academiaService.listarActivasParaRegistro();
        model.addAttribute("academias", academias);
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute("registroDTO") RegistroAlumnoDTO registroDTO,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        try {
            // Validaciones
            if (!registroDTO.getPassword().equals(registroDTO.getConfirmPassword())) {
                model.addAttribute("error", "Las contraseñas no coinciden");
                model.addAttribute("academias", academiaService.listarActivasParaRegistro());
                return "registro";
            }

            if (registroDTO.getPassword().length() < 3) {
                model.addAttribute("error", "La contraseña debe tener al menos 3 caracteres");
                model.addAttribute("academias", academiaService.listarActivasParaRegistro());
                return "registro";
            }

            if (registroDTO.getAcademiaId() == null) {
                model.addAttribute("error", "Debe seleccionar una academia");
                model.addAttribute("academias", academiaService.listarActivasParaRegistro());
                return "registro";
            }

            // Obtener academia
            Academia academia = academiaService.obtenerPorIdParaRegistro(registroDTO.getAcademiaId());

            // Crear usuario
            Usuario usuario = usuarioService.crearUsuarioAlumno(
                registroDTO.getUsername(),
                registroDTO.getPassword(),
                registroDTO.getEmail(),
                registroDTO.getNombre(),
                registroDTO.getApellidos(),
                academia
            );

            // Crear alumno
            Alumno alumno = new Alumno();
            alumno.setUsuario(usuario);
            alumno.setAcademia(academia);
            alumnoService.crear(alumno);

            // Mensaje de éxito con información del usuario
            redirectAttributes.addFlashAttribute("registroExitoso", true);
            redirectAttributes.addFlashAttribute("nombreUsuario", usuario.getUsername());
            redirectAttributes.addFlashAttribute("nombreCompleto", usuario.getNombre() + " " + usuario.getApellidos());
            return "redirect:/login";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("academias", academiaService.listarActivasParaRegistro());
            return "registro";
        }
    }

    /**
     * Redirige a la página de inicio según el rol del usuario
     */
    @RequestMapping("/default")
    public String defaultAfterLogin(Authentication authentication, Model model) {
        String userName = authentication.getName();
        Usuario usuario = usuarioService.buscarPorUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        model.addAttribute("usuario", usuario);

        // Redirección según rol
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PROPIETARIO"))) {
            return "redirect:/propietario/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SECRETARIA"))) {
            return "redirect:/secretaria/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PROFESOR"))) {
            return "redirect:/profesor/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ALUMNO"))) {
            return "redirect:/alumno/dashboard";
        }

        return "redirect:/login";
    }
}
