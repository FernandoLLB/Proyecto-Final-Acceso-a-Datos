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
import es.fempa.acd.demosecurityproductos.service.TokenVerificacionService;

import java.util.List;

@Controller
public class AuthController {

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	AcademiaService academiaService;

	@Autowired
	AlumnoService alumnoService;

	@Autowired
	TokenVerificacionService tokenVerificacionService;

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

            // Crear token y enviar email de verificación
            tokenVerificacionService.crearTokenVerificacion(usuario);

            // Mensaje de éxito indicando que se envió el email de verificación
            redirectAttributes.addFlashAttribute("registroExitoso", true);
            redirectAttributes.addFlashAttribute("nombreUsuario", usuario.getUsername());
            redirectAttributes.addFlashAttribute("emailEnviado", usuario.getEmail());
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

    @GetMapping("/verificar-email")
    public String verificarEmail(@org.springframework.web.bind.annotation.RequestParam("token") String token,
                                  RedirectAttributes redirectAttributes) {
        try {
            boolean verificado = tokenVerificacionService.verificarToken(token);

            if (verificado) {
                redirectAttributes.addFlashAttribute("emailVerificado", true);
                redirectAttributes.addFlashAttribute("mensaje", "¡Tu cuenta ha sido verificada exitosamente! Ya puedes iniciar sesión.");
            } else {
                redirectAttributes.addFlashAttribute("error", "El token de verificación es inválido o ha expirado.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al verificar el email: " + e.getMessage());
        }

        return "redirect:/login";
    }

    @GetMapping("/reenviar-verificacion")
    public String mostrarReenviarVerificacion() {
        return "reenviar-verificacion";
    }

    @PostMapping("/reenviar-verificacion")
    public String reenviarVerificacion(@org.springframework.web.bind.annotation.RequestParam("email") String email,
                                       RedirectAttributes redirectAttributes) {
        try {
            tokenVerificacionService.reenviarEmailVerificacion(email);
            redirectAttributes.addFlashAttribute("mensaje", "Se ha enviado un nuevo email de verificación a: " + email);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al reenviar el email: " + e.getMessage());
        }

        return "redirect:/login";
    }
}
