package es.fempa.acd.demosecurityproductos.repository;

import es.fempa.acd.demosecurityproductos.model.Academia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para AcademiaRepository
 */
@DataJpaTest
@ActiveProfiles("test")
class AcademiaRepositoryTest {

    @Autowired
    private AcademiaRepository academiaRepository;

    private Academia academiaActiva;
    private Academia academiaInactiva;

    @BeforeEach
    void setUp() {
        academiaRepository.deleteAll();

        academiaActiva = new Academia();
        academiaActiva.setNombre("Academia Activa");
        academiaActiva.setActiva(true);
        academiaActiva.setFechaAlta(LocalDateTime.now());
        academiaActiva.setNifCif("B12345678");
        academiaActiva.setEmailContacto("activa@test.com");
        academiaActiva = academiaRepository.save(academiaActiva);

        academiaInactiva = new Academia();
        academiaInactiva.setNombre("Academia Inactiva");
        academiaInactiva.setActiva(false);
        academiaInactiva.setFechaAlta(LocalDateTime.now());
        academiaInactiva = academiaRepository.save(academiaInactiva);
    }

    @Test
    @DisplayName("findByActivaTrue retorna solo academias activas")
    void findByActivaTrue() {
        List<Academia> activas = academiaRepository.findByActivaTrue();

        assertEquals(1, activas.size());
        assertTrue(activas.get(0).getActiva());
        assertEquals("Academia Activa", activas.get(0).getNombre());
    }

    @Test
    @DisplayName("findByActivaFalse retorna solo academias inactivas")
    void findByActivaFalse() {
        List<Academia> inactivas = academiaRepository.findByActivaFalse();

        assertEquals(1, inactivas.size());
        assertFalse(inactivas.get(0).getActiva());
        assertEquals("Academia Inactiva", inactivas.get(0).getNombre());
    }

    @Test
    @DisplayName("findByNombre retorna academia por nombre exacto")
    void findByNombre() {
        Optional<Academia> encontrada = academiaRepository.findByNombre("Academia Activa");

        assertTrue(encontrada.isPresent());
        assertEquals("Academia Activa", encontrada.get().getNombre());
    }

    @Test
    @DisplayName("findByNombre retorna vacío si no existe")
    void findByNombreNoExiste() {
        Optional<Academia> encontrada = academiaRepository.findByNombre("No Existe");

        assertFalse(encontrada.isPresent());
    }

    @Test
    @DisplayName("countByActiva cuenta academias por estado")
    void countByActiva() {
        long activas = academiaRepository.countByActiva(true);
        long inactivas = academiaRepository.countByActiva(false);

        assertEquals(1, activas);
        assertEquals(1, inactivas);
    }

    @Test
    @DisplayName("save persiste academia correctamente")
    void save() {
        Academia nueva = new Academia();
        nueva.setNombre("Nueva Academia");
        nueva.setActiva(true);
        nueva.setFechaAlta(LocalDateTime.now());

        Academia guardada = academiaRepository.save(nueva);

        assertNotNull(guardada.getId());
        assertEquals("Nueva Academia", guardada.getNombre());
    }

    @Test
    @DisplayName("findById retorna academia existente")
    void findById() {
        Optional<Academia> encontrada = academiaRepository.findById(academiaActiva.getId());

        assertTrue(encontrada.isPresent());
        assertEquals(academiaActiva.getId(), encontrada.get().getId());
    }

    @Test
    @DisplayName("findAll retorna todas las academias")
    void findAll() {
        List<Academia> todas = academiaRepository.findAll();

        assertEquals(2, todas.size());
    }

    @Test
    @DisplayName("delete elimina academia")
    void delete() {
        long countAntes = academiaRepository.count();
        academiaRepository.delete(academiaInactiva);
        long countDespues = academiaRepository.count();

        assertEquals(countAntes - 1, countDespues);
    }
}
