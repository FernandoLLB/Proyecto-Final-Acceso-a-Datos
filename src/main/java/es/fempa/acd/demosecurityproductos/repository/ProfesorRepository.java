package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

    List<Profesor> findByAcademiaId(Long academiaId);

    @Query("SELECT p FROM Profesor p WHERE p.academia.id = :academiaId AND p.usuario.activo = :activo")
    List<Profesor> findByAcademiaIdAndUsuarioActivo(@Param("academiaId") Long academiaId, @Param("activo") Boolean activo);

    long countByAcademiaId(Long academiaId);

    Optional<Profesor> findByUsuarioId(Long usuarioId);

    Optional<Profesor> findByUsuario(Usuario usuario);
}
