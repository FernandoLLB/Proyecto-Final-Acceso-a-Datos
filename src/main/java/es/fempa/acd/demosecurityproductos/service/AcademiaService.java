package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.repository.AcademiaRepository;
import es.fempa.acd.demosecurityproductos.repository.AlumnoRepository;
import es.fempa.acd.demosecurityproductos.repository.ProfesorRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import es.fempa.acd.demosecurityproductos.repository.AulaRepository;
import es.fempa.acd.demosecurityproductos.repository.ReservaAulaRepository;
import es.fempa.acd.demosecurityproductos.model.EstadoReserva;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para la gestión de academias.
 * Proporciona métodos para crear, actualizar, listar y obtener estadísticas de academias.
 * Accesible por usuarios con rol ADMIN y PROPIETARIO (excepto métodos públicos de registro).
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Service
public class AcademiaService {

    private final AcademiaRepository academiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProfesorRepository profesorRepository;
    private final AlumnoRepository alumnoRepository;
    private final AulaRepository aulaRepository;
    private final ReservaAulaRepository reservaAulaRepository;

    /**
     * Constructor del servicio de academias.
     *
     * @param academiaRepository repositorio de academias
     * @param usuarioRepository repositorio de usuarios
     * @param profesorRepository repositorio de profesores
     * @param alumnoRepository repositorio de alumnos
     * @param aulaRepository repositorio de aulas
     * @param reservaAulaRepository repositorio de reservas de aulas
     */
    public AcademiaService(AcademiaRepository academiaRepository,
                          UsuarioRepository usuarioRepository,
                          ProfesorRepository profesorRepository,
                          AlumnoRepository alumnoRepository,
                          AulaRepository aulaRepository,
                          ReservaAulaRepository reservaAulaRepository) {
        this.academiaRepository = academiaRepository;
        this.usuarioRepository = usuarioRepository;
        this.profesorRepository = profesorRepository;
        this.alumnoRepository = alumnoRepository;
        this.aulaRepository = aulaRepository;
        this.reservaAulaRepository = reservaAulaRepository;
    }

    /**
     * Lista todas las academias del sistema.
     * Solo accesible por administradores.
     *
     * @return lista de todas las academias
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<Academia> listarTodas() {
        return academiaRepository.findAll();
    }

    /**
     * Lista solo las academias activas.
     * Solo accesible por administradores.
     *
     * @return lista de academias activas
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<Academia> listarActivas() {
        return academiaRepository.findByActivaTrue();
    }

    /**
     * Lista solo las academias activas para el proceso de registro público.
     * Método público accesible sin autenticación.
     *
     * @return lista de academias activas
     */
    public List<Academia> listarActivasParaRegistro() {
        return academiaRepository.findByActivaTrue();
    }

    /**
     * Lista todas las academias de un propietario específico.
     * Accesible por administradores y el propio propietario.
     *
     * @param propietarioId ID del propietario
     * @return lista de academias del propietario
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    public List<Academia> listarPorPropietario(Long propietarioId) {
        return academiaRepository.findByPropietarioId(propietarioId);
    }

    /**
     * Lista solo las academias activas de un propietario específico.
     * Accesible por administradores y el propio propietario.
     *
     * @param propietarioId ID del propietario
     * @return lista de academias activas del propietario
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    public List<Academia> listarActivasPorPropietario(Long propietarioId) {
        return academiaRepository.findByPropietarioIdAndActivaTrue(propietarioId);
    }

    /**
     * Obtiene una academia por su ID.
     * Accesible por administradores y propietarios.
     *
     * @param id el ID de la academia
     * @return la academia encontrada
     * @throws IllegalArgumentException si la academia no existe
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    public Academia obtenerPorId(Long id) {
        return academiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Academia no encontrada"));
    }

    /**
     * Obtiene una academia por su ID para el proceso de registro público.
     * Método público accesible sin autenticación.
     * Verifica que la academia esté activa.
     *
     * @param id el ID de la academia
     * @return la academia encontrada
     * @throws IllegalArgumentException si la academia no existe o no está activa
     */
    public Academia obtenerPorIdParaRegistro(Long id) {
        Academia academia = academiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Academia no encontrada"));
        if (!academia.getActiva()) {
            throw new IllegalArgumentException("La academia seleccionada no está activa");
        }
        return academia;
    }

