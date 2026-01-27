package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.EstadoReserva;
import es.fempa.acd.demosecurityproductos.model.ReservaAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservaAulaRepository extends JpaRepository<ReservaAula, Long> {

    List<ReservaAula> findByAcademiaId(Long academiaId);

    List<ReservaAula> findByAcademiaIdAndEstado(Long academiaId, EstadoReserva estado);

    List<ReservaAula> findByAulaIdAndEstado(Long aulaId, EstadoReserva estado);

    Optional<ReservaAula> findByIdAndAcademiaId(Long id, Long academiaId);

    @Query("SELECT r FROM ReservaAula r WHERE r.aula.id = :aulaId " +
           "AND r.academia.id = :academiaId " +
           "AND r.estado = :estado " +
           "AND r.fechaInicio >= :fechaDesde " +
           "AND r.fechaFin <= :fechaHasta " +
           "ORDER BY r.fechaInicio")
    List<ReservaAula> findByAulaAndAcademiaAndEstadoAndFechaRange(
        @Param("aulaId") Long aulaId,
        @Param("academiaId") Long academiaId,
        @Param("estado") EstadoReserva estado,
        @Param("fechaDesde") LocalDateTime fechaDesde,
        @Param("fechaHasta") LocalDateTime fechaHasta
    );

    /**
     * Verifica si existe solapamiento de reservas ACTIVAS para un aula en un rango de fechas.
     * Solapa si: fechaInicio < nuevaFechaFin AND fechaFin > nuevaFechaInicio
     */
    @Query("SELECT COUNT(r) > 0 FROM ReservaAula r WHERE r.aula.id = :aulaId " +
           "AND r.estado = 'ACTIVA' " +
           "AND r.fechaInicio < :fechaFin " +
           "AND r.fechaFin > :fechaInicio " +
           "AND (:reservaId IS NULL OR r.id != :reservaId)")
    boolean existsSolapamiento(
        @Param("aulaId") Long aulaId,
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin,
        @Param("reservaId") Long reservaId
    );

    @Query("SELECT r FROM ReservaAula r WHERE r.academia.id = :academiaId " +
           "AND r.fechaInicio >= :fechaDesde " +
           "AND r.fechaFin <= :fechaHasta " +
           "ORDER BY r.fechaInicio")
    List<ReservaAula> findByAcademiaIdAndFechaRange(
        @Param("academiaId") Long academiaId,
        @Param("fechaDesde") LocalDateTime fechaDesde,
        @Param("fechaHasta") LocalDateTime fechaHasta
    );

    long countByAcademiaIdAndEstado(Long academiaId, EstadoReserva estado);
}
