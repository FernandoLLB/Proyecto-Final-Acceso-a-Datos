package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de entidades Usuario.
 * Proporciona operaciones CRUD y consultas personalizadas para usuarios del sistema.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca un usuario por su nombre de usuario (username).
     *
     * @param username el nombre de usuario a buscar
     * @return Optional con el usuario si existe, vacío si no existe
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * @param email el email a buscar
     * @return Optional con el usuario si existe, vacío si no existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     *
     * @param username el nombre de usuario a verificar
     * @return true si existe, false si no existe
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el email especificado.
     *
     * @param email el email a verificar
     * @return true si existe, false si no existe
     */
    boolean existsByEmail(String email);

    /**
     * Busca todos los usuarios de una academia específica.
     *
     * @param academiaId el ID de la academia
     * @return lista de usuarios pertenecientes a la academia
     */
    List<Usuario> findByAcademiaId(Long academiaId);

    /**
     * Busca usuarios de una academia filtrados por rol.
     *
     * @param academiaId el ID de la academia
     * @param rol el rol del usuario
     * @return lista de usuarios que coinciden con los criterios
     */
    List<Usuario> findByAcademiaIdAndRol(Long academiaId, Rol rol);

    /**
     * Busca todos los usuarios con un rol específico.
     *
     * @param rol el rol a buscar
     * @return lista de usuarios con el rol especificado
     */
    List<Usuario> findByRol(Rol rol);

    /**
     * Busca usuarios filtrados por rol y estado activo.
     *
     * @param rol el rol del usuario
     * @param activo true para usuarios activos, false para inactivos
     * @return lista de usuarios que coinciden con los criterios
     */
    List<Usuario> findByRolAndActivo(Rol rol, Boolean activo);

    /**
     * Busca usuarios de una academia filtrados por rol y estado activo.
     *
     * @param academiaId el ID de la academia
     * @param rol el rol del usuario
     * @param activo true para usuarios activos, false para inactivos
     * @return lista de usuarios que coinciden con los criterios
     */
    List<Usuario> findByAcademiaIdAndRolAndActivo(Long academiaId, Rol rol, Boolean activo);

    /**
     * Cuenta los usuarios de una academia con un rol específico.
     *
     * @param academiaId el ID de la academia
     * @param rol el rol a contar
     * @return número de usuarios con el rol especificado
     */
    long countByAcademiaIdAndRol(Long academiaId, Rol rol);

    /**
     * Cuenta el total de usuarios de una academia.
     *
     * @param academiaId el ID de la academia
     * @return número total de usuarios en la academia
     */
    long countByAcademiaId(Long academiaId);
}
