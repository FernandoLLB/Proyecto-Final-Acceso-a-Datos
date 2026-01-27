package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Curso;
import es.fempa.acd.demosecurityproductos.repository.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final SecurityUtils securityUtils;

    public CursoService(CursoRepository cursoRepository, SecurityUtils securityUtils) {
        this.cursoRepository = cursoRepository;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public Curso crear(Curso curso) {
        System.out.println("=== CursoService.crear() iniciado ===");

        Long academiaId = securityUtils.getAcademiaIdActual();
        System.out.println("Academia ID del usuario actual: " + academiaId);

        if (academiaId == null) {
            System.err.println("ERROR: Usuario sin academia asignada");
            throw new IllegalStateException("Usuario sin academia asignada");
        }

        System.out.println("Academia del curso: " + (curso.getAcademia() != null ? curso.getAcademia().getId() : "NULL"));

        if (curso.getAcademia() == null || !curso.getAcademia().getId().equals(academiaId)) {
            System.err.println("ERROR: Academia no coincide o es null");
            throw new IllegalArgumentException("No puede crear cursos para otra academia");
        }

        // Validar fechas
        System.out.println("Validando fechas: inicio=" + curso.getFechaInicio() + ", fin=" + curso.getFechaFin());
        if (curso.getFechaFin().isBefore(curso.getFechaInicio())) {
            System.err.println("ERROR: Fecha fin anterior a fecha inicio");
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        // Validar profesor pertenece a la academia
        if (curso.getProfesor() != null) {
            System.out.println("Academia del profesor: " + curso.getProfesor().getAcademia().getId());
            if (!curso.getProfesor().getAcademia().getId().equals(academiaId)) {
                System.err.println("ERROR: Profesor no pertenece a la academia");
                throw new IllegalArgumentException("El profesor no pertenece a esta academia");
            }
        }

        curso.setActivo(true);
        System.out.println("Guardando curso en BD...");
        Curso cursoGuardado = cursoRepository.save(curso);
        System.out.println("✅ Curso guardado con ID: " + cursoGuardado.getId());
        return cursoGuardado;
    }

    @Transactional(readOnly = true)
    public List<Curso> listarPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return cursoRepository.findByAcademiaId(academiaId);
    }

    @Transactional(readOnly = true)
    public List<Curso> listarActivosPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return cursoRepository.findByAcademiaIdAndActivo(academiaId, true);
    }

    @Transactional(readOnly = true)
    public List<Curso> listarPorProfesor(Long profesorId) {
        return cursoRepository.findByProfesorId(profesorId);
    }

    @Transactional(readOnly = true)
    public List<Curso> listarActivosPorProfesor(Long profesorId) {
        return cursoRepository.findByProfesorIdAndActivo(profesorId, true);
    }

    @Transactional(readOnly = true)
    public Curso obtenerPorId(Long id) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        if (academiaId == null) {
            throw new IllegalStateException("Usuario sin academia asignada");
        }

        return cursoRepository.findByIdAndAcademiaId(id, academiaId)
            .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado o no pertenece a su academia"));
    }

    @Transactional
    public Curso actualizar(Long id, Curso cursoActualizado) {
        Curso cursoExistente = obtenerPorId(id);

        // Validar fechas
        if (cursoActualizado.getFechaFin().isBefore(cursoActualizado.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        // Validar profesor pertenece a la academia
        if (cursoActualizado.getProfesor() != null) {
            Long academiaId = securityUtils.getAcademiaIdActual();
            if (!cursoActualizado.getProfesor().getAcademia().getId().equals(academiaId)) {
                throw new IllegalArgumentException("El profesor no pertenece a esta academia");
            }
        }

        cursoExistente.setNombre(cursoActualizado.getNombre());
        cursoExistente.setDescripcion(cursoActualizado.getDescripcion());
        cursoExistente.setDuracionHoras(cursoActualizado.getDuracionHoras());
        cursoExistente.setPrecio(cursoActualizado.getPrecio());
        cursoExistente.setFechaInicio(cursoActualizado.getFechaInicio());
        cursoExistente.setFechaFin(cursoActualizado.getFechaFin());
        cursoExistente.setCategoria(cursoActualizado.getCategoria());
        cursoExistente.setProfesor(cursoActualizado.getProfesor());
        cursoExistente.setPlazasDisponibles(cursoActualizado.getPlazasDisponibles());

        return cursoRepository.save(cursoExistente);
    }

    @Transactional
    public void activar(Long id) {
        Curso curso = obtenerPorId(id);
        curso.setActivo(true);
        cursoRepository.save(curso);
    }

    @Transactional
    public void desactivar(Long id) {
        Curso curso = obtenerPorId(id);
        curso.setActivo(false);
        cursoRepository.save(curso);
    }

    @Transactional(readOnly = true)
    public long contarPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return cursoRepository.countByAcademiaId(academiaId);
    }

    @Transactional(readOnly = true)
    public long contarActivosPorAcademia(Long academiaId) {
        validarAccesoAcademia(academiaId);
        return cursoRepository.countByAcademiaIdAndActivo(academiaId, true);
    }

    @Transactional(readOnly = true)
    public List<Curso> listarPorAcademiaYFechas(Long academiaId, LocalDate fechaDesde, LocalDate fechaHasta) {
        validarAccesoAcademia(academiaId);
        return cursoRepository.findByAcademiaIdAndFechaRange(academiaId, fechaDesde, fechaHasta);
    }

    @Transactional(readOnly = true)
    public List<Curso> listarPorAcademiaYCategoria(Long academiaId, String categoria) {
        validarAccesoAcademia(academiaId);
        return cursoRepository.findByAcademiaIdAndCategoria(academiaId, categoria);
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
