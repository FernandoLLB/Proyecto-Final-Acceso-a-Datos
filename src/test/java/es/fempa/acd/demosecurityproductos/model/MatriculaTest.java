package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Matricula
 */
class MatriculaTest {

    private Matricula matricula;
    private Academia academia;
    private Alumno alumno;
    private Curso curso;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        matricula = new Matricula();

        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");

        Usuario usuarioAlumno = new Usuario();
        usuarioAlumno.setId(1L);
        usuarioAlumno.setUsername("alumno1");
        usuarioAlumno.setRol(Rol.ALUMNO);

        alumno = new Alumno();
        alumno.setId(1L);
        alumno.setUsuario(usuarioAlumno);
        alumno.setAcademia(academia);

        curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso Test");
        curso.setAcademia(academia);

        usuario = new Usuario();
        usuario.setId(2L);
        usuario.setUsername("secretaria1");
        usuario.setRol(Rol.SECRETARIA);
    }

    @Test
    @DisplayName("Constructor por defecto inicializa valores correctamente")
    void constructorPorDefecto() {
        assertNotNull(matricula);
        assertEquals(EstadoMatricula.ACTIVA, matricula.getEstado());
        assertNotNull(matricula.getFechaMatriculacion());
    }

    @Test
    @DisplayName("Setters y getters funcionan correctamente")
    void settersYGetters() {
        Long id = 1L;
        LocalDateTime fechaMatriculacion = LocalDateTime.of(2024, 3, 1, 10, 0);
        String observaciones = "Observaciones de prueba";

        matricula.setId(id);
        matricula.setAcademia(academia);
        matricula.setAlumno(alumno);
        matricula.setCurso(curso);
        matricula.setFechaMatriculacion(fechaMatriculacion);
        matricula.setEstado(EstadoMatricula.COMPLETADA);
        matricula.setObservaciones(observaciones);
        matricula.setMatriculadoPor(usuario);

        assertEquals(id, matricula.getId());
        assertEquals(academia, matricula.getAcademia());
        assertEquals(alumno, matricula.getAlumno());
        assertEquals(curso, matricula.getCurso());
        assertEquals(fechaMatriculacion, matricula.getFechaMatriculacion());
        assertEquals(EstadoMatricula.COMPLETADA, matricula.getEstado());
        assertEquals(observaciones, matricula.getObservaciones());
        assertEquals(usuario, matricula.getMatriculadoPor());
    }

    @Test
    @DisplayName("Todos los estados de matrícula son válidos")
    void todosLosEstadosDeMatricula() {
        for (EstadoMatricula estado : EstadoMatricula.values()) {
            matricula.setEstado(estado);
            assertEquals(estado, matricula.getEstado());
        }
    }

    @Test
    @DisplayName("Relación con Academia es correcta")
    void relacionConAcademia() {
        matricula.setAcademia(academia);

        assertNotNull(matricula.getAcademia());
        assertEquals("Academia Test", matricula.getAcademia().getNombre());
    }

    @Test
    @DisplayName("Relación con Alumno es correcta")
    void relacionConAlumno() {
        matricula.setAlumno(alumno);

        assertNotNull(matricula.getAlumno());
        assertEquals("alumno1", matricula.getAlumno().getUsuario().getUsername());
    }

    @Test
    @DisplayName("Relación con Curso es correcta")
    void relacionConCurso() {
        matricula.setCurso(curso);

        assertNotNull(matricula.getCurso());
        assertEquals("Curso Test", matricula.getCurso().getNombre());
    }

    @Test
    @DisplayName("Observaciones pueden ser null")
    void observacionesPuedenSerNull() {
        matricula.setObservaciones(null);
        assertNull(matricula.getObservaciones());
    }

    @Test
    @DisplayName("MatriculadoPor puede ser null")
    void matriculadoPorPuedeSerNull() {
        matricula.setMatriculadoPor(null);
        assertNull(matricula.getMatriculadoPor());
    }
}
