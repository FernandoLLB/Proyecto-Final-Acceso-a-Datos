package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.repository.ProfesorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;

    public ProfesorService(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    public List<Profesor> listarPorAcademia(Long academiaId) {
        return profesorRepository.findByAcademiaId(academiaId);
    }

    public Profesor obtenerPorUsuarioId(Long usuarioId) {
        return profesorRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado"));
    }

    public long contarPorAcademia(Long academiaId) {
        return profesorRepository.countByAcademiaId(academiaId);
    }

    public Profesor obtenerPorId(Long id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado"));
    }
}
