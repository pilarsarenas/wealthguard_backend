package wealthguard.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wealthguard.mapper.UsuarioMapper;
import wealthguard.repository.ObjetivoRepository;
import wealthguard.repository.PresupuestoRepository;
import wealthguard.repository.RecomendacionRepository;
import wealthguard.repository.ScoreFinancieroRepository;
import wealthguard.repository.TransaccionRepository;
import wealthguard.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private RecomendacionRepository recomendacionRepository;

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @Mock
    private ObjetivoRepository objetivoRepository;

    @Mock
    private ScoreFinancieroRepository scoreFinancieroRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void eliminarCuentaDebeBorrarDependenciasAntesDelUsuario() {
        when(usuarioRepository.existsById(7)).thenReturn(true);

        boolean eliminado = usuarioService.eliminarCuenta(7);

        assertTrue(eliminado);

        var orden = inOrder(
                transaccionRepository,
                recomendacionRepository,
                presupuestoRepository,
                objetivoRepository,
                scoreFinancieroRepository,
                usuarioRepository);

        orden.verify(transaccionRepository).deleteByUsuarioId(7);
        orden.verify(recomendacionRepository).deleteByUsuarioId(7);
        orden.verify(presupuestoRepository).deleteByUsuarioId(7);
        orden.verify(objetivoRepository).deleteByUsuarioId(7);
        orden.verify(scoreFinancieroRepository).deleteByUsuarioId(7);
        orden.verify(usuarioRepository).deleteById(7);
        verify(usuarioRepository).existsById(7);
        verifyNoMoreInteractions(
                transaccionRepository,
                recomendacionRepository,
                presupuestoRepository,
                objetivoRepository,
                scoreFinancieroRepository,
                usuarioRepository);
    }

    @Test
    void eliminarCuentaCuandoNoExisteNoDebeTocarDependencias() {
        when(usuarioRepository.existsById(99)).thenReturn(false);

        boolean eliminado = usuarioService.eliminarCuenta(99);

        assertFalse(eliminado);
        verify(usuarioRepository).existsById(99);
        verifyNoInteractions(
                transaccionRepository,
                recomendacionRepository,
                presupuestoRepository,
                objetivoRepository,
                scoreFinancieroRepository);
        verify(usuarioRepository, org.mockito.Mockito.never()).deleteById(99);
    }
}