package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Academia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de entidades Academia.
 * Proporciona operaciones CRUD y consultas personalizadas para academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
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
}
