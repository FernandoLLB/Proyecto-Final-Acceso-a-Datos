package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para ProfesorRepository
 */
@DataJpaTest
@ActiveProfiles("test")
class ProfesorRepositoryTest {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AcademiaRepository academiaRepository;

    private Academia academia;
    private Usuario usuarioProfesor1;
    private Usuario usuarioProfesor2;
    private Profesor profesor1;
    private Profesor profesor2;

    @BeforeEach
    void setUp() {
        profesorRepository.deleteAll();
        usuarioRepository.deleteAll();
        academiaRepository.deleteAll();

        academia = new Academia();
        academia.setNombre("Academia Test");
        academia.setActiva(true);
        academia.setFechaAlta(LocalDateTime.now());
        academia = academiaRepository.save(academia);

        usuarioProfesor1 = new Usuario();
        usuarioProfesor1.setUsername("profesor1");
        usuarioProfesor1.setPassword("password");
        usuarioProfesor1.setEmail("profesor1@test.com");
        usuarioProfesor1.setRol(Rol.PROFESOR);
        usuarioProfesor1.setActivo(true);
        usuarioProfesor1.setAcademia(academia);
        usuarioProfesor1 = usuarioRepository.save(usuarioProfesor1);

        usuarioProfesor2 = new Usuario();
        usuarioProfesor2.setUsername("profesor2");
        usuarioProfesor2.setPassword("password");
        usuarioProfesor2.setEmail("profesor2@test.com");
        usuarioProfesor2.setRol(Rol.PROFESOR);
        usuarioProfesor2.setActivo(false);
        usuarioProfesor2.setAcademia(academia);
        usuarioProfesor2 = usuarioRepository.save(usuarioProfesor2);

        profesor1 = new Profesor();
        profesor1.setUsuario(usuarioProfesor1);
        profesor1.setAcademia(academia);
        profesor1.setEspecialidad("Java");
        profesor1.setBiografia("Experto en Java");
        profesor1.setFechaContratacion(LocalDate.now());
        profesor1 = profesorRepository.save(profesor1);

        profesor2 = new Profesor();
        profesor2.setUsuario(usuarioProfesor2);
        profesor2.setAcademia(academia);
        profesor2.setEspecialidad("Python");
        profesor2.setBiografia("Experto en Python");
        profesor2.setFechaContratacion(LocalDate.now().minusMonths(6));
        profesor2 = profesorRepository.save(profesor2);
    }

    @Test
    @DisplayName("findByAcademiaId retorna profesores de la academia")
    void findByAcademiaId() {
        List<Profesor> profesores = profesorRepository.findByAcademiaId(academia.getId());

        assertEquals(2, profesores.size());
    }

    @Test
    @DisplayName("findByAcademiaIdAndUsuarioActivo filtra por estado de usuario")
    void findByAcademiaIdAndUsuarioActivo() {
        List<Profesor> activos = profesorRepository.findByAcademiaIdAndUsuarioActivo(academia.getId(), true);
        List<Profesor> inactivos = profesorRepository.findByAcademiaIdAndUsuarioActivo(academia.getId(), false);

        assertEquals(1, activos.size());
        assertEquals(1, inactivos.size());
        assertTrue(activos.get(0).getUsuario().getActivo());
        assertFalse(inactivos.get(0).getUsuario().getActivo());
    }

    @Test
    @DisplayName("countByAcademiaId cuenta profesores de academia")
    void countByAcademiaId() {
        long count = profesorRepository.countByAcademiaId(academia.getId());

        assertEquals(2, count);
    }

    @Test
    @DisplayName("findByUsuarioId retorna profesor por ID de usuario")
    void findByUsuarioId() {
        Optional<Profesor> encontrado = profesorRepository.findByUsuarioId(usuarioProfesor1.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(usuarioProfesor1.getId(), encontrado.get().getUsuario().getId());
    }

    @Test
    @DisplayName("findByUsuario retorna profesor por entidad usuario")
    void findByUsuario() {
        Optional<Profesor> encontrado = profesorRepository.findByUsuario(usuarioProfesor1);

        assertTrue(encontrado.isPresent());
        assertEquals(usuarioProfesor1, encontrado.get().getUsuario());
    }

    @Test
    @DisplayName("findByUsuarioId retorna vacío si no existe")
    void findByUsuarioIdNoExiste() {
        Optional<Profesor> encontrado = profesorRepository.findByUsuarioId(999L);

        assertFalse(encontrado.isPresent());
    }

    @Test
    @DisplayName("save persiste profesor correctamente")
    void save() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("nuevo");
        nuevoUsuario.setPassword("password");
        nuevoUsuario.setEmail("nuevo@test.com");
        nuevoUsuario.setRol(Rol.PROFESOR);
        nuevoUsuario.setAcademia(academia);
        nuevoUsuario = usuarioRepository.save(nuevoUsuario);

        Profesor nuevo = new Profesor();
        nuevo.setUsuario(nuevoUsuario);
        nuevo.setAcademia(academia);
        nuevo.setEspecialidad("JavaScript");
        nuevo.setFechaContratacion(LocalDate.now());

        Profesor guardado = profesorRepository.save(nuevo);

        assertNotNull(guardado.getId());
        assertEquals("JavaScript", guardado.getEspecialidad());
    }

    @Test
    @DisplayName("findById retorna profesor existente")
    void findById() {
        Optional<Profesor> encontrado = profesorRepository.findById(profesor1.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(profesor1.getId(), encontrado.get().getId());
    }

    @Test
    @DisplayName("delete elimina profesor")
    void delete() {
        long countAntes = profesorRepository.count();
        profesorRepository.delete(profesor2);
        long countDespues = profesorRepository.count();

        assertEquals(countAntes - 1, countDespues);
    }
}
