package wealthguard.mapper;
import org.springframework.stereotype.Component;

import wealthguard.dto.TipoRecomendacionRequestDTO;
import wealthguard.dto.TipoRecomendacionResponseDTO;
import wealthguard.entity.TipoRecomendacionEntity;

@Component
public class TipoRecomendacionMapper {

    public static TipoRecomendacionEntity toEntity(TipoRecomendacionRequestDTO dto) {
        if (dto == null) return null;
        
        TipoRecomendacionEntity entity = new TipoRecomendacionEntity();
        entity.setNombre(dto.getNombre());
        entity.setMensaje(dto.getMensaje());
        return entity;
    }

    public static TipoRecomendacionResponseDTO convertirADTO(TipoRecomendacionEntity entity) {
        if (entity == null) return null;
        
        TipoRecomendacionResponseDTO dto = new TipoRecomendacionResponseDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setMensaje(entity.getMensaje());
        return dto;
    }
}
