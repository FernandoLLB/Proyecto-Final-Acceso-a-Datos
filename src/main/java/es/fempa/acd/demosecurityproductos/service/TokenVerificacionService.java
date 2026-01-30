package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.TokenVerificacion;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.TokenVerificacionRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TokenVerificacionService {

    @Autowired
    private TokenVerificacionRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Crea un token de verificación y envía el email
     */
    @Transactional
    public String crearTokenVerificacion(Usuario usuario) {
        // Eliminar token anterior si existe
        tokenRepository.findByUsuario(usuario).ifPresent(tokenRepository::delete);

        // Crear nuevo token
        String tokenString = UUID.randomUUID().toString();
        TokenVerificacion token = new TokenVerificacion(tokenString, usuario);
        tokenRepository.save(token);

        // Enviar email
        emailService.enviarEmailVerificacion(
            usuario.getEmail(),
            usuario.getNombre() + " " + usuario.getApellidos(),
            tokenString
        );

        return tokenString;
    }

    /**
     * Verifica un token y activa la cuenta del usuario
     */
    @Transactional
    public boolean verificarToken(String tokenString) {
        TokenVerificacion token = tokenRepository.findByToken(tokenString).orElse(null);

        if (token == null) {
            return false;
        }

        if (token.isExpirado()) {
            tokenRepository.delete(token);
            return false;
        }

        // Marcar usuario como verificado
        Usuario usuario = token.getUsuario();
        usuario.setEmailVerificado(true);
        usuarioRepository.save(usuario);

        // Eliminar el token usado
        tokenRepository.delete(token);

        // Enviar email de bienvenida
        emailService.enviarEmailBienvenida(
            usuario.getEmail(),
            usuario.getNombre() + " " + usuario.getApellidos()
        );

        return true;
    }

    /**
     * Reenvía el email de verificación
     */
    @Transactional
    public void reenviarEmailVerificacion(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (usuario.getEmailVerificado()) {
            throw new IllegalArgumentException("El email ya está verificado");
        }

        crearTokenVerificacion(usuario);
    }
}
