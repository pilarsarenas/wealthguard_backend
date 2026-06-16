package wealthguard.service;


import wealthguard.dto.ObjetivoRequestDTO;
import wealthguard.dto.ObjetivoResponseDTO;

public interface IObjetivoService {

    /**
     * Crea un nuevo objetivo financiero para el usuario especificado. El
     * objetivo se asocia a una categoría específica y tiene un monto objetivo,
     * fecha de inicio y fecha de fin. 
     *
     * @param objetivoRequestDTO Objeto que contiene los datos necesarios para crear el objetivo, incluyendo el ID del usuario, monto objetivo, fecha de inicio y fecha de fin.
     * @return El objeto ObjetivoResponseDTO con los datos del objetivo creado, incluyendo su ID generado.
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
     * campos del objetivo, monto, fechas.
     *
     * @param objetivoRequestDTO Objeto que contiene los datos actualizados del objetivo, incluyendo su ID, ID del usuario, monto objetivo, fecha de inicio, fecha de fin.
     * @return true si la edición fue exitosa, false si no se encontró el
     * objetivo o no pertenece al usuario
     */
    public ObjetivoResponseDTO editarObjetivo(int idObjetivo, ObjetivoRequestDTO objetivoRequestDTO);

    /**
     * Obtiene el objetivo financiero del usuario especificado. Cada
     * objetivo incluye su ID, monto objetivo y fecha.
     *
     * @param idUsuario ID del usuario
     * @return El objeto ObjetivoResponseDTO con los datos del objetivo creado, incluyendo su ID generado.
     */
    public ObjetivoResponseDTO obtenerObjetivo(Integer idUsuario);

    /**
     * Obtiene el objetivo financiero del usuario especificado. Cada
     * objetivo incluye su ID, monto objetivo y fecha.
     * @param idUsuario
     * @return El objeto ObjetivoResponseDTO con los datos del objetivo creado, incluyendo su ID generado.
     */
    public ObjetivoResponseDTO obtenerUltimoObjetivo(Integer idUsuario);


}
