package wealthguard.service;

import java.time.LocalDateTime;
import java.util.List;

import wealthguard.dto.PresupuestoRequestDTO;
import wealthguard.dto.PresupuestoResponseDTO;

public interface IPresupuestoService {

    /**
     * Crea un nuevo presupuesto para un usuario y categoría específicos.
     */
    PresupuestoResponseDTO crearPresupuesto(PresupuestoRequestDTO presupuestoRequest);

    /**
     * Elimina un presupuesto existente por su ID.
     */
    boolean eliminarPresupuesto(int idPresupuesto);

    /**
     * Modifica el límite y las fechas de un presupuesto existente.
     */
    boolean editarPresupuesto(int idPresupuesto, int idCategoria, double limite,
            LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Devuelve todos los presupuestos del usuario con el gasto actual calculado
     * a partir de sus transacciones y el porcentaje utilizado.
     * Es el método principal para la pantalla de presupuestos.
     */
    List<PresupuestoResponseDTO> obtenerPresupuestos(int idUsuario);
}