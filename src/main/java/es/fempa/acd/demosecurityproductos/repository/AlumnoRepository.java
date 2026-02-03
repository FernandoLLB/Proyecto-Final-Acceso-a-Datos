package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de entidades Alumno.
 * Proporciona operaciones CRUD y consultas personalizadas para alumnos de academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    /**
     * Busca todos los alumnos de una academia específica.
     *
     * @param academiaId el ID de la academia
     * @return lista de alumnos pertenecientes a la academia
     */
    List<Alumno> findByAcademiaId(Long academiaId);

    /**
     * Busca alumnos de una academia filtrados por estado de matrícula.
     *
     * @param academiaId el ID de la academia
     * @param estadoMatricula el estado de matrícula (ACTIVO, INACTIVO, etc.)
     * @return lista de alumnos que coinciden con los criterios
     */
    List<Alumno> findByAcademiaIdAndEstadoMatricula(Long academiaId, String estadoMatricula);

    /**
     * Cuenta el total de alumnos de una academia.
     *
     * @param academiaId el ID de la academia
     * @return número total de alumnos en la academia
     */
    long countByAcademiaId(Long academiaId);

    /**
     * Cuenta alumnos de una academia con un estado específico.
     *
     * @param academiaId el ID de la academia
     * @param estadoMatricula el estado de matrícula a contar
     * @return número de alumnos con el estado especificado
     */
    long countByAcademiaIdAndEstadoMatricula(Long academiaId, String estadoMatricula);

    /**
     * Busca un alumno por el ID de su usuario asociado.
     *
     * @param usuarioId el ID del usuario
     * @return Optional con el alumno si existe, vacío si no existe
     */
    Optional<Alumno> findByUsuarioId(Long usuarioId);

    /**
     * Busca un alumno por su entidad Usuario.
     *
     * @param usuario el usuario asociado al alumno
     * @return Optional con el alumno si existe, vacío si no existe
     */
    Optional<Alumno> findByUsuario(Usuario usuario);

    /**
     * Obtiene los 5 alumnos más recientemente registrados de una academia.
     *
     * @param academiaId el ID de la academia
     * @return lista de hasta 5 alumnos ordenados por fecha de registro descendente
     */
    List<Alumno> findTop5ByAcademiaIdOrderByFechaRegistroDesc(Long academiaId);
}
