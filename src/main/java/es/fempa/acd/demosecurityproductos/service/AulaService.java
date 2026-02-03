package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Aula;
import es.fempa.acd.demosecurityproductos.repository.AulaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para la gestión de aulas.
 * Proporciona operaciones CRUD y consultas para aulas de academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Service
public class AulaService {

    private final AulaRepository aulaRepository;
    private final AcademiaService academiaService;
    private final SecurityUtils securityUtils;

    /**
     * Constructor del servicio de aulas.
     *
     * @param aulaRepository repositorio de aulas
     * @param academiaService servicio de academias
     * @param securityUtils utilidades de seguridad
     */
    public AulaService(AulaRepository aulaRepository,
                      AcademiaService academiaService,
                      SecurityUtils securityUtils) {
        this.aulaRepository = aulaRepository;
        this.academiaService = academiaService;
        this.securityUtils = securityUtils;
    }

    /**
     * Crea una nueva aula en el sistema.
     * Valida que el aula pertenezca a la academia del usuario y que el nombre sea único.
     *
     * @param aula el aula a crear
     * @return el aula creada con su ID asignado
     * @throws IllegalStateException si el usuario no tiene academia asignada
     * @throws IllegalArgumentException si el aula pertenece a otra academia o el nombre ya existe
     */
    @Transactional
    public Aula crear(Aula aula) {
        // Validar que el aula pertenece a la academia del usuario
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            throw new IllegalStateException("Usuario sin academia asignada");
        }

        if (aula.getAcademia() == null || !aula.getAcademia().getId().equals(academiaId)) {
            throw new IllegalArgumentException("No puede crear aulas para otra academia");
        }

        // Validar nombre único en la academia
        if (aulaRepository.existsByNombreAndAcademiaIdExcludingId(
                aula.getNombre(), academiaId, null)) {
            throw new IllegalArgumentException("Ya existe un aula con ese nombre en esta academia");
        }

        aula.setActiva(true);
        return aulaRepository.save(aula);
    }

    /**
     * Lista todas las aulas de una academia.
     * Valida que el usuario tenga acceso a la academia.
     *
     * @param academiaId el ID de la academia
     * @return lista de aulas de la academia
     * @throws IllegalArgumentException si el usuario no tiene acceso a la academia
     */
    @Transactional(readOnly = true)
    public List<Aula> listarPorAcademia(Long academiaId) {
        // Validar acceso a la academia
        validarAccesoAcademia(academiaId);
        return aulaRepository.findByAcademiaId(academiaId);
    }

    /**
     * Lista las aulas activas de una academia.
     * Valida que el usuario tenga acceso a la academia.
     *
     * @param academiaId el ID de la academia
     * @return lista de aulas activas
     * @throws IllegalArgumentException si el usuario no tiene acceso a la academia
     */
    @Transactional(readOnly = true)
    public List<Aula> listarActivasPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return aulaRepository.findByAcademiaIdAndActiva(academiaId, true);
    }

    /**
     * Obtiene un aula por su ID.
     * Valida que el aula pertenezca a la academia del usuario autenticado.
     *
     * @param id el ID del aula
     * @return el aula encontrada
     * @throws IllegalStateException si el usuario no tiene academia asignada
     * @throws IllegalArgumentException si el aula no existe o no pertenece a la academia
     */
    @Transactional(readOnly = true)
    public Aula obtenerPorId(Long id) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            throw new IllegalStateException("Usuario sin academia asignada");
        }

        return aulaRepository.findByIdAndAcademiaId(id, academiaId)
            .orElseThrow(() -> new IllegalArgumentException("Aula no encontrada o no pertenece a su academia"));
    }

    /**
     * Actualiza los datos de un aula existente.
     * Valida que el nombre sea único dentro de la academia.
     *
     * @param id el ID del aula a actualizar
     * @param aulaActualizada los nuevos datos del aula
     * @return el aula actualizada
     * @throws IllegalArgumentException si el nombre ya existe o el aula no pertenece a la academia
     */
    @Transactional
    public Aula actualizar(Long id, Aula aulaActualizada) {
        Aula aulaExistente = obtenerPorId(id);
        Long academiaId = securityUtils.getAcademiaIdActual();

        // Validar nombre único (excluyendo el aula actual)
        if (aulaRepository.existsByNombreAndAcademiaIdExcludingId(
                aulaActualizada.getNombre(), academiaId, id)) {
            throw new IllegalArgumentException("Ya existe un aula con ese nombre en esta academia");
        }

        aulaExistente.setNombre(aulaActualizada.getNombre());
        aulaExistente.setCapacidad(aulaActualizada.getCapacidad());
        aulaExistente.setRecursos(aulaActualizada.getRecursos());

        return aulaRepository.save(aulaExistente);
    }

    /**
     * Activa un aula previamente desactivada.
     *
     * @param id el ID del aula a activar
     * @throws IllegalArgumentException si el aula no existe o no pertenece a la academia
     */
    @Transactional
    public void activar(Long id) {
        Aula aula = obtenerPorId(id);
        aula.setActiva(true);
        aulaRepository.save(aula);
    }

    /**
     * Desactiva un aula.
     *
     * @param id el ID del aula a desactivar
     * @throws IllegalArgumentException si el aula no existe o no pertenece a la academia
     */
    @Transactional
    public void desactivar(Long id) {
        Aula aula = obtenerPorId(id);
        aula.setActiva(false);
        aulaRepository.save(aula);
    }

    /**
     * Cuenta el total de aulas de una academia.
     *
     * @param academiaId el ID de la academia
     * @return número total de aulas
     * @throws IllegalArgumentException si el usuario no tiene acceso a la academia
     */
    @Transactional(readOnly = true)
    public long contarPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return aulaRepository.countByAcademiaId(academiaId);
    }

    /**
     * Cuenta las aulas activas de una academia.
     *
     * @param academiaId el ID de la academia
     * @return número de aulas activas
     * @throws IllegalArgumentException si el usuario no tiene acceso a la academia
     */
    @Transactional(readOnly = true)
    public long contarActivasPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return aulaRepository.countByAcademiaIdAndActiva(academiaId, true);
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
