package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para UsuarioRepository
 */
@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AcademiaRepository academiaRepository;

    private Academia academia;
    private Usuario usuarioAlumno;
    private Usuario usuarioProfesor;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
        academiaRepository.deleteAll();

        academia = new Academia();
        academia.setNombre("Academia Test");
        academia.setActiva(true);
        academia.setFechaAlta(LocalDateTime.now());
        academia = academiaRepository.save(academia);

        usuarioAlumno = new Usuario();
        usuarioAlumno.setUsername("alumno1");
        usuarioAlumno.setPassword("password");
        usuarioAlumno.setEmail("alumno1@test.com");
        usuarioAlumno.setNombre("Juan");
        usuarioAlumno.setApellidos("García");
        usuarioAlumno.setRol(Rol.ALUMNO);
        usuarioAlumno.setActivo(true);
        usuarioAlumno.setAcademia(academia);
        usuarioAlumno = usuarioRepository.save(usuarioAlumno);

        usuarioProfesor = new Usuario();
        usuarioProfesor.setUsername("profesor1");
        usuarioProfesor.setPassword("password");
        usuarioProfesor.setEmail("profesor1@test.com");
        usuarioProfesor.setNombre("María");
        usuarioProfesor.setApellidos("López");
        usuarioProfesor.setRol(Rol.PROFESOR);
        usuarioProfesor.setActivo(true);
        usuarioProfesor.setAcademia(academia);
        usuarioProfesor = usuarioRepository.save(usuarioProfesor);
    }

    @Test
    @DisplayName("findByUsername retorna usuario existente")
    void findByUsername() {
        Optional<Usuario> encontrado = usuarioRepository.findByUsername("alumno1");

        assertTrue(encontrado.isPresent());
        assertEquals("alumno1", encontrado.get().getUsername());
    }

    @Test
    @DisplayName("findByUsername retorna vacío si no existe")
    void findByUsernameNoExiste() {
        Optional<Usuario> encontrado = usuarioRepository.findByUsername("noexiste");

        assertFalse(encontrado.isPresent());
    }

    @Test
    @DisplayName("findByEmail retorna usuario existente")
    void findByEmail() {
        Optional<Usuario> encontrado = usuarioRepository.findByEmail("alumno1@test.com");

        assertTrue(encontrado.isPresent());
        assertEquals("alumno1@test.com", encontrado.get().getEmail());
    }

    @Test
    @DisplayName("existsByUsername verifica existencia correctamente")
    void existsByUsername() {
        assertTrue(usuarioRepository.existsByUsername("alumno1"));
        assertFalse(usuarioRepository.existsByUsername("noexiste"));
    }

    @Test
    @DisplayName("existsByEmail verifica existencia correctamente")
    void existsByEmail() {
        assertTrue(usuarioRepository.existsByEmail("alumno1@test.com"));
        assertFalse(usuarioRepository.existsByEmail("noexiste@test.com"));
    }

    @Test
    @DisplayName("findByAcademiaId retorna usuarios de la academia")
    void findByAcademiaId() {
        List<Usuario> usuarios = usuarioRepository.findByAcademiaId(academia.getId());

        assertEquals(2, usuarios.size());
    }

    @Test
    @DisplayName("findByAcademiaIdAndRol filtra por academia y rol")
    void findByAcademiaIdAndRol() {
        List<Usuario> alumnos = usuarioRepository.findByAcademiaIdAndRol(academia.getId(), Rol.ALUMNO);

        assertEquals(1, alumnos.size());
        assertEquals(Rol.ALUMNO, alumnos.get(0).getRol());
    }

    @Test
    @DisplayName("findByRol retorna usuarios con el rol especificado")
    void findByRol() {
        List<Usuario> profesores = usuarioRepository.findByRol(Rol.PROFESOR);

        assertEquals(1, profesores.size());
        assertEquals(Rol.PROFESOR, profesores.get(0).getRol());
    }

    @Test
    @DisplayName("findByRolAndActivo filtra por rol y estado")
    void findByRolAndActivo() {
        List<Usuario> alumnosActivos = usuarioRepository.findByRolAndActivo(Rol.ALUMNO, true);

        assertEquals(1, alumnosActivos.size());
        assertTrue(alumnosActivos.get(0).getActivo());
    }

    @Test
    @DisplayName("countByAcademiaId cuenta usuarios de academia")
    void countByAcademiaId() {
        long count = usuarioRepository.countByAcademiaId(academia.getId());

        assertEquals(2, count);
    }

    @Test
    @DisplayName("countByAcademiaIdAndRol cuenta usuarios por academia y rol")
    void countByAcademiaIdAndRol() {
        long countAlumnos = usuarioRepository.countByAcademiaIdAndRol(academia.getId(), Rol.ALUMNO);
        long countProfesores = usuarioRepository.countByAcademiaIdAndRol(academia.getId(), Rol.PROFESOR);

        assertEquals(1, countAlumnos);
        assertEquals(1, countProfesores);
    }

    @Test
    @DisplayName("save persiste usuario correctamente")
    void save() {
        Usuario nuevo = new Usuario();
        nuevo.setUsername("nuevo");
        nuevo.setPassword("password");
        nuevo.setEmail("nuevo@test.com");
        nuevo.setRol(Rol.SECRETARIA);
        nuevo.setAcademia(academia);

        Usuario guardado = usuarioRepository.save(nuevo);

        assertNotNull(guardado.getId());
        assertEquals("nuevo", guardado.getUsername());
    }

    @Test
    @DisplayName("findById retorna usuario existente")
    void findById() {
        Optional<Usuario> encontrado = usuarioRepository.findById(usuarioAlumno.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(usuarioAlumno.getId(), encontrado.get().getId());
    }
}
