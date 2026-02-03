package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad Usuario
 */
class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    @DisplayName("Constructor por defecto inicializa valores correctamente")
    void constructorPorDefecto() {
        assertNotNull(usuario);
        assertTrue(usuario.getActivo());
        assertFalse(usuario.getEmailVerificado());
    }

    @Test
    @DisplayName("Setters y getters funcionan correctamente")
    void settersYGetters() {
        Long id = 1L;
        String username = "testuser";
        String password = "password123";
        String email = "test@test.com";
        String nombre = "Juan";
        String apellidos = "García López";
        Rol rol = Rol.ALUMNO;
        Academia academia = new Academia();
        academia.setId(1L);

        usuario.setId(id);
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setEmail(email);
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setRol(rol);
        usuario.setActivo(false);
        usuario.setEmailVerificado(true);
        usuario.setAcademia(academia);

        assertEquals(id, usuario.getId());
        assertEquals(username, usuario.getUsername());
        assertEquals(password, usuario.getPassword());
        assertEquals(email, usuario.getEmail());
        assertEquals(nombre, usuario.getNombre());
        assertEquals(apellidos, usuario.getApellidos());
        assertEquals(rol, usuario.getRol());
        assertFalse(usuario.getActivo());
        assertTrue(usuario.getEmailVerificado());
        assertNotNull(usuario.getAcademia());
        assertEquals(1L, usuario.getAcademia().getId());
    }

    @Test
    @DisplayName("Usuario puede tener todos los roles del sistema")
    void todosLosRoles() {
        for (Rol rol : Rol.values()) {
            usuario.setRol(rol);
            assertEquals(rol, usuario.getRol());
        }
    }

    @Test
    @DisplayName("Academia puede ser null para ADMIN")
    void academiaPuedeSerNull() {
        usuario.setRol(Rol.ADMIN);
        usuario.setAcademia(null);
        assertNull(usuario.getAcademia());
    }
}
