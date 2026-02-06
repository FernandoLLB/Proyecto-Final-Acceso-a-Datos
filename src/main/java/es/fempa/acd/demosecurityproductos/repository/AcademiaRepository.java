package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de entidades Academia.
 * Proporciona operaciones CRUD y consultas personalizadas para academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Repository
public interface AcademiaRepository extends JpaRepository<Academia, Long> {

    /**
     * Busca todas las academias activas en el sistema.
     *
     * @return lista de academias con estado activo
     */
    List<Academia> findByActivaTrue();

    /**
     * Busca todas las academias inactivas en el sistema.
     *
     * @return lista de academias con estado inactivo
     */
    List<Academia> findByActivaFalse();

    /**
     * Busca una academia por su nombre exacto.
     *
     * @param nombre el nombre de la academia a buscar
     * @return Optional con la academia si existe, vacío si no existe
     */
    Optional<Academia> findByNombre(String nombre);

    /**
     * Cuenta las academias según su estado activo o inactivo.
     *
     * @param activa true para contar activas, false para contar inactivas
     * @return número de academias con el estado especificado
     */
    long countByActiva(Boolean activa);

    /**
     * Busca todas las academias de un propietario específico.
     *
     * @param propietario el propietario dueño de las academias
     * @return lista de academias del propietario
     */
    List<Academia> findByPropietario(Propietario propietario);

    /**
     * Busca todas las academias de un propietario por su ID.
     *
     * @param propietarioId ID del propietario
     * @return lista de academias del propietario
     */
    List<Academia> findByPropietarioId(Long propietarioId);

    /**
     * Busca todas las academias activas de un propietario.
     *
     * @param propietarioId ID del propietario
     * @return lista de academias activas del propietario
     */
    List<Academia> findByPropietarioIdAndActivaTrue(Long propietarioId);

    /**
     * Cuenta las academias de un propietario específico.
     *
     * @param propietarioId ID del propietario
     * @return número de academias del propietario
     */
    long countByPropietarioId(Long propietarioId);

    /**
     * Cuenta las academias activas de un propietario específico.
     *
     * @param propietarioId ID del propietario
     * @return número de academias activas del propietario
     */
    long countByPropietarioIdAndActivaTrue(Long propietarioId);
}
