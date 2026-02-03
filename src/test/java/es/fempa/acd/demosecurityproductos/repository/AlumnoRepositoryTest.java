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
 * Tests de integración para AlumnoRepository
 */
@DataJpaTest
@ActiveProfiles("test")
class AlumnoRepositoryTest {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AcademiaRepository academiaRepository;

    private Academia academia;
    private Usuario usuario1;
    private Usuario usuario2;
    private Alumno alumnoActivo;
    private Alumno alumnoInactivo;

    @BeforeEach
    void setUp() {
        alumnoRepository.deleteAll();
        usuarioRepository.deleteAll();
        academiaRepository.deleteAll();

        academia = new Academia();
        academia.setNombre("Academia Test");
        academia.setActiva(true);
        academia.setFechaAlta(LocalDateTime.now());
        academia = academiaRepository.save(academia);

        usuario1 = new Usuario();
        usuario1.setUsername("alumno1");
        usuario1.setPassword("password");
        usuario1.setEmail("alumno1@test.com");
        usuario1.setRol(Rol.ALUMNO);
        usuario1.setActivo(true);
        usuario1.setAcademia(academia);
        usuario1 = usuarioRepository.save(usuario1);

        usuario2 = new Usuario();
        usuario2.setUsername("alumno2");
        usuario2.setPassword("password");
        usuario2.setEmail("alumno2@test.com");
        usuario2.setRol(Rol.ALUMNO);
        usuario2.setActivo(false);
        usuario2.setAcademia(academia);
        usuario2 = usuarioRepository.save(usuario2);

        alumnoActivo = new Alumno();
        alumnoActivo.setUsuario(usuario1);
        alumnoActivo.setAcademia(academia);
        alumnoActivo.setFechaRegistro(LocalDate.now());
        alumnoActivo.setEstadoMatricula("ACTIVO");
        alumnoActivo = alumnoRepository.save(alumnoActivo);

        alumnoInactivo = new Alumno();
        alumnoInactivo.setUsuario(usuario2);
        alumnoInactivo.setAcademia(academia);
        alumnoInactivo.setFechaRegistro(LocalDate.now().minusDays(10));
        alumnoInactivo.setEstadoMatricula("INACTIVO");
        alumnoInactivo = alumnoRepository.save(alumnoInactivo);
    }

    @Test
    @DisplayName("findByAcademiaId retorna alumnos de la academia")
    void findByAcademiaId() {
        List<Alumno> alumnos = alumnoRepository.findByAcademiaId(academia.getId());

        assertEquals(2, alumnos.size());
    }

    @Test
    @DisplayName("findByAcademiaIdAndEstadoMatricula filtra por estado")
    void findByAcademiaIdAndEstadoMatricula() {
        List<Alumno> activos = alumnoRepository.findByAcademiaIdAndEstadoMatricula(academia.getId(), "ACTIVO");

        assertEquals(1, activos.size());
        assertEquals("ACTIVO", activos.get(0).getEstadoMatricula());
    }

    @Test
    @DisplayName("countByAcademiaId cuenta alumnos de academia")
    void countByAcademiaId() {
        long count = alumnoRepository.countByAcademiaId(academia.getId());

        assertEquals(2, count);
    }

    @Test
    @DisplayName("countByAcademiaIdAndEstadoMatricula cuenta por estado")
    void countByAcademiaIdAndEstadoMatricula() {
        long activos = alumnoRepository.countByAcademiaIdAndEstadoMatricula(academia.getId(), "ACTIVO");
        long inactivos = alumnoRepository.countByAcademiaIdAndEstadoMatricula(academia.getId(), "INACTIVO");

        assertEquals(1, activos);
        assertEquals(1, inactivos);
    }

    @Test
    @DisplayName("findByUsuarioId retorna alumno por ID de usuario")
    void findByUsuarioId() {
        Optional<Alumno> encontrado = alumnoRepository.findByUsuarioId(usuario1.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(usuario1.getId(), encontrado.get().getUsuario().getId());
    }

    @Test
    @DisplayName("findByUsuario retorna alumno por entidad usuario")
    void findByUsuario() {
        Optional<Alumno> encontrado = alumnoRepository.findByUsuario(usuario1);

        assertTrue(encontrado.isPresent());
        assertEquals(usuario1, encontrado.get().getUsuario());
    }

    @Test
    @DisplayName("findTop5ByAcademiaIdOrderByFechaRegistroDesc retorna últimos registrados")
    void findTop5ByAcademiaIdOrderByFechaRegistroDesc() {
        List<Alumno> ultimos = alumnoRepository.findTop5ByAcademiaIdOrderByFechaRegistroDesc(academia.getId());

        assertFalse(ultimos.isEmpty());
        // El primero debe ser el más reciente
        assertTrue(ultimos.get(0).getFechaRegistro().isAfter(ultimos.get(1).getFechaRegistro()) ||
                   ultimos.get(0).getFechaRegistro().isEqual(ultimos.get(1).getFechaRegistro()));
    }

    @Test
    @DisplayName("save persiste alumno correctamente")
    void save() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("nuevo");
        nuevoUsuario.setPassword("password");
        nuevoUsuario.setEmail("nuevo@test.com");
        nuevoUsuario.setRol(Rol.ALUMNO);
        nuevoUsuario.setAcademia(academia);
        nuevoUsuario = usuarioRepository.save(nuevoUsuario);

        Alumno nuevo = new Alumno();
        nuevo.setUsuario(nuevoUsuario);
        nuevo.setAcademia(academia);
        nuevo.setFechaRegistro(LocalDate.now());
        nuevo.setEstadoMatricula("ACTIVO");

        Alumno guardado = alumnoRepository.save(nuevo);

        assertNotNull(guardado.getId());
    }

    @Test
    @DisplayName("findById retorna alumno existente")
    void findById() {
        Optional<Alumno> encontrado = alumnoRepository.findById(alumnoActivo.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(alumnoActivo.getId(), encontrado.get().getId());
    }
}
