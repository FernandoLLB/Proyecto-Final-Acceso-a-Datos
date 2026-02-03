package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Profesor
 */
class ProfesorTest {

    private Profesor profesor;
    private Usuario usuario;
    private Academia academia;

    @BeforeEach
    void setUp() {
        profesor = new Profesor();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("profesor1");
        usuario.setEmail("profesor@test.com");
        usuario.setRol(Rol.PROFESOR);

        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");
    }

    @Test
    @DisplayName("Constructor por defecto inicializa valores correctamente")
    void constructorPorDefecto() {
        assertNotNull(profesor);
        assertNotNull(profesor.getFechaContratacion());
        assertEquals(LocalDate.now(), profesor.getFechaContratacion());
    }

    @Test
    @DisplayName("Setters y getters funcionan correctamente")
    void settersYGetters() {
        Long id = 1L;
        String especialidad = "Matemáticas";
        String biografia = "Profesor con 10 años de experiencia";
        LocalDate fechaContratacion = LocalDate.of(2020, 1, 15);

        profesor.setId(id);
        profesor.setUsuario(usuario);
        profesor.setAcademia(academia);
        profesor.setEspecialidad(especialidad);
        profesor.setBiografia(biografia);
        profesor.setFechaContratacion(fechaContratacion);

        assertEquals(id, profesor.getId());
        assertEquals(usuario, profesor.getUsuario());
        assertEquals(academia, profesor.getAcademia());
        assertEquals(especialidad, profesor.getEspecialidad());
        assertEquals(biografia, profesor.getBiografia());
        assertEquals(fechaContratacion, profesor.getFechaContratacion());
    }

    @Test
    @DisplayName("Relación con Usuario es correcta")
    void relacionConUsuario() {
        profesor.setUsuario(usuario);

        assertNotNull(profesor.getUsuario());
        assertEquals("profesor1", profesor.getUsuario().getUsername());
        assertEquals(Rol.PROFESOR, profesor.getUsuario().getRol());
    }

    @Test
    @DisplayName("Relación con Academia es correcta")
    void relacionConAcademia() {
        profesor.setAcademia(academia);

        assertNotNull(profesor.getAcademia());
        assertEquals("Academia Test", profesor.getAcademia().getNombre());
    }

    @Test
    @DisplayName("Campos opcionales pueden ser null")
    void camposOpcionales() {
        profesor.setEspecialidad(null);
        profesor.setBiografia(null);

        assertNull(profesor.getEspecialidad());
        assertNull(profesor.getBiografia());
    }

    @Test
    @DisplayName("Especialidad se configura correctamente")
    void especialidad() {
        String[] especialidades = {"Matemáticas", "Física", "Química", "Inglés", "Programación"};

        for (String esp : especialidades) {
            profesor.setEspecialidad(esp);
            assertEquals(esp, profesor.getEspecialidad());
        }
    }
}
