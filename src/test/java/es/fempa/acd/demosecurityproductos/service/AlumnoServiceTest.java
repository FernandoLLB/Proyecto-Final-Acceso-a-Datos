package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.AlumnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para AlumnoService
 */
@ExtendWith(MockitoExtension.class)
class AlumnoServiceTest {

    @Mock
    private AlumnoRepository alumnoRepository;

    @InjectMocks
    private AlumnoService alumnoService;

    private Alumno alumno;
    private Usuario usuario;
    private Academia academia;

    @BeforeEach
    void setUp() {
        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("alumno1");
        usuario.setEmail("alumno@test.com");
        usuario.setRol(Rol.ALUMNO);
        usuario.setActivo(true);
        usuario.setAcademia(academia);

        alumno = new Alumno();
        alumno.setId(1L);
        alumno.setUsuario(usuario);
        alumno.setAcademia(academia);
        alumno.setFechaRegistro(LocalDate.now());
        alumno.setEstadoMatricula("ACTIVO");
    }

    @Test
    @DisplayName("listarPorAcademia retorna lista de alumnos")
    void listarPorAcademia() {
        List<Alumno> alumnos = Arrays.asList(alumno);
        when(alumnoRepository.findByAcademiaId(1L)).thenReturn(alumnos);

        List<Alumno> resultado = alumnoService.listarPorAcademia(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(alumnoRepository, times(1)).findByAcademiaId(1L);
    }

    @Test
    @DisplayName("listarPorAcademiaYEstado filtra por estado")
    void listarPorAcademiaYEstado() {
        List<Alumno> alumnos = Arrays.asList(alumno);
        when(alumnoRepository.findByAcademiaIdAndEstadoMatricula(1L, "ACTIVO")).thenReturn(alumnos);

        List<Alumno> resultado = alumnoService.listarPorAcademiaYEstado(1L, "ACTIVO");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ACTIVO", resultado.get(0).getEstadoMatricula());
        verify(alumnoRepository, times(1)).findByAcademiaIdAndEstadoMatricula(1L, "ACTIVO");
    }

    @Test
    @DisplayName("obtenerPorUsuarioId retorna alumno existente")
    void obtenerPorUsuarioId() {
        when(alumnoRepository.findByUsuarioId(1L)).thenReturn(Optional.of(alumno));

        Alumno resultado = alumnoService.obtenerPorUsuarioId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getUsuario().getId());
        verify(alumnoRepository, times(1)).findByUsuarioId(1L);
    }

    @Test
    @DisplayName("obtenerPorUsuarioId lanza excepción si no existe")
    void obtenerPorUsuarioIdNoExiste() {
        when(alumnoRepository.findByUsuarioId(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            alumnoService.obtenerPorUsuarioId(99L);
        });
    }

    @Test
    @DisplayName("contarPorAcademia retorna conteo correcto")
    void contarPorAcademia() {
        when(alumnoRepository.countByAcademiaId(1L)).thenReturn(10L);

        long resultado = alumnoService.contarPorAcademia(1L);

        assertEquals(10L, resultado);
        verify(alumnoRepository, times(1)).countByAcademiaId(1L);
    }

    @Test
    @DisplayName("contarActivosPorAcademia cuenta alumnos activos")
    void contarActivosPorAcademia() {
        when(alumnoRepository.countByAcademiaIdAndEstadoMatricula(1L, "ACTIVO")).thenReturn(8L);

        long resultado = alumnoService.contarActivosPorAcademia(1L);

        assertEquals(8L, resultado);
        verify(alumnoRepository, times(1)).countByAcademiaIdAndEstadoMatricula(1L, "ACTIVO");
    }

