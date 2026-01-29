package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Curso;
import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.CursoRepository;
import es.fempa.acd.demosecurityproductos.repository.ProfesorRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProfesorService(ProfesorRepository profesorRepository,
                          CursoRepository cursoRepository,
                          UsuarioRepository usuarioRepository) {
        this.profesorRepository = profesorRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
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

    @Transactional
    public void eliminarProfesor(Long id) {
        Profesor profesor = obtenerPorId(id);

        // Verificar si el profesor tiene cursos asignados
        List<Curso> cursosAsignados = cursoRepository.findByProfesorId(id);

        if (!cursosAsignados.isEmpty()) {
            throw new IllegalStateException(
                "No se puede eliminar el profesor porque tiene " + cursosAsignados.size() +
                " curso(s) asignado(s). Por favor, reasigne o elimine los cursos primero."
            );
        }

        // Guardar referencia al usuario para eliminarlo después
        Usuario usuario = profesor.getUsuario();

        // Eliminar el profesor
        profesorRepository.delete(profesor);

        // Eliminar el usuario asociado
        if (usuario != null) {
            usuarioRepository.delete(usuario);
        }
    }
}
