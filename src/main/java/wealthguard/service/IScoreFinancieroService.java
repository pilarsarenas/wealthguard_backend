package wealthguard.service;

import java.util.List;

import wealthguard.dto.ScoreFinancieroRequestDTO;
import wealthguard.dto.ScoreFinancieroResponseDTO;

public interface IScoreFinancieroService {

    /**
     * Crea un nuevo score financiero.
     *
     * @param dto            Datos del score a crear
     * @param nickUsuario    Nick del usuario autenticado
     * @param nickContrasena Contraseña del usuario autenticado
     * @return El objeto ScoreFinancieroResponseDTO creado
     */
    ScoreFinancieroResponseDTO crearScore(ScoreFinancieroRequestDTO dto, String nickUsuario, String nickContrasena) throws Exception;

    /**
     * Obtiene un score financiero por su ID.
     *
     * @param id             ID del score financiero
     * @param nickUsuario    Nick del usuario autenticado
     * @param nickContrasena Contraseña del usuario autenticado
     * @return El objeto ScoreFinancieroResponseDTO encontrado, o null si no existe
     */
    ScoreFinancieroResponseDTO obtenerScorePorId(Integer id, String nickUsuario, String nickContrasena) throws Exception;

    /**
     * Lista todos los scores financieros registrados.
     *
     * @param nickUsuario    Nick del usuario autenticado
     * @param nickContrasena Contraseña del usuario autenticado
     * @return Lista de ScoreFinancieroResponseDTO
     */
    List<ScoreFinancieroResponseDTO> listarTodos(String nickUsuario, String nickContrasena) throws Exception;

    /**
     * Actualiza un score financiero existente.
     *
     * @param id             ID del score financiero a actualizar
     * @param dto            Datos actualizados del score
     * @param nickUsuario    Nick del usuario autenticado
     * @param nickContrasena Contraseña del usuario autenticado
     * @return El objeto ScoreFinancieroResponseDTO actualizado
     */
    ScoreFinancieroResponseDTO actualizarScore(Integer id, ScoreFinancieroRequestDTO dto, String nickUsuario,
            String nickContrasena) throws Exception;

    /**
     * Elimina un score financiero por su ID.
     *
     * @param id             ID del score financiero a eliminar
     * @param nickUsuario    Nick del usuario autenticado
     * @param nickContrasena Contraseña del usuario autenticado
     * @return true si se eliminó, false si no existía
     */
    void eliminarScore(Integer id, String nickUsuario, String nickContrasena)throws Exception;
}