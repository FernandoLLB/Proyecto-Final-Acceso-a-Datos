package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.*;
import es.fempa.acd.demosecurityproductos.repository.MatriculaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final CursoService cursoService;
    private final AlumnoService alumnoService;
    private final SecurityUtils securityUtils;

    public MatriculaService(MatriculaRepository matriculaRepository,
                           CursoService cursoService,
                           AlumnoService alumnoService,
                           SecurityUtils securityUtils) {
        this.matriculaRepository = matriculaRepository;
        this.cursoService = cursoService;
        this.alumnoService = alumnoService;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public Matricula matricular(Long alumnoId, Long cursoId, String observaciones) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        Usuario usuario = securityUtils.getUsuarioAutenticado();

        if (academiaId == null || usuario == null) {
            throw new IllegalStateException("Usuario sin academia asignada");
        }

        // Obtener alumno y curso (ya valida tenant scope)
        Alumno alumno = alumnoService.obtenerPorId(alumnoId);
        Curso curso = cursoService.obtenerPorId(cursoId);

        // Validar que el alumno esté activo
        if (!"ACTIVO".equals(alumno.getEstadoMatricula())) {
            throw new IllegalArgumentException("El alumno no está activo");
        }

        // Validar que el curso esté activo
        if (!curso.getActivo()) {
            throw new IllegalArgumentException("El curso no está activo");
        }

        // Validar que no exista matrícula activa previa
        if (matriculaRepository.existeMatriculaActiva(alumnoId, cursoId)) {
            throw new IllegalStateException("El alumno ya está matriculado en este curso");
        }

        // Validar plazas disponibles
        if (curso.getPlazasDisponibles() != null) {
            long matriculasActivas = matriculaRepository.countByCursoIdAndEstado(
                cursoId, EstadoMatricula.ACTIVA);
            if (matriculasActivas >= curso.getPlazasDisponibles()) {
                throw new IllegalStateException("No hay plazas disponibles en el curso");
            }
        }

        // Crear matrícula
        Matricula matricula = new Matricula();
        matricula.setAcademia(usuario.getAcademia());
        matricula.setAlumno(alumno);
        matricula.setCurso(curso);
        matricula.setFechaMatriculacion(LocalDateTime.now());
        matricula.setEstado(EstadoMatricula.ACTIVA);
        matricula.setObservaciones(observaciones);
        matricula.setMatriculadoPor(usuario);

        return matriculaRepository.save(matricula);
    }

    @Transactional(readOnly = true)
    public List<Matricula> listarPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return matriculaRepository.findByAcademiaId(academiaId);
    }

    @Transactional(readOnly = true)
    public List<Matricula> listarPorAlumno(Long alumnoId) {
        // Validar que el alumno pertenece a mi academia
        Alumno alumno = alumnoService.obtenerPorId(alumnoId);
        return matriculaRepository.findByAlumnoId(alumnoId);
    }

    @Transactional(readOnly = true)
    public List<Matricula> listarActivasPorAlumno(Long alumnoId) {
        alumnoService.obtenerPorId(alumnoId); // Valida acceso
        return matriculaRepository.findByAlumnoIdAndEstado(alumnoId, EstadoMatricula.ACTIVA);
    }

    @Transactional(readOnly = true)
    public List<Matricula> listarPorCurso(Long cursoId) {
        cursoService.obtenerPorId(cursoId); // Valida acceso
        return matriculaRepository.findByCursoId(cursoId);
    }

    @Transactional(readOnly = true)
    public List<Matricula> listarActivasPorCurso(Long cursoId) {
        cursoService.obtenerPorId(cursoId); // Valida acceso
        return matriculaRepository.findByCursoIdAndEstado(cursoId, EstadoMatricula.ACTIVA);
    }

    @Transactional(readOnly = true)
    public Matricula obtenerPorId(Long id) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            throw new IllegalStateException("Usuario sin academia asignada");
        }

        return matriculaRepository.findByIdAndAcademiaId(id, academiaId)
            .orElseThrow(() -> new IllegalArgumentException("Matrícula no encontrada o no pertenece a su academia"));
    }

    @Transactional
    public void completar(Long id) {
        Matricula matricula = obtenerPorId(id);

        if (matricula.getEstado() != EstadoMatricula.ACTIVA) {
            throw new IllegalStateException("Solo se pueden completar matrículas activas");
        }

        matricula.setEstado(EstadoMatricula.COMPLETADA);
        matriculaRepository.save(matricula);
    }

    @Transactional
    public void cancelar(Long id, String motivo) {
        Matricula matricula = obtenerPorId(id);

        if (matricula.getEstado() == EstadoMatricula.CANCELADA) {
            throw new IllegalStateException("La matrícula ya está cancelada");
        }

        matricula.setEstado(EstadoMatricula.CANCELADA);
        if (motivo != null && !motivo.isEmpty()) {
            String obs = matricula.getObservaciones() != null ?
                matricula.getObservaciones() + " | " : "";
            matricula.setObservaciones(obs + "Cancelada: " + motivo);
        }
        matriculaRepository.save(matricula);
    }

    @Transactional
    public void eliminar(Long id) {
        Matricula matricula = obtenerPorId(id);
        // Eliminar físicamente la matrícula de la base de datos
        // Esto permite eliminar cursos que tengan matrículas completadas o canceladas
        matriculaRepository.delete(matricula);
    }

    @Transactional(readOnly = true)
    public long contarMatriculasActivasCurso(Long cursoId) {
        return matriculaRepository.countByCursoIdAndEstado(cursoId, EstadoMatricula.ACTIVA);
    }

    @Transactional(readOnly = true)
    public long contarMatriculasActivasAlumno(Long alumnoId) {
        return matriculaRepository.countByAlumnoIdAndEstado(alumnoId, EstadoMatricula.ACTIVA);
    }

    private void validarAccesoAcademia(Long academiaId) {
        if (!securityUtils.tieneRol("ADMIN")) {
            Long miAcademiaId = securityUtils.getAcademiaIdActual();
            if (miAcademiaId == null || !miAcademiaId.equals(academiaId)) {
                throw new IllegalArgumentException("No tiene acceso a esta academia");
            }
        }
    }
}
