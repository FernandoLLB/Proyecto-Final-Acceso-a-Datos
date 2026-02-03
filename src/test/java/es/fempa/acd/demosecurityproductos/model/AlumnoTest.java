package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Alumno
 */
class AlumnoTest {

    private Alumno alumno;
    private Usuario usuario;
    private Academia academia;

    @BeforeEach
    void setUp() {
        alumno = new Alumno();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("alumno1");
        usuario.setEmail("alumno@test.com");
        usuario.setRol(Rol.ALUMNO);

        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");
    }

    @Test
    @DisplayName("Constructor por defecto inicializa valores correctamente")
    void constructorPorDefecto() {
        assertNotNull(alumno);
        assertEquals("ACTIVO", alumno.getEstadoMatricula());
        assertNotNull(alumno.getFechaRegistro());
        assertEquals(LocalDate.now(), alumno.getFechaRegistro());
    }

    @Test
    @DisplayName("Setters y getters funcionan correctamente")
    void settersYGetters() {
        Long id = 1L;
        LocalDate fechaRegistro = LocalDate.of(2024, 6, 15);
        String estadoMatricula = "INACTIVO";
        String observaciones = "Observaciones de prueba";

        alumno.setId(id);
        alumno.setUsuario(usuario);
        alumno.setAcademia(academia);
        alumno.setFechaRegistro(fechaRegistro);
        alumno.setEstadoMatricula(estadoMatricula);
        alumno.setObservaciones(observaciones);

        assertEquals(id, alumno.getId());
        assertEquals(usuario, alumno.getUsuario());
        assertEquals(academia, alumno.getAcademia());
        assertEquals(fechaRegistro, alumno.getFechaRegistro());
        assertEquals(estadoMatricula, alumno.getEstadoMatricula());
        assertEquals(observaciones, alumno.getObservaciones());
    }

    @Test
    @DisplayName("Estados de matrícula válidos")
    void estadosDeMatricula() {
        String[] estadosValidos = {"ACTIVO", "INACTIVO", "COMPLETADO", "SUSPENDIDO"};

        for (String estado : estadosValidos) {
            alumno.setEstadoMatricula(estado);
            assertEquals(estado, alumno.getEstadoMatricula());
        }
    }

    @Test
    @DisplayName("Relación con Usuario es correcta")
    void relacionConUsuario() {
        alumno.setUsuario(usuario);

        assertNotNull(alumno.getUsuario());
        assertEquals("alumno1", alumno.getUsuario().getUsername());
        assertEquals(Rol.ALUMNO, alumno.getUsuario().getRol());
    }

    @Test
    @DisplayName("Relación con Academia es correcta")
    void relacionConAcademia() {
        alumno.setAcademia(academia);

        assertNotNull(alumno.getAcademia());
        assertEquals("Academia Test", alumno.getAcademia().getNombre());
    }

    @Test
    @DisplayName("Observaciones pueden ser null")
    void observacionesPuedenSerNull() {
        alumno.setObservaciones(null);
        assertNull(alumno.getObservaciones());
    }
}
