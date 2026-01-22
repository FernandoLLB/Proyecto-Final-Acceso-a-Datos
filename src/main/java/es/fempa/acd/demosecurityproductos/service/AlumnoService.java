package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.repository.AlumnoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    public List<Alumno> listarPorAcademia(Long academiaId) {
        return alumnoRepository.findByAcademiaId(academiaId);
    }

    public Alumno obtenerPorUsuarioId(Long usuarioId) {
        return alumnoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
    }

    public long contarPorAcademia(Long academiaId) {
        return alumnoRepository.countByAcademiaId(academiaId);
    }

    public long contarActivosPorAcademia(Long academiaId) {
        return alumnoRepository.countByAcademiaIdAndEstadoMatricula(academiaId, "ACTIVO");
    }

    public long contarInactivosPorAcademia(Long academiaId) {
        return alumnoRepository.countByAcademiaIdAndEstadoMatricula(academiaId, "INACTIVO");
    }

    public Alumno obtenerPorId(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
    }

    public List<Alumno> obtenerUltimosRegistrados(Long academiaId, int cantidad) {
        return alumnoRepository.findTop5ByAcademiaIdOrderByFechaRegistroDesc(academiaId);
    }
}
