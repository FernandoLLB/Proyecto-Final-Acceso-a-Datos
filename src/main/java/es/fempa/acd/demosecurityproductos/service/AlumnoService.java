package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.repository.AlumnoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    @Transactional(readOnly = true)
    public List<Alumno> listarPorAcademia(Long academiaId) {
        return alumnoRepository.findByAcademiaId(academiaId);
    }

    @Transactional(readOnly = true)
    public List<Alumno> listarPorAcademiaYEstado(Long academiaId, String estado) {
        return alumnoRepository.findByAcademiaIdAndEstadoMatricula(academiaId, estado);
    }

    @Transactional(readOnly = true)
    public Alumno obtenerPorUsuarioId(Long usuarioId) {
        return alumnoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
    }

    @Transactional(readOnly = true)
    public long contarPorAcademia(Long academiaId) {
        return alumnoRepository.countByAcademiaId(academiaId);
    }

    @Transactional(readOnly = true)
    public long contarActivosPorAcademia(Long academiaId) {
        return alumnoRepository.countByAcademiaIdAndEstadoMatricula(academiaId, "ACTIVO");
    }

    @Transactional(readOnly = true)
    public long contarInactivosPorAcademia(Long academiaId) {
        return alumnoRepository.countByAcademiaIdAndEstadoMatricula(academiaId, "INACTIVO");
    }

    @Transactional(readOnly = true)
    public Alumno obtenerPorId(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Alumno> obtenerUltimosRegistrados(Long academiaId, int cantidad) {
        return alumnoRepository.findTop5ByAcademiaIdOrderByFechaRegistroDesc(academiaId);
    }

    @Transactional
    public Alumno crear(Alumno alumno) {
        if (alumno.getEstadoMatricula() == null) {
            alumno.setEstadoMatricula("ACTIVO");
        }
        return alumnoRepository.save(alumno);
    }

    @Transactional
    public Alumno actualizar(Alumno alumno) {
        Alumno existente = obtenerPorId(alumno.getId());
        existente.setEstadoMatricula(alumno.getEstadoMatricula());
        existente.setObservaciones(alumno.getObservaciones());
        return alumnoRepository.save(existente);
    }

    @Transactional
    public void activar(Long id) {
        Alumno alumno = obtenerPorId(id);
        alumno.setEstadoMatricula("ACTIVO");
        alumno.getUsuario().setActivo(true);
        alumnoRepository.save(alumno);
    }

    @Transactional
    public void desactivar(Long id) {
        Alumno alumno = obtenerPorId(id);
        alumno.setEstadoMatricula("INACTIVO");
        alumno.getUsuario().setActivo(false);
        alumnoRepository.save(alumno);
    }
}
