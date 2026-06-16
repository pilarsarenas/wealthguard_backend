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

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioMes = ahora.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        LocalDateTime finMes = ahora.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

        // Buscamos si ya existe un objetivo para el usuario
        Optional<ObjetivoEntity> objetivoExistente = objetivoRepository.findFirstByUsuarioIdOrderByFechaInicioDesc(objetivoRequestDTO.getUsuarioId());
       
       // Si existe y pertenece al mes actual lo sobreescrimos
        if (objetivoExistente.isPresent()) {
            ObjetivoEntity ultimoObjetivo = objetivoExistente.get();

            if (!ultimoObjetivo.getFechaFin().isBefore(inicioMes) && !ultimoObjetivo.getFechaInicio().isAfter(finMes)) {
                ultimoObjetivo.setCantidadObjetivo(objetivoRequestDTO.getCantidadObjetivo());
                return objetivoMapper.convertirADTO(objetivoRepository.save(ultimoObjetivo));
                
            }
        }

        // Si no existe o no pertenece al mes actual lo creamos
        ObjetivoEntity objetivoEntity = objetivoMapper.convertirAEntity(objetivoRequestDTO);
        objetivoEntity.setFechaInicio(inicioMes);
        objetivoEntity.setFechaFin(finMes);

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

    // Busca el objetivo de un usuario cuya fecha de fin haya pasado
    @Override
    public ObjetivoResponseDTO obtenerUltimoObjetivo(Integer idUsuario) {
        return objetivoRepository.findFirstByUsuarioIdAndFechaFinBeforeOrderByFechaFinDesc(idUsuario, LocalDateTime.now())
                .map(objetivoMapper::convertirADTO)
                .orElse(null);
    }


}
