package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Aula;
import es.fempa.acd.demosecurityproductos.repository.AulaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AulaService {

    private final AulaRepository aulaRepository;
    private final AcademiaService academiaService;
    private final SecurityUtils securityUtils;

    public AulaService(AulaRepository aulaRepository,
                      AcademiaService academiaService,
                      SecurityUtils securityUtils) {
        this.aulaRepository = aulaRepository;
        this.academiaService = academiaService;
        this.securityUtils = securityUtils;
    }

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

    @Transactional(readOnly = true)
    public List<Aula> listarPorAcademia(Long academiaId) {
        // Validar acceso a la academia
        validarAccesoAcademia(academiaId);
        return aulaRepository.findByAcademiaId(academiaId);
    }

    @Transactional(readOnly = true)
    public List<Aula> listarActivasPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return aulaRepository.findByAcademiaIdAndActiva(academiaId, true);
    }

    @Transactional(readOnly = true)
    public Aula obtenerPorId(Long id) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            throw new IllegalStateException("Usuario sin academia asignada");
        }

        return aulaRepository.findByIdAndAcademiaId(id, academiaId)
            .orElseThrow(() -> new IllegalArgumentException("Aula no encontrada o no pertenece a su academia"));
    }

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

    @Transactional
    public void activar(Long id) {
        Aula aula = obtenerPorId(id);
        aula.setActiva(true);
        aulaRepository.save(aula);
    }

    @Transactional
    public void desactivar(Long id) {
        Aula aula = obtenerPorId(id);
        aula.setActiva(false);
        aulaRepository.save(aula);
    }

    @Transactional(readOnly = true)
    public long contarPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return aulaRepository.countByAcademiaId(academiaId);
    }

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
