package wealthguard.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<RecomendacionResponseDTO> generarRecomendaciones(int idUsuario, int score) {

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idUsuario));

        List<TipoRecomendacionEntity> tipos = tipoRecomendacionRepository.findByScore(score);

        LocalDateTime ahora = LocalDateTime.now();

        List<RecomendacionEntity> nuevas = tipos.stream().map(tipo -> {
            RecomendacionEntity r = new RecomendacionEntity();
            r.setUsuario(usuario);
            r.setTipoRecomendacion(tipo);
            r.setFechaRecomendacion(ahora);
            return r;
        }).collect(Collectors.toList());

        return recomendacionRepository.saveAll(nuevas).stream()
                .map(recomendacionMapper::convertirADTO)
                .collect(Collectors.toList());
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