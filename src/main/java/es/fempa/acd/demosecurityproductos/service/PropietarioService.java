package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Propietario;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.repository.AcademiaRepository;
import es.fempa.acd.demosecurityproductos.repository.PropietarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de propietarios.
 * Proporciona métodos para crear, actualizar, listar y gestionar propietarios del sistema SaaS.
 * Los propietarios son clientes que pueden tener múltiples academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Service
public class PropietarioService {

    private final PropietarioRepository propietarioRepository;
    private final AcademiaRepository academiaRepository;

    /**
     * Constructor del servicio de propietarios.
     *
     * @param propietarioRepository repositorio de propietarios
     * @param academiaRepository repositorio de academias
     */
    public PropietarioService(PropietarioRepository propietarioRepository,
                             AcademiaRepository academiaRepository) {
        this.propietarioRepository = propietarioRepository;
        this.academiaRepository = academiaRepository;
    }

    /**
     * Lista todos los propietarios del sistema.
     * Solo accesible por administradores.
     *
     * @return lista de todos los propietarios
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<Propietario> listarTodos() {
        return propietarioRepository.findAll();
    }

    /**
     * Lista solo los propietarios activos.
     * Solo accesible por administradores.
     *
     * @return lista de propietarios activos
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<Propietario> listarActivos() {
        return propietarioRepository.findByActivoTrue();
    }

    /**
     * Obtiene un propietario por su ID.
     * Accesible por administradores y el propio propietario.
     *
     * @param id ID del propietario
     * @return el propietario encontrado
     * @throws RuntimeException si no se encuentra el propietario
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    public Propietario obtenerPorId(Long id) {
        return propietarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado con ID: " + id));
    }

    /**
     * Obtiene un propietario por su usuario asociado.
     *
     * @param usuario usuario asociado al propietario
     * @return Optional conteniendo el propietario si se encuentra
     */
    public Optional<Propietario> obtenerPorUsuario(Usuario usuario) {
        return propietarioRepository.findByUsuario(usuario);
    }

    /**
     * Obtiene un propietario por el ID de su usuario asociado.
     *
     * @param usuarioId ID del usuario asociado
     * @return Optional conteniendo el propietario si se encuentra
     */
    public Optional<Propietario> obtenerPorUsuarioId(Long usuarioId) {
        return propietarioRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Crea un nuevo propietario en el sistema.
     * Solo accesible por administradores.
     *
     * @param propietario propietario a crear
     * @return el propietario creado
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Propietario crear(Propietario propietario) {
        return propietarioRepository.save(propietario);
    }

    /**
     * Actualiza un propietario existente.
     * Solo accesible por administradores.
     *
     * @param id ID del propietario a actualizar
     * @param propietarioActualizado datos actualizados del propietario
     * @return el propietario actualizado
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Propietario actualizar(Long id, Propietario propietarioActualizado) {
        Propietario propietario = obtenerPorId(id);

        if (propietarioActualizado.getRazonSocial() != null) {
            propietario.setRazonSocial(propietarioActualizado.getRazonSocial());
        }
        if (propietarioActualizado.getNifCif() != null) {
            propietario.setNifCif(propietarioActualizado.getNifCif());
        }
        if (propietarioActualizado.getTelefono() != null) {
            propietario.setTelefono(propietarioActualizado.getTelefono());
        }
        if (propietarioActualizado.getDireccion() != null) {
            propietario.setDireccion(propietarioActualizado.getDireccion());
        }

        return propietarioRepository.save(propietario);
    }

    /**
     * Activa o desactiva un propietario.
     * Solo accesible por administradores.
     *
     * @param id ID del propietario
     * @param activo true para activar, false para desactivar
     * @return el propietario actualizado
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Propietario cambiarEstado(Long id, Boolean activo) {
        Propietario propietario = obtenerPorId(id);
        propietario.setActivo(activo);
        return propietarioRepository.save(propietario);
    }

    /**
     * Obtiene todas las academias de un propietario.
     *
     * @param propietarioId ID del propietario
     * @return lista de academias del propietario
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    public List<Academia> obtenerAcademias(Long propietarioId) {
        return academiaRepository.findByPropietarioId(propietarioId);
    }

    /**
     * Obtiene todas las academias activas de un propietario.
     *
     * @param propietarioId ID del propietario
     * @return lista de academias activas del propietario
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PROPIETARIO')")
    public List<Academia> obtenerAcademiasActivas(Long propietarioId) {
        return academiaRepository.findByPropietarioIdAndActivaTrue(propietarioId);
    }

    /**
     * Cuenta el número de academias de un propietario.
     *
     * @param propietarioId ID del propietario
     * @return número de academias
     */
    public long contarAcademias(Long propietarioId) {
        return academiaRepository.countByPropietarioId(propietarioId);
    }

    /**
     * Busca propietarios por razón social.
     * Solo accesible por administradores.
     *
     * @param razonSocial razón social a buscar
     * @return lista de propietarios que coinciden
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<Propietario> buscarPorRazonSocial(String razonSocial) {
        return propietarioRepository.findByRazonSocialContainingIgnoreCase(razonSocial);
    }

    /**
     * Busca un propietario por NIF/CIF.
     * Solo accesible por administradores.
     *
     * @param nifCif NIF o CIF del propietario
     * @return Optional conteniendo el propietario si se encuentra
     */
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Propietario> buscarPorNifCif(String nifCif) {
        return propietarioRepository.findByNifCif(nifCif);
    }

    /**
     * Cuenta el número total de propietarios activos.
     * Solo accesible por administradores.
     *
     * @return número de propietarios activos
     */
    @PreAuthorize("hasRole('ADMIN')")
    public long contarActivos() {
        return propietarioRepository.countByActivoTrue();
    }

    /**
     * Verifica si un usuario es propietario.
     *
     * @param usuario usuario a verificar
     * @return true si el usuario es propietario, false en caso contrario
     */
    public boolean esPropietario(Usuario usuario) {
        return propietarioRepository.findByUsuario(usuario).isPresent();
    }
}
