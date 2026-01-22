package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UsuarioService usuarioService;

    public SecurityUtils(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene el usuario autenticado actualmente
     */
    public Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return usuarioService.buscarPorUsername(username).orElse(null);
        }

        return null;
    }

    /**
     * Obtiene el ID de la academia del usuario autenticado
     */
    public Long getAcademiaIdActual() {
        Usuario usuario = getUsuarioAutenticado();
        if (usuario == null || usuario.getAcademia() == null) {
            return null;
        }
        return usuario.getAcademia().getId();
    }

    /**
     * Verifica si el usuario autenticado tiene el rol especificado
     */
    public boolean tieneRol(String rol) {
        Usuario usuario = getUsuarioAutenticado();
        return usuario != null && usuario.getRol().name().equals(rol);
    }

    /**
     * Verifica si un recurso pertenece a la academia del usuario autenticado
     */
    public boolean perteneceAMiAcademia(Long academiaId) {
        Long miAcademiaId = getAcademiaIdActual();
        return miAcademiaId != null && miAcademiaId.equals(academiaId);
    }
}
