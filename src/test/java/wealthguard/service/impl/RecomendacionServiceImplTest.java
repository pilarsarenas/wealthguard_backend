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

import wealthguard.dto.RecomendacionResponseDTO;
import wealthguard.entity.RecomendacionEntity;
import wealthguard.entity.TipoRecomendacionEntity;
import wealthguard.entity.UsuarioEntity;
import wealthguard.mapper.RecomendacionMapper;
import wealthguard.repository.RecomendacionRepository;
import wealthguard.repository.TipoRecomendacionRepository;
import wealthguard.repository.UsuarioRepository;
import wealthguard.service.LoginService;

@ExtendWith(MockitoExtension.class)
class RecomendacionServiceImplTest {

    @Mock
    private RecomendacionRepository recomendacionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoRecomendacionRepository tipoRecomendacionRepository;

    @Mock
    private RecomendacionMapper recomendacionMapper;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private RecomendacionServiceImpl recomendacionService;

    private UsuarioEntity usuarioEntity;
    private TipoRecomendacionEntity tipoRecomendacion;
    private RecomendacionEntity recomendacionVigente;
    private RecomendacionEntity recomendacionAntigua;
    private RecomendacionResponseDTO recomendacionResponseDTO;

    @BeforeEach
    void setUp() {
        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1);
        usuarioEntity.setNickUsuario("testuser");

        tipoRecomendacion = new TipoRecomendacionEntity();
        tipoRecomendacion.setId(1);
        tipoRecomendacion.setNombre("Ahorro óptimo");
        tipoRecomendacion.setMensaje("Estás ahorrando bien.");
        tipoRecomendacion.setScoreMinimo(600);
        tipoRecomendacion.setScoreMaximo(800);

        recomendacionVigente = new RecomendacionEntity();
        recomendacionVigente.setId(10);
        recomendacionVigente.setUsuario(usuarioEntity);
        recomendacionVigente.setTipoRecomendacion(tipoRecomendacion);
        recomendacionVigente.setFechaRecomendacion(LocalDateTime.now().minusDays(1));

        recomendacionAntigua = new RecomendacionEntity();
        recomendacionAntigua.setId(5);
        recomendacionAntigua.setUsuario(usuarioEntity);
        recomendacionAntigua.setTipoRecomendacion(tipoRecomendacion);
        recomendacionAntigua.setFechaRecomendacion(LocalDateTime.now().minusMonths(2));

