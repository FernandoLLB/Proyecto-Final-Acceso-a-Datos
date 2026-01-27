package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.EstadoMatricula;
import es.fempa.acd.demosecurityproductos.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    List<Matricula> findByAcademiaId(Long academiaId);

    List<Matricula> findByAlumnoId(Long alumnoId);

    List<Matricula> findByAlumnoIdAndEstado(Long alumnoId, EstadoMatricula estado);

    List<Matricula> findByCursoId(Long cursoId);

    List<Matricula> findByCursoIdAndEstado(Long cursoId, EstadoMatricula estado);

    Optional<Matricula> findByIdAndAcademiaId(Long id, Long academiaId);

    @Query("SELECT COUNT(m) > 0 FROM Matricula m WHERE m.alumno.id = :alumnoId " +
           "AND m.curso.id = :cursoId " +
           "AND m.estado = 'ACTIVA'")
    boolean existeMatriculaActiva(@Param("alumnoId") Long alumnoId, @Param("cursoId") Long cursoId);

    long countByCursoIdAndEstado(Long cursoId, EstadoMatricula estado);

    long countByAlumnoIdAndEstado(Long alumnoId, EstadoMatricula estado);

    @Query("SELECT m FROM Matricula m WHERE m.academia.id = :academiaId " +
           "AND m.estado = :estado " +
           "ORDER BY m.fechaMatriculacion DESC")
    List<Matricula> findByAcademiaIdAndEstado(
        @Param("academiaId") Long academiaId,
        @Param("estado") EstadoMatricula estado
    );
}