    /**
     * Crea una nueva academia en el sistema.
     * Establece automáticamente la fecha de alta y el estado activo si no están definidos.
     * Accesible por administradores y propietarios.
     *
     * @param academia la academia a crear
     * @return la academia creada con su ID asignado
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    @Transactional
    public Academia crear(Academia academia) {
        if (academia.getFechaAlta() == null) {
            academia.setFechaAlta(LocalDateTime.now());
        }
        if (academia.getActiva() == null) {
            academia.setActiva(true);
        }
        return academiaRepository.save(academia);
    }

    /**
     * Actualiza los datos de una academia existente.
     * Accesible por administradores y propietarios.
     *
     * @param id el ID de la academia a actualizar
     * @param academiaActualizada los nuevos datos de la academia
     * @return la academia actualizada
     * @throws IllegalArgumentException si la academia no existe
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    @Transactional
    public Academia actualizar(Long id, Academia academiaActualizada) {
        Academia academia = obtenerPorId(id);
        academia.setNombre(academiaActualizada.getNombre());
        academia.setNifCif(academiaActualizada.getNifCif());
        academia.setEmailContacto(academiaActualizada.getEmailContacto());
        academia.setTelefono(academiaActualizada.getTelefono());
        academia.setDireccion(academiaActualizada.getDireccion());
        return academiaRepository.save(academia);
    }

    /**
     * Activa una academia previamente desactivada.
     * Accesible por administradores y propietarios.
     *
     * @param id el ID de la academia a activar
     * @throws IllegalArgumentException si la academia no existe
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    @Transactional
    public void activar(Long id) {
        Academia academia = obtenerPorId(id);
        academia.setActiva(true);
        academiaRepository.save(academia);
    }

    /**
     * Desactiva una academia.
     * Accesible por administradores y propietarios.
     *
     * @param id el ID de la academia a desactivar
     * @throws IllegalArgumentException si la academia no existe
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    @Transactional
    public void desactivar(Long id) {
        Academia academia = obtenerPorId(id);
        academia.setActiva(false);
        academiaRepository.save(academia);
    }

    /**
     * Obtiene estadísticas globales del sistema.
     * Incluye totales de academias (activas/inactivas) y usuarios.
     * Solo accesible por administradores.
     *
     * @return mapa con las estadísticas globales del sistema
     */
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> obtenerEstadisticasGlobales() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAcademias", academiaRepository.count());
        stats.put("academiasActivas", academiaRepository.countByActiva(true));
        stats.put("academiasInactivas", academiaRepository.countByActiva(false));
        stats.put("totalUsuarios", usuarioRepository.count());
        return stats;
    }

    /**
     * Obtiene estadísticas detalladas de una academia específica.
     * Incluye información sobre alumnos, profesores, aulas y reservas.
     *
     * @param academiaId el ID de la academia
     * @return mapa con las estadísticas de la academia
     * @throws IllegalArgumentException si la academia no existe
     */
    public Map<String, Object> obtenerEstadisticasAcademia(Long academiaId) {
        Map<String, Object> stats = new HashMap<>();
        Academia academia = obtenerPorId(academiaId);

        stats.put("nombreAcademia", academia.getNombre());
        stats.put("totalAlumnos", alumnoRepository.countByAcademiaId(academiaId));
        stats.put("alumnosActivos", alumnoRepository.countByAcademiaIdAndEstadoMatricula(academiaId, "ACTIVO"));
        stats.put("alumnosInactivos", alumnoRepository.countByAcademiaIdAndEstadoMatricula(academiaId, "INACTIVO"));
        stats.put("totalProfesores", profesorRepository.countByAcademiaId(academiaId));
        stats.put("totalUsuarios", usuarioRepository.countByAcademiaId(academiaId));
        stats.put("totalAulas", aulaRepository.countByAcademiaId(academiaId));
        stats.put("aulasActivas", aulaRepository.countByAcademiaIdAndActiva(academiaId, true));
        stats.put("reservasActivas", reservaAulaRepository.countByAcademiaIdAndEstado(academiaId, EstadoReserva.ACTIVA));

        return stats;
    }
}
