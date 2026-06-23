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

import wealthguard.dto.TransaccionRequestDTO;
import wealthguard.dto.TransaccionResponseDTO;
import wealthguard.entity.CategoriaEntity;
import wealthguard.entity.ObjetivoEntity;
import wealthguard.entity.TransaccionEntity;
import wealthguard.entity.UsuarioEntity;
import wealthguard.mapper.TransaccionMapper;
import wealthguard.repository.CategoriaRepository;
import wealthguard.repository.ObjetivoRepository;
import wealthguard.repository.TransaccionRepository;
import wealthguard.service.LoginService;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceImplTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private ObjetivoRepository objetivoRepository;

    @Mock
    private TransaccionMapper transaccionMapper;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private LoginService loginService;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    private TransaccionEntity transaccionReciente;
    private TransaccionEntity transaccionAntigua;
    private TransaccionResponseDTO transaccionResponseDTO;
    private CategoriaEntity categoriaEntity;
    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        categoriaEntity = new CategoriaEntity();
        categoriaEntity.setId(1);
        categoriaEntity.setNombre("Alimentación");

        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1);

        transaccionReciente = new TransaccionEntity();
        transaccionReciente.setId(1);
        transaccionReciente.setUsuario(usuarioEntity);
        transaccionReciente.setCategoria(categoriaEntity);
        transaccionReciente.setCantidad(100.0);
        transaccionReciente.setFecha(LocalDateTime.now().minusDays(5));
        transaccionReciente.setTipoTransaccion(false);

        transaccionAntigua = new TransaccionEntity();
        transaccionAntigua.setId(2);
        transaccionAntigua.setUsuario(usuarioEntity);
        transaccionAntigua.setCategoria(categoriaEntity);
        transaccionAntigua.setCantidad(200.0);
        transaccionAntigua.setFecha(LocalDateTime.now().minusMonths(5));
        transaccionAntigua.setTipoTransaccion(false);

        transaccionResponseDTO = new TransaccionResponseDTO();
        transaccionResponseDTO.setId(1);
        transaccionResponseDTO.setCantidad(100.0);
    }

    // --- listarTransacciones ---

    @Test
    void listarTransacciones_conFechas_retornaLista() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        LocalDateTime inicio = LocalDateTime.now().minusDays(7);
        LocalDateTime fin = LocalDateTime.now();

        when(transaccionRepository.buscarConFiltros(eq(1), eq(inicio), eq(fin), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(Arrays.asList(transaccionReciente));
        when(transaccionMapper.convertirADTO(transaccionReciente)).thenReturn(transaccionResponseDTO);

        List<TransaccionResponseDTO> result = transaccionService.listarTransacciones(
                1, inicio, fin, null, null, null, null, "testuser", "pass");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void listarTransacciones_sinFechas_usaUltimos7Dias() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.buscarConFiltros(anyInt(), any(), any(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(Collections.emptyList());

        List<TransaccionResponseDTO> result = transaccionService.listarTransacciones(
                1, null, null, null, null, null, null, "testuser", "pass");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transaccionRepository).buscarConFiltros(
                eq(1),
                argThat(f -> f.isBefore(LocalDateTime.now().minusDays(6))),
                argThat(f -> f.isBefore(LocalDateTime.now().plusSeconds(1))),
                isNull(), isNull(), isNull(), isNull());
    }

    @Test
    void listarTransacciones_conFiltros_pasaFiltrosAlRepositorio() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        LocalDateTime inicio = LocalDateTime.now().minusDays(30);
        LocalDateTime fin = LocalDateTime.now();

        when(transaccionRepository.buscarConFiltros(eq(1), eq(inicio), eq(fin), eq(1), eq(false), eq(50.0), eq("mercado")))
                .thenReturn(Arrays.asList(transaccionReciente));
        when(transaccionMapper.convertirADTO(transaccionReciente)).thenReturn(transaccionResponseDTO);

        List<TransaccionResponseDTO> result = transaccionService.listarTransacciones(
                1, inicio, fin, 1, false, 50.0, "mercado", "testuser", "pass");

        assertEquals(1, result.size());
    }

    // --- crearTransaccion ---

    @Test
    void crearTransaccion_sinCategoria_guardaYRetornaDTO() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        TransaccionRequestDTO requestDTO = new TransaccionRequestDTO();
        requestDTO.setCantidad(150.0);
        requestDTO.setFecha(LocalDateTime.now());
        requestDTO.setTipoTransaccion(true);

        TransaccionEntity entity = new TransaccionEntity();
        entity.setCantidad(150.0);
        entity.setCategoria(null);

        when(transaccionMapper.convertirAEntity(requestDTO)).thenReturn(entity);
        when(transaccionRepository.save(entity)).thenReturn(entity);
        when(transaccionMapper.convertirADTO(entity)).thenReturn(transaccionResponseDTO);

        TransaccionResponseDTO result = transaccionService.crearTransaccion(requestDTO, "testuser", "pass");

        assertNotNull(result);
        verify(transaccionRepository).save(entity);
    }

    @Test
    void crearTransaccion_conCategoria_cargaCategoriaRealYGuarda() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        TransaccionRequestDTO requestDTO = new TransaccionRequestDTO();
        requestDTO.setCantidad(100.0);
        requestDTO.setIdCategoria(1);

        TransaccionEntity entity = new TransaccionEntity();
        entity.setCantidad(100.0);
        entity.setCategoria(categoriaEntity);

        when(transaccionMapper.convertirAEntity(requestDTO)).thenReturn(entity);
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaEntity));
        when(transaccionRepository.save(entity)).thenReturn(entity);
        when(transaccionMapper.convertirADTO(entity)).thenReturn(transaccionResponseDTO);

        TransaccionResponseDTO result = transaccionService.crearTransaccion(requestDTO, "testuser", "pass");

        assertNotNull(result);
        verify(categoriaRepository).findById(1);
    }

    @Test
    void crearTransaccion_categoriaNoEncontrada_lanzaRuntimeException() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        TransaccionRequestDTO requestDTO = new TransaccionRequestDTO();
        requestDTO.setIdCategoria(999);

        TransaccionEntity entity = new TransaccionEntity();
        CategoriaEntity categoriaRef = new CategoriaEntity();
        categoriaRef.setId(999);
        entity.setCategoria(categoriaRef);

        when(transaccionMapper.convertirAEntity(requestDTO)).thenReturn(entity);
        when(categoriaRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> transaccionService.crearTransaccion(requestDTO, "testuser", "pass"));
    }

    // --- editarTransaccion ---

    @Test
    void editarTransaccion_reciente_actualizaYRetornaDTO() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        TransaccionRequestDTO requestDTO = new TransaccionRequestDTO();
        requestDTO.setCantidad(200.0);

        TransaccionEntity actualizada = new TransaccionEntity();
        actualizada.setId(1);
        actualizada.setCantidad(200.0);

        TransaccionResponseDTO responseActualizado = new TransaccionResponseDTO();
        responseActualizado.setId(1);
        responseActualizado.setCantidad(200.0);

        when(transaccionRepository.findById(1)).thenReturn(Optional.of(transaccionReciente));
        when(transaccionMapper.convertirAEntity(requestDTO)).thenReturn(actualizada);
        when(transaccionRepository.save(actualizada)).thenReturn(actualizada);
        when(transaccionMapper.convertirADTO(actualizada)).thenReturn(responseActualizado);

        TransaccionResponseDTO result = transaccionService.editarTransaccion(1, requestDTO, "testuser", "pass");

        assertNotNull(result);
        assertEquals(200.0, result.getCantidad());
    }

    @Test
    void editarTransaccion_noEncontrada_lanzaRuntimeException() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> transaccionService.editarTransaccion(999, new TransaccionRequestDTO(), "testuser", "pass"));
    }

    @Test
    void editarTransaccion_masDesTresMeses_lanzaRuntimeException() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.findById(2)).thenReturn(Optional.of(transaccionAntigua));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> transaccionService.editarTransaccion(2, new TransaccionRequestDTO(), "testuser", "pass"));
        assertTrue(ex.getMessage().contains("3 meses"));
    }

    // --- eliminarTransaccion ---

    @Test
    void eliminarTransaccion_reciente_eliminaYRetornaTrue() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.findById(1)).thenReturn(Optional.of(transaccionReciente));

        boolean result = transaccionService.eliminarTransaccion(1, "testuser", "pass");

        assertTrue(result);
        verify(transaccionRepository).deleteById(1);
    }

    @Test
    void eliminarTransaccion_noEncontrada_retornaFalse() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.findById(999)).thenReturn(Optional.empty());

        boolean result = transaccionService.eliminarTransaccion(999, "testuser", "pass");

        assertFalse(result);
        verify(transaccionRepository, never()).deleteById(any());
    }

    @Test
    void eliminarTransaccion_masDesTresMeses_retornaFalse() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.findById(2)).thenReturn(Optional.of(transaccionAntigua));

        boolean result = transaccionService.eliminarTransaccion(2, "testuser", "pass");

        assertFalse(result);
        verify(transaccionRepository, never()).deleteById(any());
    }

    // --- obtenerTendencia ---

    @Test
    void obtenerTendencia_ambiosMesesConDatos_calculaPorcentaje() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.obtenerBalanceEntreFechas(eq(1), any(), any()))
                .thenReturn(1200.0)
                .thenReturn(1000.0);

        double result = transaccionService.obtenerTendencia(1, "testuser", "pass");

        assertEquals(20.0, result, 0.01);
    }

    @Test
    void obtenerTendencia_mesAnteriorCero_balanceActualPositivo_retorna100() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.obtenerBalanceEntreFechas(eq(1), any(), any()))
                .thenReturn(500.0)
                .thenReturn(0.0);

        double result = transaccionService.obtenerTendencia(1, "testuser", "pass");

        assertEquals(100.0, result, 0.01);
    }

    @Test
    void obtenerTendencia_ambiosMesesSinDatos_retornaCero() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.obtenerBalanceEntreFechas(eq(1), any(), any()))
                .thenReturn(null)
                .thenReturn(null);

        double result = transaccionService.obtenerTendencia(1, "testuser", "pass");

        assertEquals(0.0, result, 0.01);
    }

    @Test
    void obtenerTendencia_mesAnteriorCeroBalanceActualCero_retornaCero() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.obtenerBalanceEntreFechas(eq(1), any(), any()))
                .thenReturn(0.0)
                .thenReturn(0.0);

        double result = transaccionService.obtenerTendencia(1, "testuser", "pass");

        assertEquals(0.0, result, 0.01);
    }

    // --- obtenerCategoriaPrincipal ---

    @Test
    void obtenerCategoriaPrincipal_conGastosEsteMes_retornaCategoriaYPorcentaje() {
        doNothing().when(loginService).verificar(anyString(), anyString());

        TransaccionEntity t1 = new TransaccionEntity();
        t1.setTipoTransaccion(false);
        t1.setCantidad(300.0);
        t1.setFecha(LocalDateTime.now().withDayOfMonth(1).plusDays(1));
        t1.setCategoria(categoriaEntity);

        CategoriaEntity cat2 = new CategoriaEntity();
        cat2.setNombre("Transporte");
        TransaccionEntity t2 = new TransaccionEntity();
        t2.setTipoTransaccion(false);
        t2.setCantidad(100.0);
        t2.setFecha(LocalDateTime.now().withDayOfMonth(1).plusDays(1));
        t2.setCategoria(cat2);

        when(transaccionRepository.findByUsuarioId(1)).thenReturn(Arrays.asList(t1, t2));

        String[] result = transaccionService.obtenerCategoriaPrincipal(1, "testuser", "pass");

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("Alimentación", result[0]);
        assertEquals("75,00", result[1]);
    }

    @Test
    void obtenerCategoriaPrincipal_sinGastosEsteMes_retornaSinDatos() {
        doNothing().when(loginService).verificar(anyString(), anyString());

        TransaccionEntity ingreso = new TransaccionEntity();
        ingreso.setTipoTransaccion(true);
        ingreso.setCantidad(500.0);
        ingreso.setFecha(LocalDateTime.now());

        when(transaccionRepository.findByUsuarioId(1)).thenReturn(Arrays.asList(ingreso));

        String[] result = transaccionService.obtenerCategoriaPrincipal(1, "testuser", "pass");

        assertEquals("Sin datos", result[0]);
        assertEquals("0.0", result[1]);
    }

    @Test
    void obtenerCategoriaPrincipal_sinTransacciones_retornaSinDatos() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.findByUsuarioId(1)).thenReturn(Collections.emptyList());

        String[] result = transaccionService.obtenerCategoriaPrincipal(1, "testuser", "pass");

        assertEquals("Sin datos", result[0]);
        assertEquals("0.0", result[1]);
    }

    // --- obtenerMeta ---

    @Test
    void obtenerMeta_sinObjetivo_retornaCeros() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(objetivoRepository.findFirstByUsuarioIdOrderByFechaInicioDesc(1)).thenReturn(Optional.empty());

        double[] result = transaccionService.obtenerMeta(1, "testuser", "pass");

        assertNotNull(result);
        assertEquals(0.0, result[0]);
        assertEquals(0.0, result[1]);
    }

    @Test
    void obtenerMeta_objetivoExpirado_retornaCeros() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        ObjetivoEntity objetivoExpirado = new ObjetivoEntity();
        objetivoExpirado.setCantidadObjetivo(1000.0);
        objetivoExpirado.setFechaInicio(LocalDateTime.now().minusMonths(2));
        objetivoExpirado.setFechaFin(LocalDateTime.now().minusMonths(1));

        when(objetivoRepository.findFirstByUsuarioIdOrderByFechaInicioDesc(1)).thenReturn(Optional.of(objetivoExpirado));

        double[] result = transaccionService.obtenerMeta(1, "testuser", "pass");

        assertEquals(0.0, result[0]);
        assertEquals(0.0, result[1]);
    }

    @Test
    void obtenerMeta_objetivoVigente_calculaProgreso() {
        doNothing().when(loginService).verificar(anyString(), anyString());

        ObjetivoEntity objetivo = new ObjetivoEntity();
        objetivo.setCantidadObjetivo(1000.0);
        objetivo.setFechaInicio(LocalDateTime.now().withDayOfMonth(1));
        objetivo.setFechaFin(LocalDateTime.now().plusMonths(1));

        TransaccionEntity ingreso = new TransaccionEntity();
        ingreso.setTipoTransaccion(true);
        ingreso.setCantidad(600.0);
        ingreso.setFecha(LocalDateTime.now());

        TransaccionEntity gasto = new TransaccionEntity();
        gasto.setTipoTransaccion(false);
        gasto.setCantidad(100.0);
        gasto.setFecha(LocalDateTime.now());

        when(objetivoRepository.findFirstByUsuarioIdOrderByFechaInicioDesc(1)).thenReturn(Optional.of(objetivo));
        when(transaccionRepository.buscarConFiltros(eq(1), any(), any(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(Arrays.asList(ingreso, gasto));

        double[] result = transaccionService.obtenerMeta(1, "testuser", "pass");

        assertEquals(500.0, result[0], 0.01);
        assertEquals(1000.0, result[1], 0.01);
    }

    // --- listarTodasPorUsuario ---

    @Test
    void listarTodasPorUsuario_conDatos_retornaLista() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.findByUsuarioId(1)).thenReturn(Arrays.asList(transaccionReciente));
        when(transaccionMapper.convertirADTO(transaccionReciente)).thenReturn(transaccionResponseDTO);

        List<TransaccionResponseDTO> result = transaccionService.listarTodasPorUsuario(1, "testuser", "pass");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void listarTodasPorUsuario_sinDatos_retornaListaVacia() {
        doNothing().when(loginService).verificar(anyString(), anyString());
        when(transaccionRepository.findByUsuarioId(1)).thenReturn(Collections.emptyList());

        List<TransaccionResponseDTO> result = transaccionService.listarTodasPorUsuario(1, "testuser", "pass");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
