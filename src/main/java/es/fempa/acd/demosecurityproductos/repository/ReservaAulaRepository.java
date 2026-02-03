package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.EstadoReserva;
import es.fempa.acd.demosecurityproductos.model.ReservaAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de entidades ReservaAula.
 * Proporciona operaciones CRUD y consultas personalizadas para reservas de aulas en academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
public interface ReservaAulaRepository extends JpaRepository<ReservaAula, Long> {

    /**
     * Busca todas las reservas de una academia específica.
     *
     * @param academiaId el ID de la academia
     * @return lista de reservas pertenecientes a la academia
     */
    List<ReservaAula> findByAcademiaId(Long academiaId);

    /**
     * Busca reservas de una academia filtradas por estado.
     *
     * @param academiaId el ID de la academia
     * @param estado el estado de la reserva
     * @return lista de reservas que coinciden con los criterios
     */
    List<ReservaAula> findByAcademiaIdAndEstado(Long academiaId, EstadoReserva estado);

    /**
     * Busca reservas de un aula filtradas por estado.
     *
     * @param aulaId el ID del aula
     * @param estado el estado de la reserva
     * @return lista de reservas del aula con el estado especificado
     */
    List<ReservaAula> findByAulaIdAndEstado(Long aulaId, EstadoReserva estado);

    /**
     * Busca una reserva específica por ID dentro de una academia.
     *
     * @param id el ID de la reserva
     * @param academiaId el ID de la academia
     * @return Optional con la reserva si existe y pertenece a la academia, vacío si no
     */
    Optional<ReservaAula> findByIdAndAcademiaId(Long id, Long academiaId);

    /**
     * Busca reservas de un aula en una academia dentro de un rango de fechas,
     * filtradas por estado y ordenadas por fecha de inicio.
     *
     * @param aulaId el ID del aula
     * @param academiaId el ID de la academia
     * @param estado el estado de la reserva
     * @param fechaDesde la fecha y hora de inicio del rango
     * @param fechaHasta la fecha y hora de fin del rango
     * @return lista de reservas que coinciden con los criterios
     */
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
     * Hay solapamiento si: fechaInicio &lt; nuevaFechaFin AND fechaFin &gt; nuevaFechaInicio
     *
     * @param aulaId el ID del aula
     * @param fechaInicio la fecha y hora de inicio de la nueva reserva
     * @param fechaFin la fecha y hora de fin de la nueva reserva
     * @param reservaId el ID de la reserva a excluir (null para nueva reserva)
     * @return true si existe solapamiento, false si no existe
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

    /**
     * Busca reservas de una academia dentro de un rango de fechas,
     * ordenadas por fecha de inicio.
     *
     * @param academiaId el ID de la academia
     * @param fechaDesde la fecha y hora de inicio del rango
     * @param fechaHasta la fecha y hora de fin del rango
     * @return lista de reservas en el rango especificado
     */
    @Query("SELECT r FROM ReservaAula r WHERE r.academia.id = :academiaId " +
           "AND r.fechaInicio >= :fechaDesde " +
           "AND r.fechaFin <= :fechaHasta " +
           "ORDER BY r.fechaInicio")
    List<ReservaAula> findByAcademiaIdAndFechaRange(
        @Param("academiaId") Long academiaId,
        @Param("fechaDesde") LocalDateTime fechaDesde,
        @Param("fechaHasta") LocalDateTime fechaHasta
    );

    /**
     * Cuenta las reservas de una academia según su estado.
     *
     * @param academiaId el ID de la academia
     * @param estado el estado de la reserva
     * @return número de reservas con el estado especificado
     */
    long countByAcademiaIdAndEstado(Long academiaId, EstadoReserva estado);

    /**
     * Busca todas las reservas creadas por un usuario específico.
     *
     * @param usuarioId el ID del usuario creador
     * @return lista de reservas creadas por el usuario
     */
    List<ReservaAula> findByCreadaPorId(Long usuarioId);

    /**
     * Busca reservas creadas por un usuario filtradas por estado.
     *
     * @param usuarioId el ID del usuario creador
     * @param estado el estado de la reserva
     * @return lista de reservas que coinciden con los criterios
     */
    List<ReservaAula> findByCreadaPorIdAndEstado(Long usuarioId, EstadoReserva estado);
}
