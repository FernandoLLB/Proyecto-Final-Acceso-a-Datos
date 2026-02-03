package es.fempa.acd.demosecurityproductos;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de integración para verificar que el contexto de la aplicación carga correctamente.
 * Este test está deshabilitado por defecto ya que requiere una base de datos MySQL configurada.
 * Los tests unitarios y de repositorio usan H2 en memoria.
 */
@SpringBootTest
@ActiveProfiles("test")
@Disabled("Test de integración completa - requiere configuración de BD")
class GestionAcademiasApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring Boot carga correctamente
    }

}
