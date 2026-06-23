package wealthguard.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wealthguard.dto.ScoreFinancieroRequestDTO;
import wealthguard.dto.ScoreFinancieroResponseDTO;
import wealthguard.entity.ScoreFinancieroEntity;
import wealthguard.entity.UsuarioEntity;
import wealthguard.repository.ScoreFinancieroRepository;
import wealthguard.service.LoginService;

@ExtendWith(MockitoExtension.class)
class ScoreFinancieroServiceImplTest {

    @Mock
    private ScoreFinancieroRepository repository;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private ScoreFinancieroServiceImpl scoreFinancieroService;

    private UsuarioEntity usuarioEntity;
    private ScoreFinancieroEntity scoreEntity;
    private ScoreFinancieroRequestDTO scoreRequestDTO;

    @BeforeEach
    void setUp() {
        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1);
        usuarioEntity.setNickUsuario("testuser");

        scoreEntity = new ScoreFinancieroEntity();
        scoreEntity.setId(1);
        scoreEntity.setUsuario(usuarioEntity);
        scoreEntity.setValorMaximo(1000);
        scoreEntity.setNivel(3);
        scoreEntity.setFechaCalculo(LocalDateTime.now());

        scoreRequestDTO = new ScoreFinancieroRequestDTO();
        scoreRequestDTO.setUsuario(usuarioEntity);
        scoreRequestDTO.setValorMaximo(1000);
        scoreRequestDTO.setNivel(3);
        scoreRequestDTO.setFechaCalculo(LocalDateTime.now());
    }

    // --- crearScore ---

    @Test
    void crearScore_exitoso_retornaResponseDTO() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(repository.save(any(ScoreFinancieroEntity.class))).thenReturn(scoreEntity);

        ScoreFinancieroResponseDTO result = scoreFinancieroService.crearScore(scoreRequestDTO, "testuser", "pass");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1000, result.getValorMaximo());
        assertEquals(3, result.getNivel());
        verify(repository).save(any(ScoreFinancieroEntity.class));
    }

    @Test
    void crearScore_credencialesInvalidas_lanzaRuntimeException() {
        doThrow(new RuntimeException("Acceso denegado")).when(loginService).verificar(anyString(), anyString());

        assertThrows(RuntimeException.class,
                () -> scoreFinancieroService.crearScore(scoreRequestDTO, "wrong", "wrong"));
        verify(repository, never()).save(any());
    }

    @Test
    void crearScore_datosCompletos_entidadCreada() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(repository.save(any(ScoreFinancieroEntity.class))).thenAnswer(inv -> {
            ScoreFinancieroEntity e = inv.getArgument(0);
            e.setId(99);
            return e;
        });

        ScoreFinancieroResponseDTO result = scoreFinancieroService.crearScore(scoreRequestDTO, "testuser", "pass");

        assertNotNull(result);
        assertEquals(usuarioEntity, result.getUsuario());
        assertEquals(1000, result.getValorMaximo());
        assertEquals(3, result.getNivel());
    }

    // --- obtenerScorePorId ---

    @Test
    void obtenerScorePorId_existe_retornaResponseDTO() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(repository.findById(1)).thenReturn(Optional.of(scoreEntity));

        ScoreFinancieroResponseDTO result = scoreFinancieroService.obtenerScorePorId(1, "testuser", "pass");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1000, result.getValorMaximo());
    }

    @Test
    void obtenerScorePorId_noExiste_retornaNull() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(repository.findById(999)).thenReturn(Optional.empty());

        ScoreFinancieroResponseDTO result = scoreFinancieroService.obtenerScorePorId(999, "testuser", "pass");

        assertNull(result);
    }

    // --- listarTodos ---

    @Test
    void listarTodos_conDatos_retornaLista() {
        doNothing().when(loginService).verificar(anyString(), anyString());

        ScoreFinancieroEntity score2 = new ScoreFinancieroEntity();
        score2.setId(2);
        score2.setUsuario(usuarioEntity);
        score2.setValorMaximo(800);
        score2.setNivel(2);

        when(repository.findAll()).thenReturn(Arrays.asList(scoreEntity, score2));

        List<ScoreFinancieroResponseDTO> result = scoreFinancieroService.listarTodos("testuser", "pass");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void listarTodos_sinDatos_retornaListaVacia() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<ScoreFinancieroResponseDTO> result = scoreFinancieroService.listarTodos("testuser", "pass");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- actualizarScore ---

    @Test
    void actualizarScore_existe_actualizaYRetornaDTO() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(repository.findById(1)).thenReturn(Optional.of(scoreEntity));
        when(repository.save(scoreEntity)).thenReturn(scoreEntity);

        ScoreFinancieroRequestDTO requestActualizado = new ScoreFinancieroRequestDTO();
        requestActualizado.setUsuario(usuarioEntity);
        requestActualizado.setValorMaximo(900);
        requestActualizado.setNivel(4);
        requestActualizado.setFechaCalculo(LocalDateTime.now());

        ScoreFinancieroResponseDTO result = scoreFinancieroService.actualizarScore(1, requestActualizado, "testuser", "pass");

        assertNotNull(result);
        verify(repository).save(argThat(e -> e.getValorMaximo() == 900 && e.getNivel() == 4));
    }

    @Test
    void actualizarScore_noExiste_lanzaRuntimeException() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(repository.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> scoreFinancieroService.actualizarScore(999, scoreRequestDTO, "testuser", "pass"));
        assertEquals("Score no encontrado", ex.getMessage());
    }

    @Test
    void actualizarScore_actualizaTodosLosCampos() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(repository.findById(1)).thenReturn(Optional.of(scoreEntity));
        when(repository.save(any(ScoreFinancieroEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDateTime nuevaFecha = LocalDateTime.of(2026, 1, 15, 10, 0);
        ScoreFinancieroRequestDTO request = new ScoreFinancieroRequestDTO();
        request.setUsuario(usuarioEntity);
        request.setValorMaximo(750);
        request.setNivel(5);
        request.setFechaCalculo(nuevaFecha);

        ScoreFinancieroResponseDTO result = scoreFinancieroService.actualizarScore(1, request, "testuser", "pass");

        assertEquals(750, result.getValorMaximo());
        assertEquals(5, result.getNivel());
        assertEquals(nuevaFecha, result.getFechaCalculo());
    }

    // --- eliminarScore ---

    @Test
    void eliminarScore_llamaDeleteById() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        doNothing().when(repository).deleteById(1);

        scoreFinancieroService.eliminarScore(1, "testuser", "pass");

        verify(repository).deleteById(1);
    }

    @Test
    void eliminarScore_credencialesInvalidas_lanzaRuntimeException() {
        doThrow(new RuntimeException("Acceso denegado")).when(loginService).verificar(anyString(), anyString());

        assertThrows(RuntimeException.class,
                () -> scoreFinancieroService.eliminarScore(1, "wrong", "wrong"));
        verify(repository, never()).deleteById(any());
    }
}
