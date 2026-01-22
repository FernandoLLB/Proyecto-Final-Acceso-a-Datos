package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    List<Profesor> findByAcademiaId(Long academiaId);

    long countByAcademiaId(Long academiaId);

    Optional<Profesor> findByUsuarioId(Long usuarioId);

    Optional<Profesor> findByUsuario(Usuario usuario);
}
