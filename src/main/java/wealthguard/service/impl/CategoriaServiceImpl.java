package wealthguard.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wealthguard.dto.CategoriaRequestDTO;
import wealthguard.dto.CategoriaResponseDTO;
import wealthguard.entity.CategoriaEntity;
import wealthguard.mapper.CategoriaMapper;
import wealthguard.repository.CategoriaRepository;
import wealthguard.service.ICategoriaService;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    @Override
    public boolean eliminarCategoriaGlobal(int idCategoria) {

        if (categoriaRepository.existsById(idCategoria)) {
            // Aquí se podría agregar lógica adicional para verificar si la categoría es "General" o "Sin categoría"
            categoriaRepository.deleteById(idCategoria);
            return true;
        } else {
            return false; // No se encontró la categoría

        }
    }

    @Override
    public CategoriaResponseDTO crearCategoriaGlobal(CategoriaRequestDTO nombreCategoria) {

        CategoriaEntity categoriaEntidad = categoriaMapper.convertirAEntity(nombreCategoria);

        if (categoriaRepository.existsByNombre(categoriaEntidad.getNombre())) {
            return null; // Ya existe una categoría global con el mismo nombre
        } else {
            CategoriaEntity categoriaGuardada = categoriaRepository.save(categoriaEntidad);
            return categoriaMapper.convertirADTO(categoriaGuardada); // Retorna el DTO de la categoría creada
        }

    }

    @Override
    public List<CategoriaResponseDTO> obtenerCategoriasGlobales(Integer usuarioId, String nombreCategoria) {
        
        List<CategoriaEntity> categorias = categoriaRepository.buscarConFiltro(usuarioId, nombreCategoria);

        return categorias.stream()
                .map(categoria -> categoriaMapper.convertirADTO(categoria))
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaResponseDTO editarCategoriaGlobal(int idCategoria, CategoriaRequestDTO categoriaRequest) {
        
        CategoriaEntity categoriaEntidad = categoriaRepository.findById(idCategoria).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        CategoriaEntity categoriaActualizada = categoriaMapper.convertirAEntity(categoriaRequest);
        categoriaActualizada.setId(categoriaEntidad.getId()); // Aseguramos que el ID se mantenga

        if (categoriaRepository.existsByNombre(categoriaActualizada.getNombre())) {
            return null; // Ya existe una categoría global con el mismo nombre
        } else {
            CategoriaEntity categoriaGuardada = categoriaRepository.save(categoriaActualizada);
            return categoriaMapper.convertirADTO(categoriaGuardada); // Retorna el DTO de la categoría actualizada
        }
    }

}
