package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Curso
 */
class CursoTest {

    private Curso curso;
    private Academia academia;
    private Profesor profesor;

    @BeforeEach
    void setUp() {
        curso = new Curso();

        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");

        Usuario usuarioProfesor = new Usuario();
        usuarioProfesor.setId(1L);
        usuarioProfesor.setUsername("profesor1");
        usuarioProfesor.setRol(Rol.PROFESOR);

        profesor = new Profesor();
        profesor.setId(1L);
        profesor.setUsuario(usuarioProfesor);
        profesor.setAcademia(academia);
        profesor.setEspecialidad("Matemáticas");
    }

    @Test
    @DisplayName("Constructor por defecto inicializa valores correctamente")
    void constructorPorDefecto() {
        assertNotNull(curso);
        assertTrue(curso.getActivo());
    }

    @Test
    @DisplayName("Setters y getters funcionan correctamente")
    void settersYGetters() {
        Long id = 1L;
        String nombre = "Curso de Java";
        String descripcion = "Aprende Java desde cero";
        Integer duracionHoras = 40;
        BigDecimal precio = new BigDecimal("199.99");
        LocalDate fechaInicio = LocalDate.of(2024, 3, 1);
        LocalDate fechaFin = LocalDate.of(2024, 6, 30);
        String categoria = "Programación";
        Integer plazasDisponibles = 20;

        curso.setId(id);
        curso.setAcademia(academia);
        curso.setNombre(nombre);
        curso.setDescripcion(descripcion);
        curso.setDuracionHoras(duracionHoras);
        curso.setPrecio(precio);
        curso.setFechaInicio(fechaInicio);
        curso.setFechaFin(fechaFin);
        curso.setCategoria(categoria);
        curso.setProfesor(profesor);
        curso.setPlazasDisponibles(plazasDisponibles);
        curso.setActivo(false);

        assertEquals(id, curso.getId());
        assertEquals(academia, curso.getAcademia());
        assertEquals(nombre, curso.getNombre());
        assertEquals(descripcion, curso.getDescripcion());
        assertEquals(duracionHoras, curso.getDuracionHoras());
        assertEquals(precio, curso.getPrecio());
        assertEquals(fechaInicio, curso.getFechaInicio());
        assertEquals(fechaFin, curso.getFechaFin());
        assertEquals(categoria, curso.getCategoria());
        assertEquals(profesor, curso.getProfesor());
        assertEquals(plazasDisponibles, curso.getPlazasDisponibles());
        assertFalse(curso.getActivo());
    }

    @Test
    @DisplayName("Relación con Academia es correcta")
    void relacionConAcademia() {
        curso.setAcademia(academia);

        assertNotNull(curso.getAcademia());
        assertEquals("Academia Test", curso.getAcademia().getNombre());
    }

    @Test
    @DisplayName("Relación con Profesor es correcta")
    void relacionConProfesor() {
        curso.setProfesor(profesor);

        assertNotNull(curso.getProfesor());
        assertEquals("Matemáticas", curso.getProfesor().getEspecialidad());
    }

    @Test
    @DisplayName("Precio puede ser cero o positivo")
    void precioCeroOPositivo() {
        curso.setPrecio(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, curso.getPrecio());

        curso.setPrecio(new BigDecimal("100.50"));
        assertEquals(new BigDecimal("100.50"), curso.getPrecio());
    }

    @Test
    @DisplayName("Campos opcionales pueden ser null")
    void camposOpcionales() {
        curso.setDescripcion(null);
        curso.setPrecio(null);
        curso.setCategoria(null);
        curso.setPlazasDisponibles(null);

        assertNull(curso.getDescripcion());
        assertNull(curso.getPrecio());
        assertNull(curso.getCategoria());
        assertNull(curso.getPlazasDisponibles());
    }

    @Test
    @DisplayName("Fechas de inicio y fin se configuran correctamente")
    void fechasDeInicioYFin() {
        LocalDate fechaInicio = LocalDate.of(2024, 1, 15);
        LocalDate fechaFin = LocalDate.of(2024, 3, 15);

        curso.setFechaInicio(fechaInicio);
        curso.setFechaFin(fechaFin);

        assertEquals(fechaInicio, curso.getFechaInicio());
        assertEquals(fechaFin, curso.getFechaFin());
        assertTrue(curso.getFechaInicio().isBefore(curso.getFechaFin()));
    }
}
