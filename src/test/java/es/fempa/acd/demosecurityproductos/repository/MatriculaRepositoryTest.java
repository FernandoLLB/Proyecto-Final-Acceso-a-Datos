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
 * Tests de integración para MatriculaRepository
 */
@DataJpaTest
@ActiveProfiles("test")
class MatriculaRepositoryTest {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AcademiaRepository academiaRepository;

    private Academia academia;
    private Alumno alumno;
    private Curso curso;
    private Matricula matriculaActiva;
    private Matricula matriculaCompletada;

    @BeforeEach
    void setUp() {
        matriculaRepository.deleteAll();
        alumnoRepository.deleteAll();
        cursoRepository.deleteAll();
        profesorRepository.deleteAll();
        usuarioRepository.deleteAll();
        academiaRepository.deleteAll();

        // Crear academia
        academia = new Academia();
        academia.setNombre("Academia Test");
        academia.setActiva(true);
        academia.setFechaAlta(LocalDateTime.now());
        academia = academiaRepository.save(academia);

        // Crear usuario profesor
        Usuario usuarioProfesor = new Usuario();
        usuarioProfesor.setUsername("profesor1");
        usuarioProfesor.setPassword("password");
        usuarioProfesor.setEmail("profesor@test.com");
        usuarioProfesor.setRol(Rol.PROFESOR);
        usuarioProfesor.setActivo(true);
        usuarioProfesor.setAcademia(academia);
        usuarioProfesor = usuarioRepository.save(usuarioProfesor);

        // Crear profesor
        Profesor profesor = new Profesor();
        profesor.setUsuario(usuarioProfesor);
        profesor.setAcademia(academia);
        profesor.setEspecialidad("Java");
        profesor.setFechaContratacion(LocalDate.now());
        profesor = profesorRepository.save(profesor);

        // Crear usuario alumno
        Usuario usuarioAlumno = new Usuario();
        usuarioAlumno.setUsername("alumno1");
        usuarioAlumno.setPassword("password");
        usuarioAlumno.setEmail("alumno@test.com");
        usuarioAlumno.setRol(Rol.ALUMNO);
        usuarioAlumno.setActivo(true);
        usuarioAlumno.setAcademia(academia);
        usuarioAlumno = usuarioRepository.save(usuarioAlumno);

        // Crear alumno
        alumno = new Alumno();
        alumno.setUsuario(usuarioAlumno);
        alumno.setAcademia(academia);
        alumno.setFechaRegistro(LocalDate.now());
        alumno.setEstadoMatricula("ACTIVO");
        alumno = alumnoRepository.save(alumno);

        // Crear curso
        curso = new Curso();
        curso.setAcademia(academia);
        curso.setNombre("Curso Java");
        curso.setDuracionHoras(40);
        curso.setPrecio(new BigDecimal("199.99"));
        curso.setFechaInicio(LocalDate.now());
        curso.setFechaFin(LocalDate.now().plusMonths(3));
        curso.setProfesor(profesor);
        curso.setPlazasDisponibles(20);
        curso.setActivo(true);
        curso = cursoRepository.save(curso);

        // Crear matrícula activa
        matriculaActiva = new Matricula();
        matriculaActiva.setAcademia(academia);
        matriculaActiva.setAlumno(alumno);
        matriculaActiva.setCurso(curso);
        matriculaActiva.setFechaMatriculacion(LocalDateTime.now());
        matriculaActiva.setEstado(EstadoMatricula.ACTIVA);
        matriculaActiva = matriculaRepository.save(matriculaActiva);

        // Crear segundo curso para matrícula completada
        Curso curso2 = new Curso();
        curso2.setAcademia(academia);
        curso2.setNombre("Curso Python");
        curso2.setDuracionHoras(30);
        curso2.setPrecio(new BigDecimal("149.99"));
        curso2.setFechaInicio(LocalDate.now().minusMonths(6));
        curso2.setFechaFin(LocalDate.now().minusMonths(3));
        curso2.setProfesor(profesor);
        curso2.setPlazasDisponibles(15);
        curso2.setActivo(false);
        curso2 = cursoRepository.save(curso2);

        // Crear matrícula completada
        matriculaCompletada = new Matricula();
        matriculaCompletada.setAcademia(academia);
        matriculaCompletada.setAlumno(alumno);
        matriculaCompletada.setCurso(curso2);
        matriculaCompletada.setFechaMatriculacion(LocalDateTime.now().minusMonths(6));
        matriculaCompletada.setEstado(EstadoMatricula.COMPLETADA);
        matriculaCompletada = matriculaRepository.save(matriculaCompletada);
    }

