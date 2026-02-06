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

/**
 * Servicio para la gestión de usuarios en el sistema.
 * Proporciona métodos para crear, actualizar, buscar y listar usuarios,
 * así como gestionar sus contraseñas y validaciones.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor del servicio de usuarios.
     *
     * @param usuarioRepository repositorio de usuarios
     * @param passwordEncoder codificador de contraseñas
     */
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return Optional con el usuario si existe, vacío si no
     */
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id identificador del usuario
     * @return el usuario encontrado
     * @throws IllegalArgumentException si el usuario no existe
     */
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    /**
     * Lista todos los usuarios de una academia específica.
     *
     * @param academiaId identificador de la academia
     * @return lista de usuarios de la academia
     */
    public List<Usuario> listarPorAcademia(Long academiaId) {
        return usuarioRepository.findByAcademiaId(academiaId);
    }

    /**
     * Lista todos los usuarios de una academia con un rol específico.
     *
     * @param academiaId identificador de la academia
     * @param rol rol a filtrar
     * @return lista de usuarios de la academia con el rol especificado
     */
    public List<Usuario> listarPorAcademiaYRol(Long academiaId, Rol rol) {
        return usuarioRepository.findByAcademiaIdAndRol(academiaId, rol);
    }

    /**
     * Cuenta el número de usuarios en una academia.
     *
     * @param academiaId identificador de la academia
     * @return número de usuarios en la academia
     */
    public long contarPorAcademia(Long academiaId) {
        return usuarioRepository.countByAcademiaId(academiaId);
    }

    /**
     * Cuenta el número de usuarios con un rol específico en una academia.
     *
     * @param academiaId identificador de la academia
     * @param rol rol a contar
     * @return número de usuarios con ese rol en la academia
     */
    public long contarPorAcademiaYRol(Long academiaId, Rol rol) {
        return usuarioRepository.countByAcademiaIdAndRol(academiaId, rol);
    }

    /**
     * Lista todos los usuarios del sistema.
     * Solo accesible por usuarios con rol ADMIN.
     *
     * @return lista de todos los usuarios
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param username nombre de usuario único
     * @param password contraseña sin encriptar
     * @param email correo electrónico único
     * @param rol rol del usuario en el sistema
     * @return el usuario creado
     * @throws IllegalArgumentException si el username o email ya existen
     */
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

    /**
     * Actualiza un usuario existente.
     *
     * @param usuario usuario con los datos actualizados
     * @return el usuario actualizado
     */
    @Transactional
    public Usuario actualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Guarda un usuario en el sistema.
     * Método público para guardar un usuario ya configurado.
     *
     * @param usuario usuario a guardar
     * @return el usuario guardado
     */
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Crea un nuevo usuario con rol de alumno asociado a una academia.
     * Utilizado principalmente en el proceso de registro público.
     *
     * @param username nombre de usuario único
     * @param password contraseña sin encriptar
     * @param email correo electrónico único
     * @param nombre nombre del alumno
     * @param apellidos apellidos del alumno
     * @param academia academia a la que se asocia el alumno
     * @return el usuario alumno creado
     * @throws IllegalArgumentException si el username o email ya existen
     */
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
