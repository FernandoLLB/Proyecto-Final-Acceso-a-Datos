package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para el enum Rol
 */
class RolTest {

    @Test
    @DisplayName("Enum Rol contiene todos los roles esperados")
    void rolesExisten() {
        Rol[] roles = Rol.values();

        assertEquals(5, roles.length);

        assertNotNull(Rol.valueOf("ADMIN"));
        assertNotNull(Rol.valueOf("PROPIETARIO"));
        assertNotNull(Rol.valueOf("SECRETARIA"));
        assertNotNull(Rol.valueOf("PROFESOR"));
        assertNotNull(Rol.valueOf("ALUMNO"));
    }

    @Test
    @DisplayName("Rol ADMIN tiene máximo nivel de acceso")
    void rolAdmin() {
        Rol admin = Rol.ADMIN;
        assertEquals("ADMIN", admin.name());
        assertEquals(0, admin.ordinal());
    }

    @Test
    @DisplayName("Rol PROPIETARIO es propietario de academia")
    void rolPropietario() {
        Rol propietario = Rol.PROPIETARIO;
        assertEquals("PROPIETARIO", propietario.name());
    }

    @Test
    @DisplayName("Rol SECRETARIA gestiona matrículas")
    void rolSecretaria() {
        Rol secretaria = Rol.SECRETARIA;
        assertEquals("SECRETARIA", secretaria.name());
    }

    @Test
    @DisplayName("Rol PROFESOR imparte cursos")
    void rolProfesor() {
        Rol profesor = Rol.PROFESOR;
        assertEquals("PROFESOR", profesor.name());
    }

    @Test
    @DisplayName("Rol ALUMNO es estudiante")
    void rolAlumno() {
        Rol alumno = Rol.ALUMNO;
        assertEquals("ALUMNO", alumno.name());
    }

    @Test
    @DisplayName("valueOf devuelve rol correcto")
    void valueOfCorrecto() {
        assertEquals(Rol.ADMIN, Rol.valueOf("ADMIN"));
        assertEquals(Rol.PROPIETARIO, Rol.valueOf("PROPIETARIO"));
        assertEquals(Rol.SECRETARIA, Rol.valueOf("SECRETARIA"));
        assertEquals(Rol.PROFESOR, Rol.valueOf("PROFESOR"));
        assertEquals(Rol.ALUMNO, Rol.valueOf("ALUMNO"));
    }

    @Test
    @DisplayName("valueOf lanza excepción para rol inexistente")
    void valueOfRolInexistente() {
        assertThrows(IllegalArgumentException.class, () -> {
            Rol.valueOf("INVITADO");
        });
    }
}
