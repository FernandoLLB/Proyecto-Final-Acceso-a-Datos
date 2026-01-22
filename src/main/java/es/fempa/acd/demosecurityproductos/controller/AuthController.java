package es.fempa.acd.demosecurityproductos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;

@Controller
public class AuthController {

	@Autowired
	UsuarioService usuarioService;
	
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/")
    public String home() {
        return "login";
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
