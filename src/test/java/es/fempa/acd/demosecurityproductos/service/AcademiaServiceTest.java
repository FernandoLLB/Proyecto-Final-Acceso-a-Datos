package es.fempa.acd.demosecurityproductos.service;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.repository.AcademiaRepository;
import es.fempa.acd.demosecurityproductos.repository.AlumnoRepository;
import es.fempa.acd.demosecurityproductos.repository.AulaRepository;
import es.fempa.acd.demosecurityproductos.repository.ProfesorRepository;
import es.fempa.acd.demosecurityproductos.repository.ReservaAulaRepository;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para AcademiaService
 */
@ExtendWith(MockitoExtension.class)
class AcademiaServiceTest {

    @Mock
    private AcademiaRepository academiaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ProfesorRepository profesorRepository;

    @Mock
    private AlumnoRepository alumnoRepository;

    @Mock
    private AulaRepository aulaRepository;

    @Mock
    private ReservaAulaRepository reservaAulaRepository;

    @InjectMocks
    private AcademiaService academiaService;

    private Academia academia;

    @BeforeEach
    void setUp() {
        academia = new Academia();
        academia.setId(1L);
        academia.setNombre("Academia Test");
        academia.setActiva(true);
        academia.setFechaAlta(LocalDateTime.now());
        academia.setNifCif("B12345678");
        academia.setEmailContacto("contacto@academia.com");
        academia.setTelefono("912345678");
        academia.setDireccion("Calle Test 123");
    }

    @Test
    @DisplayName("listarActivasParaRegistro retorna academias activas")
    void listarActivasParaRegistro() {
        List<Academia> academias = Arrays.asList(academia);
        when(academiaRepository.findByActivaTrue()).thenReturn(academias);

        List<Academia> resultado = academiaService.listarActivasParaRegistro();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActiva());
        verify(academiaRepository, times(1)).findByActivaTrue();
    }

    @Test
    @DisplayName("obtenerPorIdParaRegistro retorna academia activa")
    void obtenerPorIdParaRegistro() {
        when(academiaRepository.findById(1L)).thenReturn(Optional.of(academia));

        Academia resultado = academiaService.obtenerPorIdParaRegistro(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertTrue(resultado.getActiva());
        verify(academiaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorIdParaRegistro lanza excepción si academia no existe")
    void obtenerPorIdParaRegistroNoExiste() {
        when(academiaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            academiaService.obtenerPorIdParaRegistro(99L);
        });
    }

    @Test
    @DisplayName("obtenerPorIdParaRegistro lanza excepción si academia está inactiva")
    void obtenerPorIdParaRegistroInactiva() {
        academia.setActiva(false);
        when(academiaRepository.findById(1L)).thenReturn(Optional.of(academia));

        assertThrows(IllegalArgumentException.class, () -> {
            academiaService.obtenerPorIdParaRegistro(1L);
        });
    }

    @Test
    @DisplayName("crear guarda academia nueva")
    void crear() {
        Academia nuevaAcademia = new Academia();
        nuevaAcademia.setNombre("Nueva Academia");

        when(academiaRepository.save(any(Academia.class))).thenAnswer(invocation -> {
            Academia a = invocation.getArgument(0);
            a.setId(2L);
            return a;
        });

        Academia resultado = academiaService.crear(nuevaAcademia);

        assertNotNull(resultado);
        assertEquals("Nueva Academia", resultado.getNombre());
        assertNotNull(resultado.getFechaAlta());
        assertTrue(resultado.getActiva());
        verify(academiaRepository, times(1)).save(any(Academia.class));
    }

    @Test
    @DisplayName("crear establece fecha de alta si es null")
    void crearEstableceFechaAlta() {
        Academia nuevaAcademia = new Academia();
        nuevaAcademia.setNombre("Academia Sin Fecha");
        nuevaAcademia.setFechaAlta(null);

        when(academiaRepository.save(any(Academia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Academia resultado = academiaService.crear(nuevaAcademia);

        assertNotNull(resultado.getFechaAlta());
    }

    @Test
    @DisplayName("crear establece activa si es null")
    void crearEstableceActiva() {
        Academia nuevaAcademia = new Academia();
        nuevaAcademia.setNombre("Academia Sin Estado");
        nuevaAcademia.setActiva(null);

        when(academiaRepository.save(any(Academia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Academia resultado = academiaService.crear(nuevaAcademia);

        assertTrue(resultado.getActiva());
    }

    @Test
    @DisplayName("actualizar modifica datos de academia existente")
    void actualizar() {
        Academia academiaActualizada = new Academia();
        academiaActualizada.setNombre("Academia Actualizada");
        academiaActualizada.setNifCif("B87654321");
        academiaActualizada.setEmailContacto("nuevo@academia.com");
        academiaActualizada.setTelefono("987654321");
        academiaActualizada.setDireccion("Nueva Dirección 456");

        when(academiaRepository.findById(1L)).thenReturn(Optional.of(academia));
        when(academiaRepository.save(any(Academia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Academia resultado = academiaService.actualizar(1L, academiaActualizada);

        assertEquals("Academia Actualizada", resultado.getNombre());
        assertEquals("B87654321", resultado.getNifCif());
        assertEquals("nuevo@academia.com", resultado.getEmailContacto());
        verify(academiaRepository, times(1)).save(any(Academia.class));
    }

    @Test
    @DisplayName("actualizar lanza excepción si academia no existe")
    void actualizarNoExiste() {
        when(academiaRepository.findById(99L)).thenReturn(Optional.empty());

        Academia academiaActualizada = new Academia();
        academiaActualizada.setNombre("Test");

        assertThrows(IllegalArgumentException.class, () -> {
            academiaService.actualizar(99L, academiaActualizada);
        });
    }

    @Test
    @DisplayName("activar cambia estado a activo")
    void activar() {
        academia.setActiva(false);
        when(academiaRepository.findById(1L)).thenReturn(Optional.of(academia));
        when(academiaRepository.save(any(Academia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        academiaService.activar(1L);

        assertTrue(academia.getActiva());
        verify(academiaRepository, times(1)).save(academia);
    }

    @Test
    @DisplayName("desactivar cambia estado a inactivo")
    void desactivar() {
        when(academiaRepository.findById(1L)).thenReturn(Optional.of(academia));
        when(academiaRepository.save(any(Academia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        academiaService.desactivar(1L);

        assertFalse(academia.getActiva());
        verify(academiaRepository, times(1)).save(academia);
    }
}
