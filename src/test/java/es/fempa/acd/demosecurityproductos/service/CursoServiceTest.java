package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Curso;
import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.CursoRepository;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para CursoService
 */
@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private ProfesorService profesorService;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;
    private Academia academia;
    private Profesor profesor;
    private Usuario usuarioProfesor;

    @BeforeEach
    void setUp() {
        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");

        usuarioProfesor = new Usuario();
        usuarioProfesor.setId(1L);
        usuarioProfesor.setUsername("profesor1");
        usuarioProfesor.setRol(Rol.PROFESOR);

        profesor = new Profesor();
        profesor.setId(1L);
        profesor.setUsuario(usuarioProfesor);
        profesor.setAcademia(academia);
        profesor.setEspecialidad("Matemáticas");

        curso = new Curso();
        curso.setId(1L);
        curso.setAcademia(academia);
        curso.setNombre("Curso de Java");
        curso.setDescripcion("Aprende Java desde cero");
        curso.setDuracionHoras(40);
        curso.setPrecio(new BigDecimal("199.99"));
        curso.setFechaInicio(LocalDate.of(2024, 3, 1));
        curso.setFechaFin(LocalDate.of(2024, 6, 30));
        curso.setCategoria("Programación");
        curso.setProfesor(profesor);
        curso.setPlazasDisponibles(20);
        curso.setActivo(true);
    }

    @Test
    @DisplayName("crear guarda curso correctamente")
    void crear() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> {
            Curso c = invocation.getArgument(0);
            c.setId(2L);
            return c;
        });

        Curso resultado = cursoService.crear(curso);

        assertNotNull(resultado);
        assertTrue(resultado.getActivo());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    @DisplayName("crear lanza excepción si usuario sin academia")
    void crearSinAcademia() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> {
            cursoService.crear(curso);
        });
    }

    @Test
    @DisplayName("crear lanza excepción si academia no coincide")
    void crearAcademiaNoCoincide() {
        Academia otraAcademia = new Academia();
        otraAcademia.setId(2L);
        curso.setAcademia(otraAcademia);

        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            cursoService.crear(curso);
        });
    }

    @Test
    @DisplayName("crear lanza excepción si fecha fin es anterior a fecha inicio")
    void crearFechasInvalidas() {
        curso.setFechaInicio(LocalDate.of(2024, 6, 30));
        curso.setFechaFin(LocalDate.of(2024, 3, 1));

        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            cursoService.crear(curso);
        });
    }

    @Test
    @DisplayName("crear lanza excepción si profesor es de otra academia")
    void crearProfesorOtraAcademia() {
        Academia otraAcademia = new Academia();
        otraAcademia.setId(2L);
        profesor.setAcademia(otraAcademia);

        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            cursoService.crear(curso);
        });
    }

    @Test
    @DisplayName("listarPorAcademia retorna lista de cursos")
    void listarPorAcademia() {
        List<Curso> cursos = Arrays.asList(curso);
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(securityUtils.tieneRol("ADMIN")).thenReturn(false);
        when(cursoRepository.findByAcademiaId(1L)).thenReturn(cursos);

        List<Curso> resultado = cursoService.listarPorAcademia(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(cursoRepository, times(1)).findByAcademiaId(1L);
    }

    @Test
    @DisplayName("listarActivosPorAcademia retorna solo cursos activos")
    void listarActivosPorAcademia() {
        List<Curso> cursos = Arrays.asList(curso);
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(securityUtils.tieneRol("ADMIN")).thenReturn(false);
        when(cursoRepository.findByAcademiaIdAndActivo(1L, true)).thenReturn(cursos);

        List<Curso> resultado = cursoService.listarActivosPorAcademia(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
        verify(cursoRepository, times(1)).findByAcademiaIdAndActivo(1L, true);
    }

    @Test
    @DisplayName("listarPorProfesor retorna cursos del profesor")
    void listarPorProfesor() {
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findByProfesorId(1L)).thenReturn(cursos);

        List<Curso> resultado = cursoService.listarPorProfesor(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(cursoRepository, times(1)).findByProfesorId(1L);
    }

    @Test
    @DisplayName("listarActivosPorProfesor retorna cursos activos del profesor")
    void listarActivosPorProfesor() {
        List<Curso> cursos = Arrays.asList(curso);
        when(cursoRepository.findByProfesorIdAndActivo(1L, true)).thenReturn(cursos);

        List<Curso> resultado = cursoService.listarActivosPorProfesor(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(cursoRepository, times(1)).findByProfesorIdAndActivo(1L, true);
    }

    @Test
    @DisplayName("obtenerPorId retorna curso existente")
    void obtenerPorId() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(cursoRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(curso));

        Curso resultado = cursoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(cursoRepository, times(1)).findByIdAndAcademiaId(1L, 1L);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si no existe")
    void obtenerPorIdNoExiste() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(cursoRepository.findByIdAndAcademiaId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            cursoService.obtenerPorId(99L);
        });
    }

    @Test
    @DisplayName("actualizar modifica curso existente")
    void actualizar() {
        Curso cursoActualizado = new Curso();
        cursoActualizado.setNombre("Curso Actualizado");
        cursoActualizado.setDescripcion("Nueva descripción");
        cursoActualizado.setDuracionHoras(50);
        cursoActualizado.setPrecio(new BigDecimal("249.99"));
        cursoActualizado.setFechaInicio(LocalDate.of(2024, 4, 1));
        cursoActualizado.setFechaFin(LocalDate.of(2024, 7, 30));
        cursoActualizado.setCategoria("Desarrollo");
        cursoActualizado.setPlazasDisponibles(25);
        cursoActualizado.setProfesor(profesor);

        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(cursoRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(curso));
        when(profesorService.obtenerPorId(1L)).thenReturn(profesor);
        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Curso resultado = cursoService.actualizar(1L, cursoActualizado);

        assertEquals("Curso Actualizado", resultado.getNombre());
        assertEquals("Nueva descripción", resultado.getDescripcion());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }

    @Test
    @DisplayName("activar cambia estado a activo")
    void activar() {
        curso.setActivo(false);
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(cursoRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cursoService.activar(1L);

        assertTrue(curso.getActivo());
        verify(cursoRepository, times(1)).save(curso);
    }

    @Test
    @DisplayName("desactivar cambia estado a inactivo")
    void desactivar() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(cursoRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cursoService.desactivar(1L);

        assertFalse(curso.getActivo());
        verify(cursoRepository, times(1)).save(curso);
    }

    @Test
    @DisplayName("eliminar borra curso sin matrículas")
    void eliminar() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(cursoRepository.findByIdAndAcademiaId(1L, 1L)).thenReturn(Optional.of(curso));
        when(matriculaRepository.findByCursoId(1L)).thenReturn(Collections.emptyList());
        doNothing().when(cursoRepository).delete(curso);

        cursoService.eliminar(1L);

        verify(cursoRepository, times(1)).delete(curso);
    }

    @Test
    @DisplayName("contarPorAcademia retorna conteo correcto")
    void contarPorAcademia() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(securityUtils.tieneRol("ADMIN")).thenReturn(false);
        when(cursoRepository.countByAcademiaId(1L)).thenReturn(5L);

        long resultado = cursoService.contarPorAcademia(1L);

        assertEquals(5L, resultado);
        verify(cursoRepository, times(1)).countByAcademiaId(1L);
    }

    @Test
    @DisplayName("contarActivosPorAcademia retorna conteo de cursos activos")
    void contarActivosPorAcademia() {
        when(securityUtils.getAcademiaIdActual()).thenReturn(1L);
        when(securityUtils.tieneRol("ADMIN")).thenReturn(false);
        when(cursoRepository.countByAcademiaIdAndActivo(1L, true)).thenReturn(3L);

        long resultado = cursoService.contarActivosPorAcademia(1L);

        assertEquals(3L, resultado);
        verify(cursoRepository, times(1)).countByAcademiaIdAndActivo(1L, true);
    }
}
