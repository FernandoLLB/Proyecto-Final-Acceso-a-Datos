package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.*;
import es.fempa.acd.demosecurityproductos.repository.MatriculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para MatriculaService
 */
@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private CursoService cursoService;

    @Mock
    private AlumnoService alumnoService;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private MatriculaService matriculaService;

    private Matricula matricula;
    private Academia academia;
    private Alumno alumno;
    private Curso curso;
    private Usuario usuario;
    private Usuario secretaria;

    @BeforeEach
    void setUp() {
        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("alumno1");
        usuario.setRol(Rol.ALUMNO);
        usuario.setAcademia(academia);

        alumno = new Alumno();
        alumno.setId(1L);
        alumno.setUsuario(usuario);
        alumno.setAcademia(academia);
        alumno.setEstadoMatricula("ACTIVO");
        alumno.setFechaRegistro(LocalDate.now());

        Profesor profesor = new Profesor();
        profesor.setId(1L);
        profesor.setAcademia(academia);

        curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso Test");
        curso.setAcademia(academia);
        curso.setProfesor(profesor);
        curso.setActivo(true);
        curso.setPlazasDisponibles(20);
        curso.setFechaInicio(LocalDate.now());
        curso.setFechaFin(LocalDate.now().plusMonths(3));
        curso.setPrecio(new BigDecimal("100.00"));

        secretaria = new Usuario();
        secretaria.setId(2L);
        secretaria.setUsername("secretaria1");
        secretaria.setRol(Rol.SECRETARIA);
        secretaria.setAcademia(academia);

        matricula = new Matricula();
        matricula.setId(1L);
        matricula.setAcademia(academia);
        matricula.setAlumno(alumno);
        matricula.setCurso(curso);
        matricula.setFechaMatriculacion(LocalDateTime.now());
        matricula.setEstado(EstadoMatricula.ACTIVA);
        matricula.setMatriculadoPor(secretaria);
    }

    @Test
    @DisplayName("matricular crea nueva matrícula correctamente")
    void matricular() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(securityUtils.getUsuarioAutenticado()).thenReturn(secretaria);
        when(alumnoService.obtenerPorId(1L)).thenReturn(alumno);
        when(cursoService.obtenerPorId(1L)).thenReturn(curso);
        when(matriculaRepository.existeMatriculaActiva(1L, 1L)).thenReturn(false);
        when(matriculaRepository.countByCursoIdAndEstado(1L, EstadoMatricula.ACTIVA)).thenReturn(5L);
        when(matriculaRepository.save(any(Matricula.class))).thenAnswer(invocation -> {
            Matricula m = invocation.getArgument(0);
            m.setId(2L);
            return m;
        });

        Matricula resultado = matriculaService.matricular(1L, 1L, "Observaciones");

        assertNotNull(resultado);
        assertEquals(EstadoMatricula.ACTIVA, resultado.getEstado());
        assertEquals(alumno, resultado.getAlumno());
        assertEquals(curso, resultado.getCurso());
        verify(matriculaRepository, times(1)).save(any(Matricula.class));
    }

    @Test
    @DisplayName("matricular lanza excepción si usuario sin academia")
    void matricularSinAcademia() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(null);
        when(securityUtils.getUsuarioAutenticado()).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> {
            matriculaService.matricular(1L, 1L, null);
        });
    }

    @Test
    @DisplayName("matricular lanza excepción si alumno no está activo")
    void matricularAlumnoInactivo() {
        alumno.setEstadoMatricula("INACTIVO");

        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(securityUtils.getUsuarioAutenticado()).thenReturn(secretaria);
        when(alumnoService.obtenerPorId(1L)).thenReturn(alumno);
        when(cursoService.obtenerPorId(1L)).thenReturn(curso);

        assertThrows(IllegalArgumentException.class, () -> {
            matriculaService.matricular(1L, 1L, null);
        });
    }

    @Test
    @DisplayName("matricular lanza excepción si curso no está activo")
    void matricularCursoInactivo() {
        curso.setActivo(false);

        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(securityUtils.getUsuarioAutenticado()).thenReturn(secretaria);
        when(alumnoService.obtenerPorId(1L)).thenReturn(alumno);
        when(cursoService.obtenerPorId(1L)).thenReturn(curso);

        assertThrows(IllegalArgumentException.class, () -> {
            matriculaService.matricular(1L, 1L, null);
        });
    }

    @Test
    @DisplayName("matricular lanza excepción si ya existe matrícula activa")
    void matricularYaExiste() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(securityUtils.getUsuarioAutenticado()).thenReturn(secretaria);
        when(alumnoService.obtenerPorId(1L)).thenReturn(alumno);
        when(cursoService.obtenerPorId(1L)).thenReturn(curso);
        when(matriculaRepository.existeMatriculaActiva(1L, 1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            matriculaService.matricular(1L, 1L, null);
        });
    }

    @Test
    @DisplayName("matricular lanza excepción si no hay plazas disponibles")
    void matricularSinPlazas() {
        curso.setPlazasDisponibles(5);

        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(securityUtils.getUsuarioAutenticado()).thenReturn(secretaria);
        when(alumnoService.obtenerPorId(1L)).thenReturn(alumno);
        when(cursoService.obtenerPorId(1L)).thenReturn(curso);
        when(matriculaRepository.existeMatriculaActiva(1L, 1L)).thenReturn(false);
        when(matriculaRepository.countByCursoIdAndEstado(1L, EstadoMatricula.ACTIVA)).thenReturn(5L);

        assertThrows(IllegalStateException.class, () -> {
            matriculaService.matricular(1L, 1L, null);
        });
    }

    @Test
    @DisplayName("listarPorAcademia retorna lista de matrículas")
    void listarPorAcademia() {
        List<Matricula> matriculas = Arrays.asList(matricula);
        when(securityUtils.tieneRol("ADMIN")).thenReturn(false);
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(matriculaRepository.findByAcademiaId(1L)).thenReturn(matriculas);

        List<Matricula> resultado = matriculaService.listarPorAcademia(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(matriculaRepository, times(1)).findByAcademiaId(1L);
    }

    @Test
    @DisplayName("listarPorAlumno retorna matrículas del alumno")
    void listarPorAlumno() {
        List<Matricula> matriculas = Arrays.asList(matricula);
        when(alumnoService.obtenerPorId(1L)).thenReturn(alumno);
        when(matriculaRepository.findByAlumnoId(1L)).thenReturn(matriculas);

        List<Matricula> resultado = matriculaService.listarPorAlumno(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(matriculaRepository, times(1)).findByAlumnoId(1L);
    }

    @Test
    @DisplayName("listarActivasPorAlumno retorna matrículas activas del alumno")
    void listarActivasPorAlumno() {
        List<Matricula> matriculas = Arrays.asList(matricula);
        when(alumnoService.obtenerPorId(1L)).thenReturn(alumno);
        when(matriculaRepository.findByAlumnoIdAndEstado(1L, EstadoMatricula.ACTIVA)).thenReturn(matriculas);

        List<Matricula> resultado = matriculaService.listarActivasPorAlumno(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(EstadoMatricula.ACTIVA, resultado.get(0).getEstado());
        verify(matriculaRepository, times(1)).findByAlumnoIdAndEstado(1L, EstadoMatricula.ACTIVA);
    }

    @Test
    @DisplayName("listarPorCurso retorna matrículas del curso")
    void listarPorCurso() {
        List<Matricula> matriculas = Arrays.asList(matricula);
        when(cursoService.obtenerPorId(1L)).thenReturn(curso);
        when(matriculaRepository.findByCursoId(1L)).thenReturn(matriculas);

        List<Matricula> resultado = matriculaService.listarPorCurso(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(matriculaRepository, times(1)).findByCursoId(1L);
    }

    @Test
    @DisplayName("obtenerPorId retorna matrícula existente")
    void obtenerPorId() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(matriculaRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(matricula));

        Matricula resultado = matriculaService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(matriculaRepository, times(1)).findByIdAndAcademiaId(1L, 1L);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si no existe")
    void obtenerPorIdNoExiste() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(matriculaRepository.findByIdAndAcademiaId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            matriculaService.obtenerPorId(99L);
        });
    }

    @Test
    @DisplayName("completar cambia estado a COMPLETADA")
    void completar() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(matriculaRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(matricula));
        when(matriculaRepository.save(any(Matricula.class))).thenAnswer(invocation -> invocation.getArgument(0));

        matriculaService.completar(1L);

        assertEquals(EstadoMatricula.COMPLETADA, matricula.getEstado());
        verify(matriculaRepository, times(1)).save(matricula);
    }

    @Test
    @DisplayName("completar lanza excepción si matrícula no está activa")
    void completarNoActiva() {
        matricula.setEstado(EstadoMatricula.CANCELADA);

        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(matriculaRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(matricula));

        assertThrows(IllegalStateException.class, () -> {
            matriculaService.completar(1L);
        });
    }

    @Test
    @DisplayName("cancelar cambia estado a CANCELADA")
    void cancelar() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(matriculaRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(matricula));
        when(matriculaRepository.save(any(Matricula.class))).thenAnswer(invocation -> invocation.getArgument(0));

        matriculaService.cancelar(1L, "Motivo de cancelación");

        assertEquals(EstadoMatricula.CANCELADA, matricula.getEstado());
        assertTrue(matricula.getObservaciones().contains("Motivo de cancelación"));
        verify(matriculaRepository, times(1)).save(matricula);
    }

    @Test
    @DisplayName("cancelar lanza excepción si ya está cancelada")
    void cancelarYaCancelada() {
        matricula.setEstado(EstadoMatricula.CANCELADA);

        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(matriculaRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(matricula));

        assertThrows(IllegalStateException.class, () -> {
            matriculaService.cancelar(1L, "Motivo");
        });
    }

    @Test
    @DisplayName("eliminar borra matrícula")
    void eliminar() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(matriculaRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(matricula));
        doNothing().when(matriculaRepository).delete(matricula);

        matriculaService.eliminar(1L);

        verify(matriculaRepository, times(1)).delete(matricula);
    }

    @Test
    @DisplayName("contarMatriculasActivasCurso retorna conteo correcto")
    void contarMatriculasActivasCurso() {
        when(matriculaRepository.countByCursoIdAndEstado(1L, EstadoMatricula.ACTIVA)).thenReturn(10L);

        long resultado = matriculaService.contarMatriculasActivasCurso(1L);

        assertEquals(10L, resultado);
        verify(matriculaRepository, times(1)).countByCursoIdAndEstado(1L, EstadoMatricula.ACTIVA);
    }

    @Test
    @DisplayName("contarMatriculasActivasAlumno retorna conteo correcto")
    void contarMatriculasActivasAlumno() {
        when(matriculaRepository.countByAlumnoIdAndEstado(1L, EstadoMatricula.ACTIVA)).thenReturn(3L);

        long resultado = matriculaService.contarMatriculasActivasAlumno(1L);

        assertEquals(3L, resultado);
        verify(matriculaRepository, times(1)).countByAlumnoIdAndEstado(1L, EstadoMatricula.ACTIVA);
    }

    @Test
    @DisplayName("obtenerMisMatriculas retorna matrículas del alumno")
    void obtenerMisMatriculas() {
        List<Matricula> matriculas = Arrays.asList(matricula);
        when(matriculaRepository.findByAlumnoId(1L)).thenReturn(matriculas);

        List<Matricula> resultado = matriculaService.obtenerMisMatriculas(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(matriculaRepository, times(1)).findByAlumnoId(1L);
    }

    @Test
    @DisplayName("obtenerMisMatriculasActivas retorna matrículas activas del alumno")
    void obtenerMisMatriculasActivas() {
        List<Matricula> matriculas = Arrays.asList(matricula);
        when(matriculaRepository.findByAlumnoIdAndEstado(1L, EstadoMatricula.ACTIVA)).thenReturn(matriculas);

        List<Matricula> resultado = matriculaService.obtenerMisMatriculasActivas(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(matriculaRepository, times(1)).findByAlumnoIdAndEstado(1L, EstadoMatricula.ACTIVA);
    }
}
