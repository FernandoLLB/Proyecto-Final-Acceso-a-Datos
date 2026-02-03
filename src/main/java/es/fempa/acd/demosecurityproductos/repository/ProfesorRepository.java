package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de entidades Profesor.
 * Proporciona operaciones CRUD y consultas personalizadas para profesores de academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    /**
     * Busca todos los profesores de una academia específica.
     *
     * @param academiaId el ID de la academia
     * @return lista de profesores pertenecientes a la academia
     */
    List<Profesor> findByAcademiaId(Long academiaId);

    /**
     * Busca profesores de una academia filtrados por el estado activo del usuario.
     *
     * @param academiaId el ID de la academia
     * @param activo true para profesores con usuario activo, false para inactivos
     * @return lista de profesores que coinciden con los criterios
     */
    @Query("SELECT p FROM Profesor p WHERE p.academia.id = :academiaId AND p.usuario.activo = :activo")
    List<Profesor> findByAcademiaIdAndUsuarioActivo(@Param("academiaId") Long academiaId, @Param("activo") Boolean activo);

    /**
     * Cuenta el total de profesores de una academia.
     *
     * @param academiaId el ID de la academia
     * @return número total de profesores en la academia
     */
    long countByAcademiaId(Long academiaId);

    /**
     * Busca un profesor por el ID de su usuario asociado.
     *
     * @param usuarioId el ID del usuario
     * @return Optional con el profesor si existe, vacío si no existe
     */
    Optional<Profesor> findByUsuarioId(Long usuarioId);

    /**
     * Busca un profesor por su entidad Usuario.
     *
     * @param usuario el usuario asociado al profesor
     * @return Optional con el profesor si existe, vacío si no existe
     */
    Optional<Profesor> findByUsuario(Usuario usuario);
}