    @Test
    @DisplayName("findByAcademiaId retorna matrículas de la academia")
    void findByAcademiaId() {
        List<Matricula> matriculas = matriculaRepository.findByAcademiaId(academia.getId());

        assertEquals(2, matriculas.size());
    }

    @Test
    @DisplayName("findByAlumnoId retorna matrículas del alumno")
    void findByAlumnoId() {
        List<Matricula> matriculas = matriculaRepository.findByAlumnoId(alumno.getId());

        assertEquals(2, matriculas.size());
    }

    @Test
    @DisplayName("findByAlumnoIdAndEstado filtra por estado")
    void findByAlumnoIdAndEstado() {
        List<Matricula> activas = matriculaRepository.findByAlumnoIdAndEstado(alumno.getId(), EstadoMatricula.ACTIVA);

        assertEquals(1, activas.size());
        assertEquals(EstadoMatricula.ACTIVA, activas.get(0).getEstado());
    }

    @Test
    @DisplayName("findByCursoId retorna matrículas del curso")
    void findByCursoId() {
        List<Matricula> matriculas = matriculaRepository.findByCursoId(curso.getId());

        assertEquals(1, matriculas.size());
    }

    @Test
    @DisplayName("findByCursoIdAndEstado filtra por estado")
    void findByCursoIdAndEstado() {
        List<Matricula> activas = matriculaRepository.findByCursoIdAndEstado(curso.getId(), EstadoMatricula.ACTIVA);

        assertEquals(1, activas.size());
    }

    @Test
    @DisplayName("findByIdAndAcademiaId retorna matrícula de academia específica")
    void findByIdAndAcademiaId() {
        Optional<Matricula> encontrada = matriculaRepository.findByIdAndAcademiaId(
            matriculaActiva.getId(), academia.getId());

        assertTrue(encontrada.isPresent());
        assertEquals(matriculaActiva.getId(), encontrada.get().getId());
    }

    @Test
    @DisplayName("existeMatriculaActiva verifica matrícula activa existente")
    void existeMatriculaActiva() {
        boolean existe = matriculaRepository.existeMatriculaActiva(alumno.getId(), curso.getId());

        assertTrue(existe);
    }

    @Test
    @DisplayName("existeMatriculaActiva retorna false si no existe")
    void existeMatriculaActivaNoExiste() {
        boolean existe = matriculaRepository.existeMatriculaActiva(999L, 999L);

        assertFalse(existe);
    }

    @Test
    @DisplayName("countByCursoIdAndEstado cuenta por curso y estado")
    void countByCursoIdAndEstado() {
        long count = matriculaRepository.countByCursoIdAndEstado(curso.getId(), EstadoMatricula.ACTIVA);

        assertEquals(1, count);
    }

    @Test
    @DisplayName("countByAlumnoIdAndEstado cuenta por alumno y estado")
    void countByAlumnoIdAndEstado() {
        long activas = matriculaRepository.countByAlumnoIdAndEstado(alumno.getId(), EstadoMatricula.ACTIVA);
        long completadas = matriculaRepository.countByAlumnoIdAndEstado(alumno.getId(), EstadoMatricula.COMPLETADA);

        assertEquals(1, activas);
        assertEquals(1, completadas);
    }

    @Test
    @DisplayName("save persiste matrícula correctamente")
    void save() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("nuevo");
        nuevoUsuario.setPassword("password");
        nuevoUsuario.setEmail("nuevo@test.com");
        nuevoUsuario.setRol(Rol.ALUMNO);
        nuevoUsuario.setAcademia(academia);
        nuevoUsuario = usuarioRepository.save(nuevoUsuario);

        Alumno nuevoAlumno = new Alumno();
        nuevoAlumno.setUsuario(nuevoUsuario);
        nuevoAlumno.setAcademia(academia);
        nuevoAlumno.setFechaRegistro(LocalDate.now());
        nuevoAlumno.setEstadoMatricula("ACTIVO");
        nuevoAlumno = alumnoRepository.save(nuevoAlumno);

        Matricula nueva = new Matricula();
        nueva.setAcademia(academia);
        nueva.setAlumno(nuevoAlumno);
        nueva.setCurso(curso);
        nueva.setFechaMatriculacion(LocalDateTime.now());
        nueva.setEstado(EstadoMatricula.ACTIVA);

        Matricula guardada = matriculaRepository.save(nueva);

        assertNotNull(guardada.getId());
    }

    @Test
    @DisplayName("delete elimina matrícula")
    void delete() {
        long countAntes = matriculaRepository.count();
        matriculaRepository.delete(matriculaCompletada);
        long countDespues = matriculaRepository.count();

        assertEquals(countAntes - 1, countDespues);
    }
}
