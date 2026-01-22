package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Buscar un usuario por su nombre de usuario
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Buscar usuario por ID
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    // Listar usuarios por academia
    public List<Usuario> listarPorAcademia(Long academiaId) {
        return usuarioRepository.findByAcademiaId(academiaId);
    }

    // Listar usuarios por academia y rol
    public List<Usuario> listarPorAcademiaYRol(Long academiaId, Rol rol) {
        return usuarioRepository.findByAcademiaIdAndRol(academiaId, rol);
    }

    // Contar usuarios por academia
    public long contarPorAcademia(Long academiaId) {
        return usuarioRepository.countByAcademiaId(academiaId);
    }

    // Contar usuarios por academia y rol
    public long contarPorAcademiaYRol(Long academiaId, Rol rol) {
        return usuarioRepository.countByAcademiaIdAndRol(academiaId, rol);
    }

    // Listar todos los usuarios (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
}

