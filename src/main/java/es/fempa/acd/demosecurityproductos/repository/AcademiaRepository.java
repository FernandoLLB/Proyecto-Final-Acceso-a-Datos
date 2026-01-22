package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Academia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademiaRepository extends JpaRepository<Academia, Long> {

    List<Academia> findByActivaTrue();

    List<Academia> findByActivaFalse();

    Optional<Academia> findByNombre(String nombre);

    long countByActiva(Boolean activa);
}