        recomendacionResponseDTO = new RecomendacionResponseDTO();
        recomendacionResponseDTO.setIdRecomendacion(10);
        recomendacionResponseDTO.setTitulo("Ahorro óptimo");
    }

    // --- generarRecomendaciones ---

    @Test
    void generarRecomendaciones_scoreMismoRango_retornaExistentes() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(recomendacionRepository.findByUsuarioIdOrderByFechaRecomendacionDesc(1))
                .thenReturn(Arrays.asList(recomendacionVigente));
        when(recomendacionMapper.convertirADTO(recomendacionVigente)).thenReturn(recomendacionResponseDTO);

        int scoreEnMismoRango = 700;
        List<RecomendacionResponseDTO> result = recomendacionService.generarRecomendaciones(
                1, scoreEnMismoRango, "testuser", "pass");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tipoRecomendacionRepository, never()).findByScore(any());
        verify(recomendacionRepository, never()).save(any());
    }

    @Test
    void generarRecomendaciones_scoreCambiaRango_creaRecomendacionNueva() {
        doNothing().when(loginService).verificar(anyString(), anyString());

        TipoRecomendacionEntity tipoNuevo = new TipoRecomendacionEntity();
        tipoNuevo.setId(2);
        tipoNuevo.setNombre("Riesgo elevado");
        tipoNuevo.setMensaje("Debes reducir gastos.");
        tipoNuevo.setScoreMinimo(200);
        tipoNuevo.setScoreMaximo(400);

        RecomendacionEntity nuevaRec = new RecomendacionEntity();
        nuevaRec.setId(11);
        nuevaRec.setUsuario(usuarioEntity);
        nuevaRec.setTipoRecomendacion(tipoNuevo);
        nuevaRec.setFechaRecomendacion(LocalDateTime.now());

        RecomendacionResponseDTO nuevaDTO = new RecomendacionResponseDTO();
        nuevaDTO.setIdRecomendacion(11);

        when(recomendacionRepository.findByUsuarioIdOrderByFechaRecomendacionDesc(1))
                .thenReturn(Arrays.asList(recomendacionVigente))
                .thenReturn(Arrays.asList(nuevaRec, recomendacionVigente));
        when(tipoRecomendacionRepository.findByScore(300)).thenReturn(Arrays.asList(tipoNuevo));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));
        when(recomendacionRepository.save(any(RecomendacionEntity.class))).thenReturn(nuevaRec);
        when(recomendacionMapper.convertirADTO(nuevaRec)).thenReturn(nuevaDTO);
        when(recomendacionMapper.convertirADTO(recomendacionVigente)).thenReturn(recomendacionResponseDTO);

        List<RecomendacionResponseDTO> result = recomendacionService.generarRecomendaciones(
                1, 300, "testuser", "pass");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(recomendacionRepository).save(any(RecomendacionEntity.class));
    }

    @Test
    void generarRecomendaciones_sinExistentes_creaRecomendacionNueva() {
        doNothing().when(loginService).verificar(anyString(), anyString());

        RecomendacionEntity nuevaRec = new RecomendacionEntity();
        nuevaRec.setId(1);
        nuevaRec.setUsuario(usuarioEntity);
        nuevaRec.setTipoRecomendacion(tipoRecomendacion);

        RecomendacionResponseDTO nuevaDTO = new RecomendacionResponseDTO();
        nuevaDTO.setIdRecomendacion(1);

        when(recomendacionRepository.findByUsuarioIdOrderByFechaRecomendacionDesc(1))
                .thenReturn(Collections.emptyList())
                .thenReturn(Arrays.asList(nuevaRec));
        when(tipoRecomendacionRepository.findByScore(700)).thenReturn(Arrays.asList(tipoRecomendacion));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));
        when(recomendacionRepository.save(any(RecomendacionEntity.class))).thenReturn(nuevaRec);
        when(recomendacionMapper.convertirADTO(nuevaRec)).thenReturn(nuevaDTO);

        List<RecomendacionResponseDTO> result = recomendacionService.generarRecomendaciones(
                1, 700, "testuser", "pass");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void generarRecomendaciones_sinCandidatos_retornaExistentes() {
        doNothing().when(loginService).verificar(anyString(), anyString());

        TipoRecomendacionEntity tipoFueraDe = new TipoRecomendacionEntity();
        tipoFueraDe.setScoreMinimo(800);
        tipoFueraDe.setScoreMaximo(1000);

        when(recomendacionRepository.findByUsuarioIdOrderByFechaRecomendacionDesc(1))
                .thenReturn(Arrays.asList(recomendacionVigente));
        when(tipoRecomendacionRepository.findByScore(150)).thenReturn(Collections.emptyList());
        when(recomendacionMapper.convertirADTO(recomendacionVigente)).thenReturn(recomendacionResponseDTO);

        List<RecomendacionResponseDTO> result = recomendacionService.generarRecomendaciones(
                1, 150, "testuser", "pass");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recomendacionRepository, never()).save(any());
    }

    @Test
    void generarRecomendaciones_seleccionaMasEspecifico() {
        doNothing().when(loginService).verificar(anyString(), anyString());

        TipoRecomendacionEntity tipoAmplioRango = new TipoRecomendacionEntity();
        tipoAmplioRango.setId(3);
        tipoAmplioRango.setNombre("Rango amplio");
        tipoAmplioRango.setMensaje("Descripción genérica.");
        tipoAmplioRango.setScoreMinimo(0);
        tipoAmplioRango.setScoreMaximo(1000);

        TipoRecomendacionEntity tipoEstrechoRango = new TipoRecomendacionEntity();
        tipoEstrechoRango.setId(4);
        tipoEstrechoRango.setNombre("Rango estrecho");
        tipoEstrechoRango.setMensaje("Descripción específica.");
        tipoEstrechoRango.setScoreMinimo(490);
        tipoEstrechoRango.setScoreMaximo(510);

        when(recomendacionRepository.findByUsuarioIdOrderByFechaRecomendacionDesc(1))
                .thenReturn(Collections.emptyList())
                .thenReturn(Collections.emptyList());
        when(tipoRecomendacionRepository.findByScore(500))
                .thenReturn(Arrays.asList(tipoAmplioRango, tipoEstrechoRango));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));
        when(recomendacionRepository.save(any(RecomendacionEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(recomendacionMapper.convertirADTO(any())).thenReturn(recomendacionResponseDTO);

        recomendacionService.generarRecomendaciones(1, 500, "testuser", "pass");

        verify(recomendacionRepository).save(argThat(r -> r.getTipoRecomendacion().getId().equals(4)));
    }

    // --- obtenerRecomendaciones ---

    @Test
    void obtenerRecomendaciones_conDatos_retornaLista() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(recomendacionRepository.findByUsuarioIdOrderByFechaRecomendacionDesc(1))
                .thenReturn(Arrays.asList(recomendacionVigente, recomendacionAntigua));
        when(recomendacionMapper.convertirADTO(recomendacionVigente)).thenReturn(recomendacionResponseDTO);
        when(recomendacionMapper.convertirADTO(recomendacionAntigua)).thenReturn(new RecomendacionResponseDTO());

        List<RecomendacionResponseDTO> result = recomendacionService.obtenerRecomendaciones(1, "testuser", "pass");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void obtenerRecomendaciones_sinDatos_retornaListaVacia() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(recomendacionRepository.findByUsuarioIdOrderByFechaRecomendacionDesc(1))
                .thenReturn(Collections.emptyList());

        List<RecomendacionResponseDTO> result = recomendacionService.obtenerRecomendaciones(1, "testuser", "pass");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- eliminarRecomendacion ---

    @Test
    void eliminarRecomendacion_esAntigua_eliminaYRetornaTrue() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(recomendacionRepository.findById(5)).thenReturn(Optional.of(recomendacionAntigua));
        when(recomendacionRepository.findByUsuarioIdOrderByFechaRecomendacionDesc(1))
                .thenReturn(Arrays.asList(recomendacionVigente, recomendacionAntigua));

        boolean result = recomendacionService.eliminarRecomendacion(5, "testuser", "pass");

        assertTrue(result);
        verify(recomendacionRepository).deleteById(5);
    }

    @Test
    void eliminarRecomendacion_esLaVigente_retornaFalse() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(recomendacionRepository.findById(10)).thenReturn(Optional.of(recomendacionVigente));
        when(recomendacionRepository.findByUsuarioIdOrderByFechaRecomendacionDesc(1))
                .thenReturn(Arrays.asList(recomendacionVigente, recomendacionAntigua));

        boolean result = recomendacionService.eliminarRecomendacion(10, "testuser", "pass");

        assertFalse(result);
        verify(recomendacionRepository, never()).deleteById(any());
    }

    @Test
    void eliminarRecomendacion_noExiste_retornaFalse() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(recomendacionRepository.findById(999)).thenReturn(Optional.empty());

        boolean result = recomendacionService.eliminarRecomendacion(999, "testuser", "pass");

        assertFalse(result);
        verify(recomendacionRepository, never()).deleteById(any());
    }

    @Test
    void eliminarRecomendacion_unica_esVigente_retornaFalse() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(recomendacionRepository.findById(10)).thenReturn(Optional.of(recomendacionVigente));
        when(recomendacionRepository.findByUsuarioIdOrderByFechaRecomendacionDesc(1))
                .thenReturn(Arrays.asList(recomendacionVigente));

        boolean result = recomendacionService.eliminarRecomendacion(10, "testuser", "pass");

        assertFalse(result);
    }
}
