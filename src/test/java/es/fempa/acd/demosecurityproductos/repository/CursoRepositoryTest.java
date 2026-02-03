package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para CursoRepository
 */
@DataJpaTest
@ActiveProfiles("test")
class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AcademiaRepository academiaRepository;

    private Academia academia;
    private Profesor profesor;
    private Curso cursoActivo;
    private Curso cursoInactivo;

    @BeforeEach
    void setUp() {
        cursoRepository.deleteAll();
        profesorRepository.deleteAll();
        usuarioRepository.deleteAll();
        academiaRepository.deleteAll();

        academia = new Academia();
        academia.setNombre("Academia Test");
        academia.setActiva(true);
        academia.setFechaAlta(LocalDateTime.now());
        academia = academiaRepository.save(academia);

        Usuario usuarioProfesor = new Usuario();
        usuarioProfesor.setUsername("profesor1");
        usuarioProfesor.setPassword("password");
        usuarioProfesor.setEmail("profesor@test.com");
        usuarioProfesor.setRol(Rol.PROFESOR);
        usuarioProfesor.setActivo(true);
        usuarioProfesor.setAcademia(academia);
        usuarioProfesor = usuarioRepository.save(usuarioProfesor);

        profesor = new Profesor();
        profesor.setUsuario(usuarioProfesor);
        profesor.setAcademia(academia);
        profesor.setEspecialidad("Java");
        profesor.setFechaContratacion(LocalDate.now());
        profesor = profesorRepository.save(profesor);

        cursoActivo = new Curso();
        cursoActivo.setAcademia(academia);
        cursoActivo.setNombre("Curso Java");
        cursoActivo.setDescripcion("Aprende Java");
        cursoActivo.setDuracionHoras(40);
        cursoActivo.setPrecio(new BigDecimal("199.99"));
        cursoActivo.setFechaInicio(LocalDate.of(2024, 3, 1));
        cursoActivo.setFechaFin(LocalDate.of(2024, 6, 30));
        cursoActivo.setCategoria("Programación");
        cursoActivo.setProfesor(profesor);
        cursoActivo.setPlazasDisponibles(20);
        cursoActivo.setActivo(true);
        cursoActivo = cursoRepository.save(cursoActivo);

        cursoInactivo = new Curso();
        cursoInactivo.setAcademia(academia);
        cursoInactivo.setNombre("Curso Python");
        cursoInactivo.setDescripcion("Aprende Python");
        cursoInactivo.setDuracionHoras(30);
        cursoInactivo.setPrecio(new BigDecimal("149.99"));
        cursoInactivo.setFechaInicio(LocalDate.of(2024, 1, 1));
        cursoInactivo.setFechaFin(LocalDate.of(2024, 2, 28));
        cursoInactivo.setCategoria("Programación");
        cursoInactivo.setProfesor(profesor);
        cursoInactivo.setPlazasDisponibles(15);
        cursoInactivo.setActivo(false);
        cursoInactivo = cursoRepository.save(cursoInactivo);
    }

    @Test
    @DisplayName("findByAcademiaId retorna cursos de la academia")
    void findByAcademiaId() {
        List<Curso> cursos = cursoRepository.findByAcademiaId(academia.getId());

        assertEquals(2, cursos.size());
    }

    @Test
    @DisplayName("findByAcademiaIdAndActivo filtra cursos por estado")
    void findByAcademiaIdAndActivo() {
        List<Curso> activos = cursoRepository.findByAcademiaIdAndActivo(academia.getId(), true);

        assertEquals(1, activos.size());
        assertTrue(activos.get(0).getActivo());
    }

    @Test
    @DisplayName("findByProfesorId retorna cursos del profesor")
    void findByProfesorId() {
        List<Curso> cursos = cursoRepository.findByProfesorId(profesor.getId());

        assertEquals(2, cursos.size());
    }

    @Test
    @DisplayName("findByProfesorIdAndActivo filtra cursos activos del profesor")
    void findByProfesorIdAndActivo() {
        List<Curso> activos = cursoRepository.findByProfesorIdAndActivo(profesor.getId(), true);

        assertEquals(1, activos.size());
    }

    @Test
    @DisplayName("findByIdAndAcademiaId retorna curso de academia específica")
    void findByIdAndAcademiaId() {
        Optional<Curso> encontrado = cursoRepository.findByIdAndAcademiaId(cursoActivo.getId(), academia.getId());

        assertTrue(encontrado.isPresent());
        assertEquals(cursoActivo.getId(), encontrado.get().getId());
    }

    @Test
    @DisplayName("findByIdAndAcademiaId retorna vacío si no coincide academia")
    void findByIdAndAcademiaIdNoCoincide() {
        Optional<Curso> encontrado = cursoRepository.findByIdAndAcademiaId(cursoActivo.getId(), 999L);

        assertFalse(encontrado.isPresent());
    }

    @Test
    @DisplayName("countByAcademiaId cuenta cursos de academia")
    void countByAcademiaId() {
        long count = cursoRepository.countByAcademiaId(academia.getId());

        assertEquals(2, count);
    }

    @Test
    @DisplayName("countByAcademiaIdAndActivo cuenta cursos activos")
    void countByAcademiaIdAndActivo() {
        long activos = cursoRepository.countByAcademiaIdAndActivo(academia.getId(), true);
        long inactivos = cursoRepository.countByAcademiaIdAndActivo(academia.getId(), false);

        assertEquals(1, activos);
        assertEquals(1, inactivos);
    }

    @Test
    @DisplayName("countByProfesorId cuenta cursos del profesor")
    void countByProfesorId() {
        long count = cursoRepository.countByProfesorId(profesor.getId());

        assertEquals(2, count);
    }

    @Test
    @DisplayName("findByAcademiaIdAndFechaRange filtra por rango de fechas")
    void findByAcademiaIdAndFechaRange() {
        List<Curso> cursos = cursoRepository.findByAcademiaIdAndFechaRange(
            academia.getId(),
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 3, 31)
        );

        assertEquals(1, cursos.size()); // Solo el curso inactivo está en ese rango
    }

    @Test
    @DisplayName("findByAcademiaIdAndCategoria filtra por categoría")
    void findByAcademiaIdAndCategoria() {
        List<Curso> cursos = cursoRepository.findByAcademiaIdAndCategoria(academia.getId(), "Programación");

        assertEquals(1, cursos.size()); // Solo cursos activos de la categoría
    }

    @Test
    @DisplayName("save persiste curso correctamente")
    void save() {
        Curso nuevo = new Curso();
        nuevo.setAcademia(academia);
        nuevo.setNombre("Nuevo Curso");
        nuevo.setDuracionHoras(20);
        nuevo.setFechaInicio(LocalDate.now());
        nuevo.setFechaFin(LocalDate.now().plusMonths(1));
        nuevo.setProfesor(profesor);
        nuevo.setActivo(true);

        Curso guardado = cursoRepository.save(nuevo);

        assertNotNull(guardado.getId());
    }

    @Test
    @DisplayName("delete elimina curso")
    void delete() {
        long countAntes = cursoRepository.count();
        cursoRepository.delete(cursoInactivo);
        long countDespues = cursoRepository.count();

        assertEquals(countAntes - 1, countDespues);
    }
}
