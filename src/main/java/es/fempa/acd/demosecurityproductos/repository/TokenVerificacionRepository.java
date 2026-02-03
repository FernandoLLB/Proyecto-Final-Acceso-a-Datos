package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.TokenVerificacion;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la gestión de entidades TokenVerificacion.
 * Proporciona operaciones CRUD y consultas para tokens de verificación de email.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
public interface TokenVerificacionRepository extends JpaRepository<TokenVerificacion, Long> {

    /**
     * Busca un token de verificación por su cadena de texto.
     *
     * @param token la cadena del token a buscar
     * @return Optional con el token si existe, vacío si no existe
     */
    Optional<TokenVerificacion> findByToken(String token);

    /**
     * Busca el token de verificación asociado a un usuario.
     *
     * @param usuario el usuario cuyo token se busca
     * @return Optional con el token si existe, vacío si no existe
     */
    Optional<TokenVerificacion> findByUsuario(Usuario usuario);

    /**
     * Elimina todos los tokens de verificación de un usuario.
     *
     * @param usuario el usuario cuyos tokens serán eliminados
     */
    void deleteByUsuario(Usuario usuario);
}
