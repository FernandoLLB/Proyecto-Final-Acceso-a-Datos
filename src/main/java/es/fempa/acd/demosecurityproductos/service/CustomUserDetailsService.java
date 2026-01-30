package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Verificar que el email esté verificado (excepto para ADMIN y PROPIETARIO)
        if (!usuario.getEmailVerificado() &&
            usuario.getRol() != Rol.ADMIN &&
            usuario.getRol() != Rol.PROPIETARIO) {
            throw new UsernameNotFoundException("Debes verificar tu email antes de iniciar sesión. Revisa tu correo electrónico.");
        }

        // Verificar que la cuenta esté activa
        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("Tu cuenta ha sido desactivada. Contacta al administrador.");
        }

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword()) // Contraseña encriptada
                .roles(usuario.getRol().name()) // Rol asignado
                .build();
    }
}
