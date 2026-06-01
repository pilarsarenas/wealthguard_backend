package wealthguard.mapper;
import org.springframework.stereotype.Component;

import wealthguard.dto.ScoreFinancieroRequestDTO;
import wealthguard.dto.ScoreFinancieroResponseDTO;
import wealthguard.entity.ScoreFinancieroEntity;

@Component
public class ScoreFinancieroMapper {

    public static ScoreFinancieroEntity toEntity(ScoreFinancieroRequestDTO dto) {
        if (dto == null) return null;
        
        ScoreFinancieroEntity entity = new ScoreFinancieroEntity();
        entity.setUsuario(dto.getUsuario());
        entity.setValorMaximo(dto.getValorMaximo());
        entity.setNivel(dto.getNivel());
        entity.setFechaCalculo(dto.getFechaCalculo());
        return entity;
    }

    public static ScoreFinancieroResponseDTO convertirADTO(ScoreFinancieroEntity entity) {
        if (entity == null) return null;
        
        ScoreFinancieroResponseDTO dto = new ScoreFinancieroResponseDTO();
        dto.setId(entity.getId());
        dto.setUsuario(entity.getUsuario());
        dto.setValorMaximo(entity.getValorMaximo());
        dto.setNivel(entity.getNivel());
        dto.setFechaCalculo(entity.getFechaCalculo());
        return dto;
    }
}
