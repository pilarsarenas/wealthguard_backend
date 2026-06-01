package wealthguard.mapper;

import org.springframework.stereotype.Component;

import wealthguard.dto.ObjetivoRequestDTO;
import wealthguard.dto.ObjetivoResponseDTO;
import wealthguard.entity.ObjetivoEntity;

@Component
public class ObjetivoMapper {

    public ObjetivoEntity convertirAEntity (ObjetivoRequestDTO objetivoRequestDTO){
        if (objetivoRequestDTO == null) {
            return null;
        }

        ObjetivoEntity objetivoEntity = new ObjetivoEntity();
        
        objetivoEntity.setUsuarioId(objetivoRequestDTO.getUsuarioId());
        objetivoEntity.setCantidadObjetivo(objetivoRequestDTO.getCantidadObjetivo());
        objetivoEntity.setCategoriaId(objetivoRequestDTO.getCategoriaId());
        objetivoEntity.setFechaInicio(objetivoRequestDTO.getFechaInicio());
        objetivoEntity.setFechaFin(objetivoRequestDTO.getFechaFin());
        objetivoEntity.setCompletado(objetivoRequestDTO.getCompletado());

        return objetivoEntity;
    }

    public ObjetivoResponseDTO convertirADTO (ObjetivoEntity objetivoEntity){
        if (objetivoEntity == null) {
            return null;
        }

        ObjetivoResponseDTO objetivoResponseDTO = new ObjetivoResponseDTO();

        objetivoResponseDTO.setUsuarioId(objetivoEntity.getUsuarioId());
        objetivoResponseDTO.setCantidadObjetivo(objetivoEntity.getCantidadObjetivo());
        objetivoResponseDTO.setCategoriaId(objetivoEntity.getCategoria());
        objetivoResponseDTO.setFechaInicio(objetivoEntity.getFechaInicio());
        objetivoResponseDTO.setFechaFin(objetivoEntity.getFechaFin());
        objetivoResponseDTO.setCompletado(objetivoEntity.getCompletado());

        return objetivoResponseDTO;
    }

}
