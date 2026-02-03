package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByAcademiaId(Long academiaId);

    List<Curso> findByAcademiaIdAndActivo(Long academiaId, Boolean activo);

    List<Curso> findByProfesorId(Long profesorId);

    List<Curso> findByProfesorIdAndActivo(Long profesorId, Boolean activo);

    Optional<Curso> findByIdAndAcademiaId(Long id, Long academiaId);

    long countByAcademiaId(Long academiaId);

    long countByAcademiaIdAndActivo(Long academiaId, Boolean activo);

    long countByProfesorId(Long profesorId);

    /**
     * Busca cursos de una academia dentro de un rango de fechas específico.
     * Los cursos se ordenan por fecha de inicio.
     *
     * @param academiaId el ID de la academia
     * @param fechaDesde la fecha de inicio del rango
     * @param fechaHasta la fecha de fin del rango
     * @return lista de cursos en el rango de fechas especificado
     */
    @Query("SELECT c FROM Curso c WHERE c.academia.id = :academiaId " +
           "AND c.fechaInicio >= :fechaDesde " +
           "AND c.fechaFin <= :fechaHasta " +
           "ORDER BY c.fechaInicio")
    List<Curso> findByAcademiaIdAndFechaRange(
        @Param("academiaId") Long academiaId,
        @Param("fechaDesde") LocalDate fechaDesde,
        @Param("fechaHasta") LocalDate fechaHasta
    );

    /**
     * Busca cursos activos de una academia filtrados por categoría.
     * Los cursos se ordenan por fecha de inicio.
     *
     * @param academiaId el ID de la academia
     * @param categoria la categoría del curso a buscar
     * @return lista de cursos activos de la categoría especificada
     */
    @Query("SELECT c FROM Curso c WHERE c.academia.id = :academiaId " +
           "AND c.categoria = :categoria " +
           "AND c.activo = true " +
           "ORDER BY c.fechaInicio")
    List<Curso> findByAcademiaIdAndCategoria(
        @Param("academiaId") Long academiaId,
        @Param("categoria") String categoria
    );
}
