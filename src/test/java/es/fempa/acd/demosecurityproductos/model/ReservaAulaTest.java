package es.fempa.acd.demosecurityproductos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la entidad ReservaAula
 */
class ReservaAulaTest {

    private ReservaAula reserva;
    private Academia academia;
    private Aula aula;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        reserva = new ReservaAula();

        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");

        aula = new Aula();
        aula.setId(1L);
        aula.setNombre("Aula 101");
        aula.setAcademia(academia);
        aula.setCapacidad(30);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("profesor1");
        usuario.setRol(Rol.PROFESOR);
    }

    @Test
    @DisplayName("Constructor por defecto inicializa valores correctamente")
    void constructorPorDefecto() {
        assertNotNull(reserva);
        assertEquals(EstadoReserva.ACTIVA, reserva.getEstado());
        assertNotNull(reserva.getFechaCreacion());
    }

    @Test
    @DisplayName("Setters y getters funcionan correctamente")
    void settersYGetters() {
        Long id = 1L;
        LocalDateTime fechaInicio = LocalDateTime.of(2024, 3, 1, 9, 0);
        LocalDateTime fechaFin = LocalDateTime.of(2024, 3, 1, 11, 0);
        LocalDateTime fechaCreacion = LocalDateTime.of(2024, 2, 28, 10, 0);
        String descripcion = "Clase de Matemáticas";

        reserva.setId(id);
        reserva.setAcademia(academia);
        reserva.setAula(aula);
        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva.setDescripcion(descripcion);
        reserva.setCreadaPor(usuario);
        reserva.setFechaCreacion(fechaCreacion);

        assertEquals(id, reserva.getId());
        assertEquals(academia, reserva.getAcademia());
        assertEquals(aula, reserva.getAula());
        assertEquals(fechaInicio, reserva.getFechaInicio());
        assertEquals(fechaFin, reserva.getFechaFin());
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertEquals(descripcion, reserva.getDescripcion());
        assertEquals(usuario, reserva.getCreadaPor());
        assertEquals(fechaCreacion, reserva.getFechaCreacion());
    }

    @Test
    @DisplayName("Todos los estados de reserva son válidos")
    void todosLosEstadosDeReserva() {
        for (EstadoReserva estado : EstadoReserva.values()) {
            reserva.setEstado(estado);
            assertEquals(estado, reserva.getEstado());
        }
    }

    @Test
    @DisplayName("Relación con Academia es correcta")
    void relacionConAcademia() {
        reserva.setAcademia(academia);

        assertNotNull(reserva.getAcademia());
        assertEquals("Academia Test", reserva.getAcademia().getNombre());
    }

    @Test
    @DisplayName("Relación con Aula es correcta")
    void relacionConAula() {
        reserva.setAula(aula);

        assertNotNull(reserva.getAula());
        assertEquals("Aula 101", reserva.getAula().getNombre());
    }

    @Test
    @DisplayName("Relación con Usuario creador es correcta")
    void relacionConUsuarioCreador() {
        reserva.setCreadaPor(usuario);

        assertNotNull(reserva.getCreadaPor());
        assertEquals("profesor1", reserva.getCreadaPor().getUsername());
    }

    @Test
    @DisplayName("Cancelación registra usuario y fecha")
    void cancelacion() {
        Usuario cancelador = new Usuario();
        cancelador.setId(2L);
        cancelador.setUsername("admin");
        LocalDateTime fechaCancelacion = LocalDateTime.now();

        reserva.setEstado(EstadoReserva.CANCELADA);
        reserva.setCanceladaPor(cancelador);
        reserva.setFechaCancelacion(fechaCancelacion);

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertEquals(cancelador, reserva.getCanceladaPor());
        assertEquals(fechaCancelacion, reserva.getFechaCancelacion());
    }

    @Test
    @DisplayName("Descripción puede ser null")
    void descripcionPuedeSerNull() {
        reserva.setDescripcion(null);
        assertNull(reserva.getDescripcion());
    }

    @Test
    @DisplayName("Fechas de inicio y fin configuradas correctamente")
    void fechasDeInicioYFin() {
        LocalDateTime fechaInicio = LocalDateTime.of(2024, 3, 1, 9, 0);
        LocalDateTime fechaFin = LocalDateTime.of(2024, 3, 1, 11, 0);

        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);

        assertEquals(fechaInicio, reserva.getFechaInicio());
        assertEquals(fechaFin, reserva.getFechaFin());
        assertTrue(reserva.getFechaInicio().isBefore(reserva.getFechaFin()));
    }
}
