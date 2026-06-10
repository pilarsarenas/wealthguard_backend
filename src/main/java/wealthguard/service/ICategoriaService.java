package wealthguard.service;

import java.util.List;

import wealthguard.dto.CategoriaRequestDTO;
import wealthguard.dto.CategoriaResponseDTO;

public interface ICategoriaService {

    /**
     * Elimina una categoría
     *
     * @param idCategoria ID de la categoría a eliminar
     * @return true si la eliminación fue exitosa, false si no se encontró la
     * categoría
     */
    public boolean eliminarCategoria(int idCategoria);

    /**
     * Crea una nueva categoría  en el sistema.
     *
     * @param nombreCategoria DTO que contiene el nombre de la categoría  a crear
     * @return true si la creación fue exitosa, false si ya existe una categoría
     * con el mismo nombre
     */
    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO nombreCategoria);

    /**
     * Obtiene la lista de categorías disponibles en el sistema.
     *
     * @return Lista de nombres de categorías
     */
    public List<CategoriaResponseDTO> obtenerCategorias();

    /**
     * Edita el nombre de una categoría existente.
     *
     * @param idCategoria ID de la categoría a editar
     * @param nuevoNombre DTO que contiene el nuevo nombre para la categoría
     * @return El objeto {@link CategoriaResponseDTO} con los datos actualizados de la categoría, o null si no se encontró la categoría
     */
    public CategoriaResponseDTO editarCategoria(int idCategoria, CategoriaRequestDTO nuevoNombre);

}