    @Test
    @DisplayName("contarInactivosPorAcademia cuenta alumnos inactivos")
    void contarInactivosPorAcademia() {
        when(alumnoRepository.countByAcademiaIdAndEstadoMatricula(1L, "INACTIVO")).thenReturn(2L);

        long resultado = alumnoService.contarInactivosPorAcademia(1L);

        assertEquals(2L, resultado);
        verify(alumnoRepository, times(1)).countByAcademiaIdAndEstadoMatricula(1L, "INACTIVO");
    }

    @Test
    @DisplayName("obtenerPorId retorna alumno existente")
    void obtenerPorId() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));

        Alumno resultado = alumnoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(alumnoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si no existe")
    void obtenerPorIdNoExiste() {
        when(alumnoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            alumnoService.obtenerPorId(99L);
        });
    }

    @Test
    @DisplayName("obtenerUltimosRegistrados retorna lista limitada")
    void obtenerUltimosRegistrados() {
        List<Alumno> alumnos = Arrays.asList(alumno);
        when(alumnoRepository.findTop5ByAcademiaIdOrderByFechaRegistroDesc(1L)).thenReturn(alumnos);

        List<Alumno> resultado = alumnoService.obtenerUltimosRegistrados(1L, 5);

        assertNotNull(resultado);
        verify(alumnoRepository, times(1)).findTop5ByAcademiaIdOrderByFechaRegistroDesc(1L);
    }

    @Test
    @DisplayName("crear guarda alumno nuevo")
    void crear() {
        Alumno nuevoAlumno = new Alumno();
        nuevoAlumno.setUsuario(usuario);
        nuevoAlumno.setAcademia(academia);

        when(alumnoRepository.save(any(Alumno.class))).thenAnswer(invocation -> {
            Alumno a = invocation.getArgument(0);
            a.setId(2L);
            return a;
        });

        Alumno resultado = alumnoService.crear(nuevoAlumno);

        assertNotNull(resultado);
        assertEquals("ACTIVO", resultado.getEstadoMatricula());
        verify(alumnoRepository, times(1)).save(any(Alumno.class));
    }

    @Test
    @DisplayName("crear establece estado ACTIVO si es null")
    void crearEstableceEstado() {
        Alumno nuevoAlumno = new Alumno();
        nuevoAlumno.setUsuario(usuario);
        nuevoAlumno.setAcademia(academia);
        nuevoAlumno.setEstadoMatricula(null);

        when(alumnoRepository.save(any(Alumno.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Alumno resultado = alumnoService.crear(nuevoAlumno);

        assertEquals("ACTIVO", resultado.getEstadoMatricula());
    }

    @Test
    @DisplayName("actualizar modifica datos del alumno")
    void actualizar() {
        alumno.setEstadoMatricula("INACTIVO");
        alumno.setObservaciones("Nuevas observaciones");

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(alumnoRepository.save(any(Alumno.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Alumno resultado = alumnoService.actualizar(alumno);

        assertEquals("INACTIVO", resultado.getEstadoMatricula());
        assertEquals("Nuevas observaciones", resultado.getObservaciones());
        verify(alumnoRepository, times(1)).save(any(Alumno.class));
    }

    @Test
    @DisplayName("activar cambia estado a ACTIVO")
    void activar() {
        alumno.setEstadoMatricula("INACTIVO");
        usuario.setActivo(false);

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(alumnoRepository.save(any(Alumno.class))).thenAnswer(invocation -> invocation.getArgument(0));

        alumnoService.activar(1L);

        assertEquals("ACTIVO", alumno.getEstadoMatricula());
        assertTrue(alumno.getUsuario().getActivo());
        verify(alumnoRepository, times(1)).save(alumno);
    }

    @Test
    @DisplayName("desactivar cambia estado a INACTIVO")
    void desactivar() {
        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(alumnoRepository.save(any(Alumno.class))).thenAnswer(invocation -> invocation.getArgument(0));

        alumnoService.desactivar(1L);

        assertEquals("INACTIVO", alumno.getEstadoMatricula());
        assertFalse(alumno.getUsuario().getActivo());
        verify(alumnoRepository, times(1)).save(alumno);
    }
}
