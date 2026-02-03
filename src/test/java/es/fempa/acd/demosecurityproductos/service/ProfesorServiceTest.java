package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Curso;
import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.CursoRepository;
import es.fempa.acd.demosecurityproductos.repository.ProfesorRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProfesorService
 */
@ExtendWith(MockitoExtension.class)
class ProfesorServiceTest {

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ProfesorService profesorService;

    private Profesor profesor;
    private Usuario usuario;
    private Academia academia;

    @BeforeEach
    void setUp() {
        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("profesor1");
        usuario.setEmail("profesor@test.com");
        usuario.setRol(Rol.PROFESOR);
        usuario.setActivo(true);
        usuario.setAcademia(academia);

        profesor = new Profesor();
        profesor.setId(1L);
        profesor.setUsuario(usuario);
        profesor.setAcademia(academia);
        profesor.setEspecialidad("Matemáticas");
        profesor.setBiografia("Profesor con experiencia");
        profesor.setFechaContratacion(LocalDate.now());
    }

    @Test
    @DisplayName("listarPorAcademia retorna lista de profesores")
    void listarPorAcademia() {
        List<Profesor> profesores = Arrays.asList(profesor);
        when(profesorRepository.findByAcademiaId(1L)).thenReturn(profesores);

        List<Profesor> resultado = profesorService.listarPorAcademia(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Matemáticas", resultado.get(0).getEspecialidad());
        verify(profesorRepository, times(1)).findByAcademiaId(1L);
    }

    @Test
    @DisplayName("obtenerPorUsuarioId retorna profesor existente")
    void obtenerPorUsuarioId() {
        when(profesorRepository.findByUsuarioId(1L)).thenReturn(Optional.of(profesor));

        Profesor resultado = profesorService.obtenerPorUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getUsuario().getId());
        verify(profesorRepository, times(1)).findByUsuarioId(1L);
    }

    @Test
    @DisplayName("obtenerPorUsuarioId lanza excepción si no existe")
    void obtenerPorUsuarioIdNoExiste() {
        when(profesorRepository.findByUsuarioId(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            profesorService.obtenerPorUsuarioId(99L);
        });
    }

    @Test
    @DisplayName("contarPorAcademia retorna conteo correcto")
    void contarPorAcademia() {
        when(profesorRepository.countByAcademiaId(1L)).thenReturn(5L);

        long resultado = profesorService.contarPorAcademia(1L);

        assertEquals(5L, resultado);
        verify(profesorRepository, times(1)).countByAcademiaId(1L);
    }

    @Test
    @DisplayName("obtenerPorId retorna profesor existente")
    void obtenerPorId() {
        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesor));

        Profesor resultado = profesorService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(profesorRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si no existe")
    void obtenerPorIdNoExiste() {
        when(profesorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            profesorService.obtenerPorId(99L);
        });
    }

    @Test
    @DisplayName("eliminarProfesor borra profesor sin cursos")
    void eliminarProfesorSinCursos() {
        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesor));
        when(cursoRepository.findByProfesorId(1L)).thenReturn(Collections.emptyList());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        doNothing().when(profesorRepository).delete(profesor);

        profesorService.eliminarProfesor(1L);

        assertFalse(usuario.getActivo());
        verify(usuarioRepository, times(1)).save(usuario);
        verify(profesorRepository, times(1)).delete(profesor);
    }

    @Test
    @DisplayName("eliminarProfesor lanza excepción si tiene cursos asignados")
    void eliminarProfesorConCursos() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso Test");
        curso.setProfesor(profesor);

        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesor));
        when(cursoRepository.findByProfesorId(1L)).thenReturn(Arrays.asList(curso));

        assertThrows(IllegalStateException.class, () -> {
            profesorService.eliminarProfesor(1L);
        });

        verify(profesorRepository, never()).delete(any(Profesor.class));
    }

    @Test
    @DisplayName("eliminarProfesor desactiva usuario correctamente")
    void eliminarProfesorDesactivaUsuario() {
        when(profesorRepository.findById(1L)).thenReturn(Optional.of(profesor));
        when(cursoRepository.findByProfesorId(1L)).thenReturn(Collections.emptyList());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(profesorRepository).delete(profesor);

        profesorService.eliminarProfesor(1L);

        assertFalse(usuario.getActivo());
        verify(usuarioRepository, times(1)).save(usuario);
    }
}
