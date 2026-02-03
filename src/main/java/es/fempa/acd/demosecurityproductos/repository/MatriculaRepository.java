package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.EstadoMatricula;
import es.fempa.acd.demosecurityproductos.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de entidades Matricula.
 * Proporciona operaciones CRUD y consultas personalizadas para matrículas de alumnos en cursos.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    /**
     * Busca todas las matrículas de una academia específica.
     *
     * @param academiaId el ID de la academia
     * @return lista de matrículas pertenecientes a la academia
     */
    List<Matricula> findByAcademiaId(Long academiaId);

    /**
     * Busca todas las matrículas de un alumno específico.
     *
     * @param alumnoId el ID del alumno
     * @return lista de matrículas del alumno
     */
    List<Matricula> findByAlumnoId(Long alumnoId);

    /**
     * Busca matrículas de un alumno filtradas por estado.
     *
     * @param alumnoId el ID del alumno
     * @param estado el estado de la matrícula
     * @return lista de matrículas que coinciden con los criterios
     */
    List<Matricula> findByAlumnoIdAndEstado(Long alumnoId, EstadoMatricula estado);

    /**
     * Busca todas las matrículas de un curso específico.
     *
     * @param cursoId el ID del curso
     * @return lista de matrículas del curso
     */
    List<Matricula> findByCursoId(Long cursoId);

    /**
     * Busca matrículas de un curso filtradas por estado.
     *
     * @param cursoId el ID del curso
     * @param estado el estado de la matrícula
     * @return lista de matrículas que coinciden con los criterios
     */
    List<Matricula> findByCursoIdAndEstado(Long cursoId, EstadoMatricula estado);

    /**
     * Busca una matrícula específica por ID dentro de una academia.
     *
     * @param id el ID de la matrícula
     * @param academiaId el ID de la academia
     * @return Optional con la matrícula si existe y pertenece a la academia, vacío si no
     */
    Optional<Matricula> findByIdAndAcademiaId(Long id, Long academiaId);

    /**
     * Verifica si existe una matrícula activa para un alumno en un curso específico.
     *
     * @param alumnoId el ID del alumno
     * @param cursoId el ID del curso
     * @return true si existe una matrícula activa, false si no existe
     */
    @Query("SELECT COUNT(m) > 0 FROM Matricula m WHERE m.alumno.id = :alumnoId " +
           "AND m.curso.id = :cursoId " +
           "AND m.estado = 'ACTIVA'")
    boolean existeMatriculaActiva(@Param("alumnoId") Long alumnoId, @Param("cursoId") Long cursoId);

    /**
     * Cuenta las matrículas de un curso según su estado.
     *
     * @param cursoId el ID del curso
     * @param estado el estado de la matrícula
     * @return número de matrículas con el estado especificado
     */
    long countByCursoIdAndEstado(Long cursoId, EstadoMatricula estado);

    /**
     * Cuenta las matrículas de un alumno según su estado.
     *
     * @param alumnoId el ID del alumno
     * @param estado el estado de la matrícula
     * @return número de matrículas con el estado especificado
     */
    long countByAlumnoIdAndEstado(Long alumnoId, EstadoMatricula estado);

    /**
     * Busca matrículas de una academia filtradas por estado, ordenadas por fecha.
     *
     * @param academiaId el ID de la academia
     * @param estado el estado de la matrícula
     * @return lista de matrículas ordenadas por fecha de matriculación descendente
     */
    @Query("SELECT m FROM Matricula m WHERE m.academia.id = :academiaId " +
           "AND m.estado = :estado " +
           "ORDER BY m.fechaMatriculacion DESC")
    List<Matricula> findByAcademiaIdAndEstado(
        @Param("academiaId") Long academiaId,
        @Param("estado") EstadoMatricula estado
    );
}
