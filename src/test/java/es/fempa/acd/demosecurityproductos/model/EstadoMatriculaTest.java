package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para el enum EstadoMatricula
 */
class EstadoMatriculaTest {

    @Test
    @DisplayName("Enum EstadoMatricula contiene todos los estados esperados")
    void estadosExisten() {
        EstadoMatricula[] estados = EstadoMatricula.values();

        assertEquals(3, estados.length);

        assertNotNull(EstadoMatricula.valueOf("ACTIVA"));
        assertNotNull(EstadoMatricula.valueOf("COMPLETADA"));
        assertNotNull(EstadoMatricula.valueOf("CANCELADA"));
    }

    @Test
    @DisplayName("Estado ACTIVA indica que el alumno está cursando")
    void estadoActiva() {
        EstadoMatricula activa = EstadoMatricula.ACTIVA;
        assertEquals("ACTIVA", activa.name());
    }

    @Test
    @DisplayName("Estado COMPLETADA indica que el alumno ha completado el curso")
    void estadoCompletada() {
        EstadoMatricula completada = EstadoMatricula.COMPLETADA;
        assertEquals("COMPLETADA", completada.name());
    }

    @Test
    @DisplayName("Estado CANCELADA indica que la matrícula ha sido cancelada")
    void estadoCancelada() {
        EstadoMatricula cancelada = EstadoMatricula.CANCELADA;
        assertEquals("CANCELADA", cancelada.name());
    }

    @Test
    @DisplayName("valueOf devuelve estado correcto")
    void valueOfCorrecto() {
        assertEquals(EstadoMatricula.ACTIVA, EstadoMatricula.valueOf("ACTIVA"));
        assertEquals(EstadoMatricula.COMPLETADA, EstadoMatricula.valueOf("COMPLETADA"));
        assertEquals(EstadoMatricula.CANCELADA, EstadoMatricula.valueOf("CANCELADA"));
    }

    @Test
    @DisplayName("valueOf lanza excepción para estado inexistente")
    void valueOfEstadoInexistente() {
        assertThrows(IllegalArgumentException.class, () -> {
            EstadoMatricula.valueOf("PENDIENTE");
        });
    }
}
