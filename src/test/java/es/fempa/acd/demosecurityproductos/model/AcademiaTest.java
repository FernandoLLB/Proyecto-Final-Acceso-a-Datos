package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Academia
 */
class AcademiaTest {

    private Academia academia;

    @BeforeEach
    void setUp() {
        academia = new Academia();
    }

    @Test
    @DisplayName("Constructor por defecto inicializa valores correctamente")
    void constructorPorDefecto() {
        assertNotNull(academia);
        assertTrue(academia.getActiva());
        assertNotNull(academia.getFechaAlta());
    }

    @Test
    @DisplayName("Setters y getters funcionan correctamente")
    void settersYGetters() {
        Long id = 1L;
        String nombre = "Academia Test";
        String nifCif = "B12345678";
        String emailContacto = "contacto@academia.com";
        String telefono = "912345678";
        String direccion = "Calle Test 123";
        LocalDateTime fechaAlta = LocalDateTime.of(2024, 1, 1, 10, 0);

        academia.setId(id);
        academia.setNombre(nombre);
        academia.setNifCif(nifCif);
        academia.setEmailContacto(emailContacto);
        academia.setTelefono(telefono);
        academia.setDireccion(direccion);
        academia.setActiva(false);
        academia.setFechaAlta(fechaAlta);

        assertEquals(id, academia.getId());
        assertEquals(nombre, academia.getNombre());
        assertEquals(nifCif, academia.getNifCif());
        assertEquals(emailContacto, academia.getEmailContacto());
        assertEquals(telefono, academia.getTelefono());
        assertEquals(direccion, academia.getDireccion());
        assertFalse(academia.getActiva());
        assertEquals(fechaAlta, academia.getFechaAlta());
    }

    @Test
    @DisplayName("Academia puede activarse y desactivarse")
    void activarDesactivar() {
        assertTrue(academia.getActiva());

        academia.setActiva(false);
        assertFalse(academia.getActiva());

        academia.setActiva(true);
        assertTrue(academia.getActiva());
    }

    @Test
    @DisplayName("Campos opcionales pueden ser null")
    void camposOpcionales() {
        academia.setNifCif(null);
        academia.setEmailContacto(null);
        academia.setTelefono(null);
        academia.setDireccion(null);

        assertNull(academia.getNifCif());
        assertNull(academia.getEmailContacto());
        assertNull(academia.getTelefono());
        assertNull(academia.getDireccion());
    }
}
