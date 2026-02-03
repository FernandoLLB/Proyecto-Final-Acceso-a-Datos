package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para UsuarioService
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Academia academia;

    @BeforeEach
    void setUp() {
        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setPassword("encodedPassword");
        usuario.setEmail("test@test.com");
        usuario.setNombre("Test");
        usuario.setApellidos("User");
        usuario.setRol(Rol.ALUMNO);
        usuario.setActivo(true);
        usuario.setAcademia(academia);
    }

    @Test
    @DisplayName("buscarPorUsername retorna usuario existente")
    void buscarPorUsernameExiste() {
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorUsername("testuser");

        assertTrue(resultado.isPresent());
        assertEquals("testuser", resultado.get().getUsername());
        verify(usuarioRepository, times(1)).findByUsername("testuser");
    }

    @Test
    @DisplayName("buscarPorUsername retorna vacío si no existe")
    void buscarPorUsernameNoExiste() {
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.buscarPorUsername("noexiste");

        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findByUsername("noexiste");
    }

    @Test
    @DisplayName("obtenerUsuarioPorId retorna usuario existente")
    void obtenerUsuarioPorIdExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obtenerUsuarioPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerUsuarioPorId lanza excepción si no existe")
    void obtenerUsuarioPorIdNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.obtenerUsuarioPorId(99L);
        });
    }

    @Test
    @DisplayName("listarPorAcademia retorna lista de usuarios")
    void listarPorAcademia() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findByAcademiaId(1L)).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarPorAcademia(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findByAcademiaId(1L);
    }

    @Test
    @DisplayName("listarPorAcademiaYRol retorna lista filtrada")
    void listarPorAcademiaYRol() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findByAcademiaIdAndRol(1L, Rol.ALUMNO)).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarPorAcademiaYRol(1L, Rol.ALUMNO);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(Rol.ALUMNO, resultado.get(0).getRol());
        verify(usuarioRepository, times(1)).findByAcademiaIdAndRol(1L, Rol.ALUMNO);
    }

    @Test
    @DisplayName("contarPorAcademia retorna conteo correcto")
    void contarPorAcademia() {
        when(usuarioRepository.countByAcademiaId(1L)).thenReturn(5L);

        long resultado = usuarioService.contarPorAcademia(1L);

        assertEquals(5L, resultado);
        verify(usuarioRepository, times(1)).countByAcademiaId(1L);
    }

    @Test
    @DisplayName("contarPorAcademiaYRol retorna conteo filtrado")
    void contarPorAcademiaYRol() {
        when(usuarioRepository.countByAcademiaIdAndRol(1L, Rol.PROFESOR)).thenReturn(3L);

        long resultado = usuarioService.contarPorAcademiaYRol(1L, Rol.PROFESOR);

        assertEquals(3L, resultado);
        verify(usuarioRepository, times(1)).countByAcademiaIdAndRol(1L, Rol.PROFESOR);
    }

    @Test
    @DisplayName("crearUsuario crea usuario correctamente")
    void crearUsuario() {
        when(usuarioRepository.existsByUsername("newuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });

        Usuario resultado = usuarioService.crearUsuario("newuser", "password123", "new@test.com", Rol.ALUMNO);

        assertNotNull(resultado);
        assertEquals("newuser", resultado.getUsername());
        assertEquals("encodedPassword", resultado.getPassword());
        assertEquals("new@test.com", resultado.getEmail());
        assertEquals(Rol.ALUMNO, resultado.getRol());
        assertTrue(resultado.getActivo());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("crearUsuario lanza excepción si username existe")
    void crearUsuarioUsernameExiste() {
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario("testuser", "password", "new@test.com", Rol.ALUMNO);
        });
    }

    @Test
    @DisplayName("crearUsuario lanza excepción si email existe")
    void crearUsuarioEmailExiste() {
        when(usuarioRepository.existsByUsername("newuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.crearUsuario("newuser", "password", "test@test.com", Rol.ALUMNO);
        });
    }

    @Test
    @DisplayName("actualizar guarda cambios del usuario")
    void actualizar() {
        usuario.setNombre("Nombre Actualizado");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.actualizar(usuario);

        assertNotNull(resultado);
        assertEquals("Nombre Actualizado", resultado.getNombre());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("crearUsuarioAlumno crea alumno con academia asignada")
    void crearUsuarioAlumno() {
        when(usuarioRepository.existsByUsername("alumno1")).thenReturn(false);
        when(usuarioRepository.existsByEmail("alumno@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(3L);
            return u;
        });

        Usuario resultado = usuarioService.crearUsuarioAlumno(
                "alumno1", "password", "alumno@test.com",
                "Juan", "García", academia
        );

        assertNotNull(resultado);
        assertEquals("alumno1", resultado.getUsername());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("García", resultado.getApellidos());
        assertEquals(Rol.ALUMNO, resultado.getRol());
        assertEquals(academia, resultado.getAcademia());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}
