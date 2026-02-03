package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.repository.AlumnoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para la gestión de alumnos.
 * Proporciona operaciones CRUD y consultas para alumnos de academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    /**
     * Constructor del servicio de alumnos.
     *
     * @param alumnoRepository repositorio de alumnos
     */
    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    /**
     * Lista todos los alumnos de una academia.
     *
     * @param academiaId el ID de la academia
     * @return lista de alumnos de la academia
     */
    @Transactional(readOnly = true)
    public List<Alumno> listarPorAcademia(Long academiaId) {
        return alumnoRepository.findByAcademiaId(academiaId);
    }

    /**
     * Lista alumnos de una academia filtrados por estado de matrícula.
     *
     * @param academiaId el ID de la academia
     * @param estado el estado de matrícula (ACTIVO, INACTIVO, etc.)
     * @return lista de alumnos que coinciden con los criterios
     */
    @Transactional(readOnly = true)
    public List<Alumno> listarPorAcademiaYEstado(Long academiaId, String estado) {
        return alumnoRepository.findByAcademiaIdAndEstadoMatricula(academiaId, estado);
    }

    /**
     * Obtiene un alumno por el ID de su usuario asociado.
     *
     * @param usuarioId el ID del usuario
     * @return el alumno encontrado
     * @throws IllegalArgumentException si el alumno no existe
     */
    @Transactional(readOnly = true)
    public Alumno obtenerPorUsuarioId(Long usuarioId) {
        return alumnoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
    }

    /**
     * Cuenta el total de alumnos de una academia.
     *
     * @param academiaId el ID de la academia
     * @return número total de alumnos
     */
    @Transactional(readOnly = true)
    public long contarPorAcademia(Long academiaId) {
        return alumnoRepository.countByAcademiaId(academiaId);
    }

    /**
     * Cuenta los alumnos activos de una academia.
     *
     * @param academiaId el ID de la academia
     * @return número de alumnos activos
     */
    @Transactional(readOnly = true)
    public long contarActivosPorAcademia(Long academiaId) {
        return alumnoRepository.countByAcademiaIdAndEstadoMatricula(academiaId, "ACTIVO");
    }

    /**
     * Cuenta los alumnos inactivos de una academia.
     *
     * @param academiaId el ID de la academia
     * @return número de alumnos inactivos
     */
    @Transactional(readOnly = true)
    public long contarInactivosPorAcademia(Long academiaId) {
        return alumnoRepository.countByAcademiaIdAndEstadoMatricula(academiaId, "INACTIVO");
    }

    /**
     * Obtiene un alumno por su ID.
     *
     * @param id el ID del alumno
     * @return el alumno encontrado
     * @throws IllegalArgumentException si el alumno no existe
     */
    @Transactional(readOnly = true)
    public Alumno obtenerPorId(Long id) {
        return alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
    }

    /**
     * Obtiene los últimos alumnos registrados de una academia.
     *
     * @param academiaId el ID de la academia
     * @param cantidad el número de alumnos a obtener (se usa el límite del repositorio)
     * @return lista de los últimos alumnos registrados
     */
    @Transactional(readOnly = true)
    public List<Alumno> obtenerUltimosRegistrados(Long academiaId, int cantidad) {
        return alumnoRepository.findTop5ByAcademiaIdOrderByFechaRegistroDesc(academiaId);
    }

    /**
     * Crea un nuevo alumno en el sistema.
     * Establece automáticamente el estado como ACTIVO si no está definido.
     *
     * @param alumno el alumno a crear
     * @return el alumno creado con su ID asignado
     */
    @Transactional
    public Alumno crear(Alumno alumno) {
        if (alumno.getEstadoMatricula() == null) {
            alumno.setEstadoMatricula("ACTIVO");
        }
        return alumnoRepository.save(alumno);
    }

    /**
     * Actualiza los datos de un alumno existente.
     *
     * @param alumno el alumno con los datos actualizados
     * @return el alumno actualizado
     * @throws IllegalArgumentException si el alumno no existe
     */
    @Transactional
    public Alumno actualizar(Alumno alumno) {
        Alumno existente = obtenerPorId(alumno.getId());
        existente.setEstadoMatricula(alumno.getEstadoMatricula());
        existente.setObservaciones(alumno.getObservaciones());
        return alumnoRepository.save(existente);
    }

    /**
     * Activa un alumno y su usuario asociado.
     *
     * @param id el ID del alumno
     * @throws IllegalArgumentException si el alumno no existe
     */
    @Transactional
    public void activar(Long id) {
        Alumno alumno = obtenerPorId(id);
        alumno.setEstadoMatricula("ACTIVO");
        alumno.getUsuario().setActivo(true);
        alumnoRepository.save(alumno);
    }

    /**
     * Desactiva un alumno y su usuario asociado.
     *
     * @param id el ID del alumno
     * @throws IllegalArgumentException si el alumno no existe
     */
    @Transactional
    public void desactivar(Long id) {
        Alumno alumno = obtenerPorId(id);
        alumno.setEstadoMatricula("INACTIVO");
        alumno.getUsuario().setActivo(false);
        alumnoRepository.save(alumno);
    }
}
