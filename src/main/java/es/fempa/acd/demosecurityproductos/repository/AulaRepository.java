package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de entidades Aula.
 * Proporciona operaciones CRUD y consultas personalizadas para aulas de academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
public interface AulaRepository extends JpaRepository<Aula, Long> {

    /**
     * Busca todas las aulas de una academia específica.
     *
     * @param academiaId el ID de la academia
     * @return lista de aulas pertenecientes a la academia
     */
    List<Aula> findByAcademiaId(Long academiaId);

    /**
     * Busca aulas de una academia filtradas por estado activo.
     *
     * @param academiaId el ID de la academia
     * @param activa true para aulas activas, false para inactivas
     * @return lista de aulas que coinciden con los criterios
     */
    List<Aula> findByAcademiaIdAndActiva(Long academiaId, Boolean activa);

    /**
     * Busca un aula específica por ID dentro de una academia.
     *
     * @param id el ID del aula
     * @param academiaId el ID de la academia
     * @return Optional con el aula si existe y pertenece a la academia, vacío si no
     */
    Optional<Aula> findByIdAndAcademiaId(Long id, Long academiaId);

    /**
     * Cuenta el total de aulas de una academia.
     *
     * @param academiaId el ID de la academia
     * @return número total de aulas en la academia
     */
    long countByAcademiaId(Long academiaId);

    /**
     * Cuenta aulas de una academia según su estado activo.
     *
     * @param academiaId el ID de la academia
     * @param activa true para contar activas, false para inactivas
     * @return número de aulas con el estado especificado
     */
    long countByAcademiaIdAndActiva(Long academiaId, Boolean activa);

    /**
     * Verifica si existe un aula con el nombre dado en una academia,
     * excluyendo opcionalmente un aula específica (útil para edición).
     *
     * @param nombre el nombre del aula a verificar
     * @param academiaId el ID de la academia
     * @param aulaId el ID del aula a excluir de la búsqueda (null para no excluir)
     * @return true si existe un aula con ese nombre, false si no existe
     */
    @Query("SELECT COUNT(a) > 0 FROM Aula a WHERE a.nombre = :nombre AND a.academia.id = :academiaId AND (:aulaId IS NULL OR a.id != :aulaId)")
    boolean existsByNombreAndAcademiaIdExcludingId(@Param("nombre") String nombre,
                                                    @Param("academiaId") Long academiaId,
                                                    @Param("aulaId") Long aulaId);
}
