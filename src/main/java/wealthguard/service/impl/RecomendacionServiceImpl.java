package wealthguard.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wealthguard.dto.RecomendacionResponseDTO;
import wealthguard.entity.RecomendacionEntity;
import wealthguard.entity.TipoRecomendacionEntity;
import wealthguard.entity.UsuarioEntity;
import wealthguard.mapper.RecomendacionMapper;
import wealthguard.repository.RecomendacionRepository;
import wealthguard.repository.TipoRecomendacionRepository;
import wealthguard.repository.UsuarioRepository;
import wealthguard.service.IRecomendacionService;

@Service
public class RecomendacionServiceImpl implements IRecomendacionService {

    @Autowired
    private RecomendacionRepository recomendacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoRecomendacionRepository tipoRecomendacionRepository;

    @Autowired
    private RecomendacionMapper recomendacionMapper;

    @Override
    @Transactional
    public List<RecomendacionResponseDTO> generarRecomendaciones(int idUsuario, int score) {

        List<RecomendacionEntity> existentes = recomendacionRepository
                .findByUsuarioIdOrderByFechaRecomendacionDesc(idUsuario);

        if (!existentes.isEmpty()) {
            TipoRecomendacionEntity tipoActual = existentes.get(0).getTipoRecomendacion();
            boolean scoreSigueEnMismoRango = score >= tipoActual.getScoreMinimo()
                    && score <= tipoActual.getScoreMaximo();

            // Si el score sigue cayendo en el mismo rango que la última vez,
            // no generamos una fila nueva: devolvemos el historial tal cual está.
            if (scoreSigueEnMismoRango) {
                return existentes.stream()
                        .map(recomendacionMapper::convertirADTO)
                        .collect(Collectors.toList());
            }
        }

        // El score cambió de rango (o es la primera evaluación): añadimos una
        // nueva entrada al historial SIN borrar las anteriores.
        List<TipoRecomendacionEntity> candidatos = tipoRecomendacionRepository.findByScore(score);
        TipoRecomendacionEntity tipoMasEspecifico = seleccionarMasEspecifico(candidatos);

        if (tipoMasEspecifico == null) {
            return existentes.stream()
                    .map(recomendacionMapper::convertirADTO)
                    .collect(Collectors.toList());
        }

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idUsuario));

        RecomendacionEntity r = new RecomendacionEntity();
        r.setUsuario(usuario);
        r.setTipoRecomendacion(tipoMasEspecifico);
        r.setFechaRecomendacion(LocalDateTime.now());

        recomendacionRepository.save(r);

        // Devolvemos el historial completo ya actualizado (la nueva queda primera).
        List<RecomendacionEntity> actualizadas = recomendacionRepository
                .findByUsuarioIdOrderByFechaRecomendacionDesc(idUsuario);

        return actualizadas.stream()
                .map(recomendacionMapper::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * De entre los tipos cuyo rango incluye el score, devuelve el que tiene
     * el rango más estrecho (scoreMaximo - scoreMinimo más pequeño), es decir,
     * el más específico para esa puntuación concreta.
     */
    private TipoRecomendacionEntity seleccionarMasEspecifico(List<TipoRecomendacionEntity> candidatos) {
        return candidatos.stream()
                .min(Comparator.comparingInt(t -> t.getScoreMaximo() - t.getScoreMinimo()))
                .orElse(null);
    }

    @Override
    public List<RecomendacionResponseDTO> obtenerRecomendaciones(int idUsuario) {
        return recomendacionRepository
                .findByUsuarioIdOrderByFechaRecomendacionDesc(idUsuario)
                .stream()
                .map(recomendacionMapper::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean eliminarRecomendacion(int idRecomendacion) {
        RecomendacionEntity recomendacion = recomendacionRepository.findById(idRecomendacion).orElse(null);
        if (recomendacion == null) {
            return false;
        }

        int idUsuario = recomendacion.getUsuario().getId();
        List<RecomendacionEntity> historico = recomendacionRepository
                .findByUsuarioIdOrderByFechaRecomendacionDesc(idUsuario);

        boolean esLaVigente = !historico.isEmpty()
                && historico.get(0).getId().equals(idRecomendacion);

        if (esLaVigente) {
            // no se permite eliminar la recomendación vigente del usuario
            return false;
        }

        recomendacionRepository.deleteById(idRecomendacion);
        return true;
    }
}