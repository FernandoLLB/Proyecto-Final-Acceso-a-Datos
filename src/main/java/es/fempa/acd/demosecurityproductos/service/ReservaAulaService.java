package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.*;
import es.fempa.acd.demosecurityproductos.repository.ReservaAulaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaAulaService {

    private final ReservaAulaRepository reservaAulaRepository;
    private final AulaService aulaService;
    private final SecurityUtils securityUtils;

    public ReservaAulaService(ReservaAulaRepository reservaAulaRepository,
                             AulaService aulaService,
                             SecurityUtils securityUtils) {
        this.reservaAulaRepository = reservaAulaRepository;
        this.aulaService = aulaService;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public ReservaAula crear(ReservaAula reserva) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        Usuario usuario = securityUtils.getUsuarioAutenticado();

        if (academiaId == null || usuario == null) {
            throw new IllegalStateException("Usuario sin academia asignada");
        }

        // Validar que el aula pertenece a la academia
        Aula aula = aulaService.obtenerPorId(reserva.getAula().getId());
        if (!aula.getActiva()) {
            throw new IllegalArgumentException("El aula no está activa");
        }

        // Validar fechas
        validarFechas(reserva.getFechaInicio(), reserva.getFechaFin());

        // Validar anti-solapamiento
        if (reservaAulaRepository.existsSolapamiento(
                aula.getId(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                null)) {
            throw new IllegalStateException("Ya existe una reserva activa que solapa con el horario solicitado");
        }

        // Configurar la reserva
        reserva.setAcademia(usuario.getAcademia());
        reserva.setAula(aula);
        reserva.setCreadaPor(usuario);
        reserva.setEstado(EstadoReserva.ACTIVA);
        reserva.setFechaCreacion(LocalDateTime.now());

        return reservaAulaRepository.save(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservaAula> listarPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return reservaAulaRepository.findByAcademiaId(academiaId);
    }

    @Transactional(readOnly = true)
    public List<ReservaAula> listarActivasPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return reservaAulaRepository.findByAcademiaIdAndEstado(academiaId, EstadoReserva.ACTIVA);
    }

    @Transactional(readOnly = true)
    public List<ReservaAula> listarPorAulaYFechas(Long aulaId, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            throw new IllegalStateException("Usuario sin academia asignada");
        }

        // Validar que el aula pertenece a la academia
        aulaService.obtenerPorId(aulaId);

        return reservaAulaRepository.findByAulaAndAcademiaAndEstadoAndFechaRange(
            aulaId, academiaId, EstadoReserva.ACTIVA, fechaDesde, fechaHasta);
    }

    @Transactional(readOnly = true)
    public List<ReservaAula> listarPorAcademiaYFechas(Long academiaId, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        validarAccesoAcademia(academiaId);
        return reservaAulaRepository.findByAcademiaIdAndFechaRange(academiaId, fechaDesde, fechaHasta);
    }

    @Transactional(readOnly = true)
    public ReservaAula obtenerPorId(Long id) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            throw new IllegalStateException("Usuario sin academia asignada");
        }

        return reservaAulaRepository.findByIdAndAcademiaId(id, academiaId)
            .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada o no pertenece a su academia"));
    }

    @Transactional
    public void cancelar(Long id) {
        ReservaAula reserva = obtenerPorId(id);
        Usuario usuario = securityUtils.getUsuarioAutenticado();

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("La reserva ya está cancelada");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva.setCanceladaPor(usuario);
        reserva.setFechaCancelacion(LocalDateTime.now());

        reservaAulaRepository.save(reserva);
    }

    @Transactional
    public ReservaAula actualizar(Long id, ReservaAula reservaActualizada) {
        ReservaAula reservaExistente = obtenerPorId(id);

        if (reservaExistente.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("No se puede modificar una reserva cancelada");
        }

        // Validar fechas
        validarFechas(reservaActualizada.getFechaInicio(), reservaActualizada.getFechaFin());

        // Validar que el aula pertenece a la academia
        Aula aula = aulaService.obtenerPorId(reservaActualizada.getAula().getId());
        if (!aula.getActiva()) {
            throw new IllegalArgumentException("El aula no está activa");
        }

        // Validar anti-solapamiento (excluyendo la reserva actual)
        if (reservaAulaRepository.existsSolapamiento(
                aula.getId(),
                reservaActualizada.getFechaInicio(),
                reservaActualizada.getFechaFin(),
                id)) {
            throw new IllegalStateException("Ya existe una reserva activa que solapa con el horario solicitado");
        }

        reservaExistente.setAula(aula);
        reservaExistente.setFechaInicio(reservaActualizada.getFechaInicio());
        reservaExistente.setFechaFin(reservaActualizada.getFechaFin());
        reservaExistente.setDescripcion(reservaActualizada.getDescripcion());

        return reservaAulaRepository.save(reservaExistente);
    }

    @Transactional(readOnly = true)
    public long contarActivasPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return reservaAulaRepository.countByAcademiaIdAndEstado(academiaId, EstadoReserva.ACTIVA);
    }

    @Transactional(readOnly = true)
    public List<ReservaAula> listarPorUsuarioCreador(Long usuarioId) {
        return reservaAulaRepository.findByCreadaPorId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<ReservaAula> listarActivasPorUsuarioCreador(Long usuarioId) {
        return reservaAulaRepository.findByCreadaPorIdAndEstado(usuarioId, EstadoReserva.ACTIVA);
    }

    private void validarFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }

        if (fechaFin.isBefore(fechaInicio) || fechaFin.isEqual(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        if (fechaInicio.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se pueden crear reservas en el pasado");
        }
    }

    private void validarAccesoAcademia(Long academiaId) {
        if (!securityUtils.tieneRol("ADMIN")) {
            Long miAcademiaId = securityUtils.getAcademiaIdActual();
            if (miAcademiaId == null || !miAcademiaId.equals(academiaId)) {
                throw new IllegalArgumentException("No tiene acceso a esta academia");
            }
        }
    }
}
