package wealthguard.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wealthguard.dto.ScoreFinancieroRequestDTO;
import wealthguard.dto.ScoreFinancieroResponseDTO;
import wealthguard.entity.ScoreFinancieroEntity;
import wealthguard.mapper.ScoreFinancieroMapper;
import wealthguard.repository.ScoreFinancieroRepository;
import wealthguard.service.IScoreFinancieroService;

@Service
public class ScoreFinancieroServiceImpl implements IScoreFinancieroService {

    @Autowired
    private ScoreFinancieroRepository repository;

    @Override
    public ScoreFinancieroResponseDTO crearScore(ScoreFinancieroRequestDTO dto) {
        ScoreFinancieroEntity entity = ScoreFinancieroMapper.toEntity(dto);
        ScoreFinancieroEntity savedEntity = repository.save(entity);
        return ScoreFinancieroMapper.convertirADTO(savedEntity);
    }

    @Override
    public ScoreFinancieroResponseDTO obtenerScorePorId(Integer id) {
        return repository.findById(id)
                .map(ScoreFinancieroMapper::convertirADTO)
                .orElse(null);
    }

    @Override
    public List<ScoreFinancieroResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(ScoreFinancieroMapper::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public ScoreFinancieroResponseDTO actualizarScore(Integer id, ScoreFinancieroRequestDTO dto) {
        
                    ScoreFinancieroEntity existente = repository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Score no encontrado"));
        
                    existente.setUsuario(dto.getUsuario());
                    existente.setValorMaximo(dto.getValorMaximo());
                    existente.setNivel(dto.getNivel());
                    existente.setFechaCalculo(dto.getFechaCalculo());
                    ScoreFinancieroEntity actualizado = repository.save(existente);
                    return ScoreFinancieroMapper.convertirADTO(actualizado);
                
                
    }

    @Override
    public void eliminarScore(Integer id) {
        repository.deleteById(id);
    }

    
}


