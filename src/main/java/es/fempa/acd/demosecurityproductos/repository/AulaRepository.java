package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AulaRepository extends JpaRepository<Aula, Long> {

    List<Aula> findByAcademiaId(Long academiaId);

    List<Aula> findByAcademiaIdAndActiva(Long academiaId, Boolean activa);

    Optional<Aula> findByIdAndAcademiaId(Long id, Long academiaId);

    long countByAcademiaId(Long academiaId);

    long countByAcademiaIdAndActiva(Long academiaId, Boolean activa);

    @Query("SELECT COUNT(a) > 0 FROM Aula a WHERE a.nombre = :nombre AND a.academia.id = :academiaId AND (:aulaId IS NULL OR a.id != :aulaId)")
    boolean existsByNombreAndAcademiaIdExcludingId(@Param("nombre") String nombre,
                                                    @Param("academiaId") Long academiaId,
                                                    @Param("aulaId") Long aulaId);
}
