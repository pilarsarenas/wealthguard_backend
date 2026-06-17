package wealthguard.service;

import java.util.List;

import wealthguard.dto.TipoRecomendacionRequestDTO;
import wealthguard.dto.TipoRecomendacionResponseDTO;

public interface ITipoRecomendacionService {

    
    TipoRecomendacionResponseDTO crearTipoRecomendacion(TipoRecomendacionRequestDTO dto);

    TipoRecomendacionResponseDTO obtenerTipoRecomendacionPorId(Integer id);

    List<TipoRecomendacionResponseDTO> listarTodos();

    TipoRecomendacionResponseDTO actualizarTipoRecomendacion(Integer id, TipoRecomendacionRequestDTO dto);

    void eliminarTipoRecomendacion(Integer id);
}