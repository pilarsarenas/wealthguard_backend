package wealthguard.mapper;

import org.springframework.stereotype.Component;

import wealthguard.dto.ObjetivoRequestDTO;
import wealthguard.dto.ObjetivoResponseDTO;
import wealthguard.entity.CategoriaEntity;
import wealthguard.entity.ObjetivoEntity;
import wealthguard.entity.UsuarioEntity;

@Component
public class ObjetivoMapper {

    public ObjetivoEntity convertirAEntity (ObjetivoRequestDTO objetivoRequestDTO){
        if (objetivoRequestDTO == null) {
            return null;
        }

        ObjetivoEntity objetivoEntity = new ObjetivoEntity();
        
        if (objetivoRequestDTO.getUsuarioId() != null) {
                    UsuarioEntity usuario = new UsuarioEntity();
                    usuario.setId(objetivoRequestDTO.getUsuarioId());
                    objetivoEntity.setUsuario(usuario);
                }
        if (objetivoRequestDTO.getCategoriaId() != null) {
            CategoriaEntity categoria = new CategoriaEntity();
            categoria.setId(objetivoRequestDTO.getCategoriaId());
            objetivoEntity.setCategoria(categoria);
        }
        objetivoEntity.setCantidadObjetivo(objetivoRequestDTO.getCantidadObjetivo());
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

        objetivoResponseDTO.setUsuarioId(objetivoEntity.getUsuario().getId());
        objetivoResponseDTO.setCantidadObjetivo(objetivoEntity.getCantidadObjetivo());
        objetivoResponseDTO.setCategoriaId(objetivoEntity.getCategoria().getId());
        objetivoResponseDTO.setFechaInicio(objetivoEntity.getFechaInicio());
        objetivoResponseDTO.setFechaFin(objetivoEntity.getFechaFin());
        objetivoResponseDTO.setCompletado(objetivoEntity.getCompletado());

        return objetivoResponseDTO;
    }

}
