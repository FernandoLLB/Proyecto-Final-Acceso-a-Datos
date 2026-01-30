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

@Service
public class AcademiaService {

    private final AcademiaRepository academiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProfesorRepository profesorRepository;
    private final AlumnoRepository alumnoRepository;
    private final AulaRepository aulaRepository;
    private final ReservaAulaRepository reservaAulaRepository;

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

    @PreAuthorize("hasRole('ADMIN')")
    public List<Academia> listarTodas() {
        return academiaRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Academia> listarActivas() {
        return academiaRepository.findByActivaTrue();
    }

    // Método público para registro de alumnos (sin autenticación)
    public List<Academia> listarActivasParaRegistro() {
        return academiaRepository.findByActivaTrue();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Academia obtenerPorId(Long id) {
        return academiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Academia no encontrada"));
    }

    // Método público para obtener academia por ID (para registro)
    public Academia obtenerPorIdParaRegistro(Long id) {
        Academia academia = academiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Academia no encontrada"));
        if (!academia.getActiva()) {
            throw new IllegalArgumentException("La academia seleccionada no está activa");
        }
        return academia;
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void activar(Long id) {
        Academia academia = obtenerPorId(id);
        academia.setActiva(true);
        academiaRepository.save(academia);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void desactivar(Long id) {
        Academia academia = obtenerPorId(id);
        academia.setActiva(false);
        academiaRepository.save(academia);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> obtenerEstadisticasGlobales() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAcademias", academiaRepository.count());
        stats.put("academiasActivas", academiaRepository.countByActiva(true));
        stats.put("academiasInactivas", academiaRepository.countByActiva(false));
        stats.put("totalUsuarios", usuarioRepository.count());
        return stats;
    }

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
