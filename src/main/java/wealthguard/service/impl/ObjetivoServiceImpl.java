package wealthguard.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import wealthguard.dto.ObjetivoRequestDTO;
import wealthguard.dto.ObjetivoResponseDTO;
import wealthguard.entity.ObjetivoEntity;
import wealthguard.mapper.ObjetivoMapper;
import wealthguard.repository.ObjetivoRepository;
import wealthguard.service.IObjetivoService;

public class ObjetivoServiceImpl implements IObjetivoService {

    @Autowired
    private ObjetivoMapper objetivoMapper;

    @Autowired
    private ObjetivoRepository objetivoRepository;

    @Override
    public ObjetivoResponseDTO crearObjetivo(ObjetivoRequestDTO objetivoRequestDTO) {
        ObjetivoEntity objetivoEntidad = objetivoMapper.convertirAEntity(objetivoRequestDTO);

        ObjetivoEntity objetivoGuardado = objetivoRepository.save(objetivoEntidad);
        return objetivoMapper.convertirADTO(objetivoGuardado);
    }

    @Override
    public boolean eliminarObjetivo(int idObjetivo) {
        if (objetivoRepository.existsById(idObjetivo)) {
            objetivoRepository.deleteById(idObjetivo);
            return true;
        }
        return false;
    }

    @Override
    public ObjetivoResponseDTO editarObjetivo(Integer idObjetivo, ObjetivoRequestDTO objetivoRequestDTO) {
        if (objetivoRepository.findById(idObjetivo).isEmpty()) {
            return null; // O lanzar una excepción
        }

        ObjetivoEntity objetivoEntidad = objetivoMapper.convertirAEntity(objetivoRequestDTO);
        ObjetivoEntity objetivoActualizado = objetivoRepository.save(objetivoEntidad);
        return objetivoMapper.convertirADTO(objetivoActualizado);
    }

    @Override
    public List<ObjetivoEntity> obtenerObjetivos(int idUsuario) {
        return objetivoRepository.findByUsuarioId(idUsuario);
    }

    @Override
    public boolean cambiarEstadoCompletado(int idObjetivo, Boolean completado) {
        if (objetivoRepository.findById(idObjetivo).isEmpty()) {
            return false; // O lanzar una excepción
        }

        ObjetivoEntity objetivoEntidad = objetivoRepository.findById(idObjetivo).get();
        objetivoEntidad.setCompletado(completado);
        objetivoRepository.save(objetivoEntidad);
        return true;
    }
}