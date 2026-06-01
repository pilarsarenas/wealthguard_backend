package wealthguard.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wealthguard.dto.TipoRecomendacionRequestDTO;
import wealthguard.dto.TipoRecomendacionResponseDTO;
import wealthguard.entity.TipoRecomendacionEntity;
import wealthguard.mapper.TipoRecomendacionMapper;
import wealthguard.repository.TipoRecomendacionRepository;
import wealthguard.service.ITipoRecomendacionService;

@Service
public class TipoRecomendacionServiceImpl implements ITipoRecomendacionService{

    @Autowired
    private TipoRecomendacionRepository tipoRecomendacionRepository;

    @Autowired
    private TipoRecomendacionMapper tipoRecomendacionMapper;

    @Override
    public TipoRecomendacionResponseDTO crearTipoRecomendacion(TipoRecomendacionRequestDTO request) {
        
        TipoRecomendacionEntity entity = tipoRecomendacionMapper.convertirAEntity(request);
        
        TipoRecomendacionEntity guardado = tipoRecomendacionRepository.save(entity);
        
        return tipoRecomendacionMapper.convertirADTo(Guardado);
    }

    @Override
    public boolean eliminarTipoRecomendacion(Integer idRecomendacion) {
        if (tipoRecomendacionRepository.existsById(idRecomendacion)) {
            tipoRecomendacionRepository.deleteById(idRecomendacion);
            return true;
        }else{
        return false;
    }

}

