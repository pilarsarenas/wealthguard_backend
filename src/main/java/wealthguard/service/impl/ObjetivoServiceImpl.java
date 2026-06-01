package wealthguard.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ObjetivoResponseDTO crearObjetivo(ObjetivoRequestDTO objetivoRequestDTO) {

        ObjetivoEntity objetivoEntity = objetivoMapper.convertirAEntity(objetivoRequestDTO);

        if(objetivoEntity.getFechaFin().isBefore(objetivoEntity.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        ObjetivoEntity objetivoGuardado = objetivoRepository.save(objetivoEntity);

        return objetivoMapper.convertirADTO(objetivoGuardado);

    }
    
    @Override
    public boolean eliminarObjetivo(Integer idObjetivo) {
        if(objetivoRepository.existsById(idObjetivo)){
            objetivoRepository.deleteById(idObjetivo);
            return true;
        } else {
            return false; // No se encontró el objetivo
        }
    }

    @Override
    public ObjetivoResponseDTO editarObjetivo(int idObjetivo, ObjetivoRequestDTO objetivoRequestDTO) {

        // Verificar que el objetivo exista antes de intentar editarlo
        if (!objetivoRepository.existsById(idObjetivo)) {
            throw new IllegalArgumentException("No se encontró el objetivo con ID: " + idObjetivo);
        }

        ObjetivoEntity objetivoActualizado = objetivoMapper.convertirAEntity(objetivoRequestDTO);
        objetivoActualizado.setId(idObjetivo); // Aseguramos que el ID se mantenga igual

        if(objetivoActualizado.getFechaFin().isBefore(objetivoActualizado.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        } else {
             ObjetivoEntity objetivoGuardado = objetivoRepository.save(objetivoActualizado);
             return objetivoMapper.convertirADTO(objetivoGuardado);
        }
    }

    @Override
    public java.util.List<ObjetivoEntity> obtenerObjetivos(Integer idUsuario) {
        return null;
    }

    @Override
    public boolean cambiarEstadoCompletado(Integer idObjetivo, Boolean completado) {
        return false;
    }

}
