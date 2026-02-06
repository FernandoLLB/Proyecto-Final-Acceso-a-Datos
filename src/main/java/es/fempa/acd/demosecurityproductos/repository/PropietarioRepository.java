package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Propietario;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de entidades Propietario.
 * Proporciona operaciones CRUD y consultas personalizadas para propietarios.
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Repository
public interface PropietarioRepository extends JpaRepository<Propietario, Long> {

    /**
     * Busca un propietario por su usuario asociado.
     *
     * @param usuario usuario asociado al propietario
     * @return Optional conteniendo el propietario si se encuentra
     */
    Optional<Propietario> findByUsuario(Usuario usuario);

    /**
     * Busca un propietario por el ID de su usuario asociado.
     *
     * @param usuarioId ID del usuario asociado
     * @return Optional conteniendo el propietario si se encuentra
     */
    Optional<Propietario> findByUsuarioId(Long usuarioId);

    /**
     * Busca todos los propietarios activos en el sistema.
     *
     * @return lista de propietarios con estado activo
     */
    List<Propietario> findByActivoTrue();

    /**
     * Busca todos los propietarios inactivos en el sistema.
     *
     * @return lista de propietarios con estado inactivo
     */
    List<Propietario> findByActivoFalse();

    /**
     * Busca propietarios por razón social (búsqueda parcial).
     *
     * @param razonSocial razón social a buscar
     * @return lista de propietarios que coinciden
     */
    List<Propietario> findByRazonSocialContainingIgnoreCase(String razonSocial);

    /**
     * Busca propietarios por NIF/CIF.
     *
     * @param nifCif NIF o CIF del propietario
     * @return Optional conteniendo el propietario si se encuentra
     */
    Optional<Propietario> findByNifCif(String nifCif);

    /**
     * Cuenta el número de propietarios activos.
     *
     * @return número de propietarios activos
     */
    long countByActivoTrue();
}
