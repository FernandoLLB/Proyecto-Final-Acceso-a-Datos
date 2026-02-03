package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Aula
 */
class AulaTest {

    private Aula aula;
    private Academia academia;

    @BeforeEach
    void setUp() {
        aula = new Aula();

        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");
    }

    @Test
    @DisplayName("Constructor por defecto inicializa valores correctamente")
    void constructorPorDefecto() {
        assertNotNull(aula);
        assertTrue(aula.getActiva());
    }

    @Test
    @DisplayName("Setters y getters funcionan correctamente")
    void settersYGetters() {
        Long id = 1L;
        String nombre = "Aula 101";
        Integer capacidad = 30;
        String recursos = "Proyector, Pizarra Digital, WiFi";

        aula.setId(id);
        aula.setAcademia(academia);
        aula.setNombre(nombre);
        aula.setCapacidad(capacidad);
        aula.setActiva(false);
        aula.setRecursos(recursos);

        assertEquals(id, aula.getId());
        assertEquals(academia, aula.getAcademia());
        assertEquals(nombre, aula.getNombre());
        assertEquals(capacidad, aula.getCapacidad());
        assertFalse(aula.getActiva());
        assertEquals(recursos, aula.getRecursos());
    }

    @Test
    @DisplayName("Relación con Academia es correcta")
    void relacionConAcademia() {
        aula.setAcademia(academia);

        assertNotNull(aula.getAcademia());
        assertEquals("Academia Test", aula.getAcademia().getNombre());
    }

    @Test
    @DisplayName("Capacidad positiva")
    void capacidadPositiva() {
        aula.setCapacidad(1);
        assertEquals(1, aula.getCapacidad());

        aula.setCapacidad(100);
        assertEquals(100, aula.getCapacidad());
    }

    @Test
    @DisplayName("Aula puede activarse y desactivarse")
    void activarDesactivar() {
        assertTrue(aula.getActiva());

        aula.setActiva(false);
        assertFalse(aula.getActiva());

        aula.setActiva(true);
        assertTrue(aula.getActiva());
    }

    @Test
    @DisplayName("Recursos pueden ser null")
    void recursosPuedenSerNull() {
        aula.setRecursos(null);
        assertNull(aula.getRecursos());
    }

    @Test
    @DisplayName("Nombre del aula se configura correctamente")
    void nombreAula() {
        String[] nombres = {"Aula 101", "Aula A", "Laboratorio", "Sala de conferencias"};

        for (String nombre : nombres) {
            aula.setNombre(nombre);
            assertEquals(nombre, aula.getNombre());
        }
    }
}
