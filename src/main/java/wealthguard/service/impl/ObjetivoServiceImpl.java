package wealthguard.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import wealthguard.dto.ObjetivoRequestDTO;
import wealthguard.dto.ObjetivoResponseDTO;
import wealthguard.entity.ObjetivoEntity;
import wealthguard.mapper.ObjetivoMapper;
import wealthguard.repository.ObjetivoRepository;
import wealthguard.service.IObjetivoService;

@Service
public class ObjetivoServiceImpl implements IObjetivoService {

    @Autowired
    private ObjetivoRepository objetivoRepository;

    @Autowired
    private ObjetivoMapper objetivoMapper;

    @Override
    @Transactional
    public ObjetivoResponseDTO crearObjetivo(ObjetivoRequestDTO objetivoRequestDTO) {

        // Buscamos si ya existe un objetivo para el usuario
        Optional<ObjetivoEntity> objetivoExistente = objetivoRepository.findFirstByUsuarioIdOrderByFechaInicioDesc(objetivoRequestDTO.getUsuarioId());
       
       // Si existe, lo eliminamos
        if (objetivoExistente.isPresent()) {
            objetivoRepository.deleteById(objetivoExistente.get().getId());
        }

        // Creamos el objetivo con las fechas de inicio y fin del mes actual
        ObjetivoEntity objetivoEntity = objetivoMapper.convertirAEntity(objetivoRequestDTO);
        LocalDateTime ahora = LocalDateTime.now();
        objetivoEntity.setFechaInicio(ahora.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN));
        objetivoEntity.setFechaFin(ahora.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX));

        ObjetivoEntity objetivoGuardado = objetivoRepository.save(objetivoEntity);

        return objetivoMapper.convertirADTO(objetivoGuardado);

    }

    @Override
    public boolean eliminarObjetivo(Integer idObjetivo) {
        if (objetivoRepository.existsById(idObjetivo)) {
            objetivoRepository.deleteById(idObjetivo);
            return true;
        } else {
            return false; // No se encontró el objetivo
        }
    }

    @Override
    public ObjetivoResponseDTO editarObjetivo(int idObjetivo, ObjetivoRequestDTO objetivoRequestDTO) {

        ObjetivoEntity objetivoExistente = objetivoRepository.findById(idObjetivo)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el objetivo con ID: " + idObjetivo));

        // Solo modificamos la cantidad. El resto se mantiene igual
        objetivoExistente.setCantidadObjetivo(objetivoRequestDTO.getCantidadObjetivo());

        ObjetivoEntity objetivoGuardado = objetivoRepository.save(objetivoExistente);
        return objetivoMapper.convertirADTO(objetivoGuardado);
    }

    @Override
    public ObjetivoResponseDTO obtenerObjetivo(Integer idUsuario) {
        return objetivoRepository.findFirstByUsuarioIdOrderByFechaInicioDesc(idUsuario)
                .map(objetivoMapper::convertirADTO)
                .orElse(null);
    }

}
