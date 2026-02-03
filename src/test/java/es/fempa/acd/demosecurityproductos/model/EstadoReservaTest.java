package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para el enum EstadoReserva
 */
class EstadoReservaTest {

    @Test
    @DisplayName("Enum EstadoReserva contiene todos los estados esperados")
    void estadosExisten() {
        EstadoReserva[] estados = EstadoReserva.values();

        assertEquals(2, estados.length);

        assertNotNull(EstadoReserva.valueOf("ACTIVA"));
        assertNotNull(EstadoReserva.valueOf("CANCELADA"));
    }

    @Test
    @DisplayName("Estado ACTIVA indica reserva activa y vigente")
    void estadoActiva() {
        EstadoReserva activa = EstadoReserva.ACTIVA;
        assertEquals("ACTIVA", activa.name());
    }

    @Test
    @DisplayName("Estado CANCELADA indica reserva cancelada")
    void estadoCancelada() {
        EstadoReserva cancelada = EstadoReserva.CANCELADA;
        assertEquals("CANCELADA", cancelada.name());
    }

    @Test
    @DisplayName("valueOf devuelve estado correcto")
    void valueOfCorrecto() {
        assertEquals(EstadoReserva.ACTIVA, EstadoReserva.valueOf("ACTIVA"));
        assertEquals(EstadoReserva.CANCELADA, EstadoReserva.valueOf("CANCELADA"));
    }

    @Test
    @DisplayName("valueOf lanza excepción para estado inexistente")
    void valueOfEstadoInexistente() {
        assertThrows(IllegalArgumentException.class, () -> {
            EstadoReserva.valueOf("PENDIENTE");
        });
    }
}
