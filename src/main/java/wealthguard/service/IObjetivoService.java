package wealthguard.service;

import java.util.List;

import wealthguard.dto.ObjetivoRequestDTO;
import wealthguard.dto.ObjetivoResponseDTO;
import wealthguard.entity.ObjetivoEntity;

public interface IObjetivoService {

    /**
     * Crea un nuevo objetivo financiero para el usuario especificado. El
     * objetivo se asocia a una categoría específica y tiene un monto objetivo,
     * fecha de inicio y fecha de fin. El campo "completado" se establece
     * inicialmente en false.
     *
     * @param objetivoRequestDTO Objeto que contiene los datos necesarios para crear el objetivo, incluyendo el ID del usuario, ID de la categoría, nombre del objetivo, monto objetivo, fecha de inicio y fecha de fin.
     * @return El objeto ObjetivoResponseDTO con los datos del objetivo creado, incluyendo su ID generado y el estado de completado inicial.
     */
    public ObjetivoResponseDTO crearObjetivo(ObjetivoRequestDTO objetivoRequestDTO);

    /**
     * Elimina un objetivo financiero existente por su ID. Solo se puede
     * eliminar un objetivo que pertenezca al usuario especificado.
     *
     * @param idObjetivo ID del objetivo a eliminar
     * @return true si la eliminación fue exitosa, false si no se encontró el
     * objetivo o no pertenece al usuario
     */
    public boolean eliminarObjetivo(Integer idObjetivo);

    /**
     * Edita un objetivo financiero existente. Solo se puede editar un objetivo
     * que pertenezca al usuario especificado. Se pueden actualizar todos los
     * campos del objetivo, incluyendo el nombre, monto, fechas y estado de
     * completado.
     *
     * @param objetivoRequestDTO Objeto que contiene los datos actualizados del objetivo, incluyendo su ID, ID del usuario, ID de la categoría, nombre del objetivo, monto objetivo, fecha de inicio, fecha de fin y estado de completado.
     * @return true si la edición fue exitosa, false si no se encontró el
     * objetivo o no pertenece al usuario
     */
    public ObjetivoResponseDTO editarObjetivo(int idObjetivo, ObjetivoRequestDTO objetivoRequestDTO);

    /**
     * Obtiene la lista de objetivos financieros del usuario especificado. Cada
     * objetivo incluye su ID, nombre, monto objetivo, fechas y estado de
     * completado.
     *
     * @param idUsuario ID del usuario
     * @return Lista de ObjetivoEntity que pertenecen al usuario
     */
    public List<ObjetivoEntity> obtenerObjetivos(Integer idUsuario);

    /**
     * Cambia el estado de completado de un objetivo financiero. Solo se puede
     * cambiar el estado de un objetivo que pertenezca al usuario especificado.
     *
     * @param idObjetivo ID del objetivo a actualizar
     * @param completado Nuevo estado de completado (true o false)
     * @return true si la actualización fue exitosa, false si no se encontró el
     * objetivo o no pertenece al usuario
     */
    public boolean cambiarEstadoCompletado(Integer idObjetivo, Boolean completado);

}
