package wealthguard.service;

import java.time.LocalDateTime;
import java.util.List;

import wealthguard.dto.PresupuestoRequestDTO;
import wealthguard.dto.PresupuestoResponseDTO;
import wealthguard.entity.PresupuestoEntity;

public interface IPresupuestoService {

    /**
     * Crea un nuevo presupuesto para un usuario y categoría específicos.
     *
     * @param idUsuario ID del usuario al que pertenece el presupuesto
     * @param idCategoria ID de la categoría asociada al presupuesto
     * @param limite Límite del presupuesto
     * @param fechaInicio Fecha y hora de inicio del presupuesto
     * @param fechaFin Fecha y hora de fin del presupuesto
     * @return true si el presupuesto se creó exitosamente, false en caso
     * contrario.
     */
    public PresupuestoResponseDTO crearPresupuesto(PresupuestoRequestDTO presupuestoRequest);

    /**
     * Elimina un presupuesto existente por su ID.
     *
     * @param idPresupuesto ID del presupuesto a eliminar
     * @return true si la eliminación fue exitosa, false si no se encontró el
     * presupuesto o no pertenece al usuario
     */
    public boolean eliminarPresupuesto(int idPresupuesto);

    /**
     * Edita un presupuesto existente. Solo se puede editar un presupuesto que
     * pertenezca al usuario especificado.
     *
     * @param idPresupuesto ID del presupuesto a editar
     * @param idCategoria ID de la nueva categoría asociada al presupuesto
     * @param limite Nuevo límite del presupuesto
     * @param fechaInicio Nueva fecha y hora de inicio del presupuesto
     * @param fechaFin Nueva fecha y hora de fin del presupuesto
     * @return true si la edición fue exitosa, false si no se encontró el
     * presupuesto o no pertenece al usuario
     */
    public boolean editarPresupuesto(int idPresupuesto, int idCategoria, double limite, LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Obtiene la lista de presupuestos del usuario especificado. Cada
     * presupuesto incluye su ID, categoría, límite y fechas.
     *
     * @param idUsuario ID del usuario
     * @return Lista de PresupuestoEntity que pertenecen al usuario
     */
    public List<PresupuestoEntity> obtenerPresupuestos(int idUsuario);

}
