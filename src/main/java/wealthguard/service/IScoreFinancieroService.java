package wealthguard.service;

import java.util.List;

import wealthguard.dto.ScoreFinancieroRequestDTO;
import wealthguard.dto.ScoreFinancieroResponseDTO;

public interface IScoreFinancieroService {

    ScoreFinancieroResponseDTO crearScore(ScoreFinancieroRequestDTO dto);

    ScoreFinancieroResponseDTO obtenerScorePorId(Integer id);

    List<ScoreFinancieroResponseDTO> listarTodos();

    ScoreFinancieroResponseDTO actualizarScore(Integer id, ScoreFinancieroRequestDTO dto);

    void eliminarScore(Integer id);
}