package wealthguard.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wealthguard.dto.ScoreFinancieroRequestDTO;
import wealthguard.dto.ScoreFinancieroResponseDTO;
import wealthguard.entity.ScoreFinancieroEntity;
import wealthguard.mapper.ScoreFinancieroMapper;
import wealthguard.repository.ScoreFinancieroRepository;
import wealthguard.service.IScoreFinancieroService;
public class ScoreFinancieroServiceImpl implements IScoreFinancieroService{

    @Autowired
    private ScoreFinancieroRepository scoreFinancieroRepository;

    @Autowired
    private ScoreFinancieroMapper scoreFinancieroMapper;

    @Override
    public ScoreFinancieroResponseDTO calcularScore(ScoreFinancieroRequestDTO request) {
        
        ScoreFinancieroEntity entity = scoreFinancieroMapper.convertirAEntity(request);
        
        ScoreFinancieroEntity guardado = scoreFinancieroRepository.save(entity);
        
        return scoreFinancieroMapper.convertirADTO(guardado);
    }

}

