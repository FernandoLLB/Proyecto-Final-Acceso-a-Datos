package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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

    // Crear usuario
    @Transactional
    public Usuario crearUsuario(String username, String password, String email, Rol rol) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEmail(email);
        usuario.setRol(rol);
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    // Actualizar usuario
    @Transactional
    public Usuario actualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Crear usuario alumno con academia (para registro público)
    @Transactional
    public Usuario crearUsuarioAlumno(String username, String password, String email,
                                       String nombre, String apellidos, Academia academia) {
        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEmail(email);
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setRol(Rol.ALUMNO);
        usuario.setActivo(true);
        usuario.setAcademia(academia);

        return usuarioRepository.save(usuario);
    }
}
