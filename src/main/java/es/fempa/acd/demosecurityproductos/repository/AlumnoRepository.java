package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Alumno;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    List<Alumno> findByAcademiaId(Long academiaId);

    long countByAcademiaId(Long academiaId);

    long countByAcademiaIdAndEstadoMatricula(Long academiaId, String estadoMatricula);

    Optional<Alumno> findByUsuarioId(Long usuarioId);

    Optional<Alumno> findByUsuario(Usuario usuario);

    List<Alumno> findTop5ByAcademiaIdOrderByFechaRegistroDesc(Long academiaId);
}
