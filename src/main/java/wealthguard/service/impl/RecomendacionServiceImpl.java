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

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idUsuario));

        // Buscamos la recomendación más reciente ya guardada, si existe.
        List<RecomendacionEntity> existentes = recomendacionRepository
                .findByUsuarioIdOrderByFechaRecomendacionDesc(idUsuario);

        if (!existentes.isEmpty()) {
            TipoRecomendacionEntity tipoActual = existentes.get(0).getTipoRecomendacion();
            boolean scoreSigueEnMismoRango = score >= tipoActual.getScoreMinimo()
                    && score <= tipoActual.getScoreMaximo();

            // Si el score sigue cayendo en el mismo rango que la última vez,
            // no regeneramos nada: devolvemos lo que ya había.
            if (scoreSigueEnMismoRango) {
                return existentes.stream()
                        .map(recomendacionMapper::convertirADTO)
                        .collect(Collectors.toList());
            }
        }

        // El score cambió de rango (o es la primera evaluación): regeneramos.
        recomendacionRepository.deleteByUsuarioId(idUsuario);

        List<TipoRecomendacionEntity> candidatos = tipoRecomendacionRepository.findByScore(score);
        TipoRecomendacionEntity tipoMasEspecifico = seleccionarMasEspecifico(candidatos);

        if (tipoMasEspecifico == null) {
            return List.of();
        }

        RecomendacionEntity r = new RecomendacionEntity();
        r.setUsuario(usuario);
        r.setTipoRecomendacion(tipoMasEspecifico);
        r.setFechaRecomendacion(LocalDateTime.now());

        RecomendacionEntity guardada = recomendacionRepository.save(r);

        return List.of(recomendacionMapper.convertirADTO(guardada));
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
        if (!recomendacionRepository.existsById(idRecomendacion))
            return false;
        recomendacionRepository.deleteById(idRecomendacion);
        return true;
    }
}