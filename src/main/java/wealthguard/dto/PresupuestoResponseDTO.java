package wealthguard.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import wealthguard.entity.CategoriaEntity;
import wealthguard.entity.UsuarioEntity;

@Schema(description = "DTO de respuesta con los datos de un presupuesto")
public class PresupuestoResponseDTO {

    @Schema(description = "ID único del presupuesto", example = "1")
    private Integer id;

    @Schema(description = "Usuario propietario del presupuesto")
    private UsuarioEntity usuario;

    @Schema(description = "Categoría a la que aplica el presupuesto")
    private CategoriaEntity categoria;

    @Schema(description = "Límite de gasto del presupuesto", example = "500.00")
    private double limite;

    @Schema(description = "Fecha de inicio del presupuesto", example = "2024-01-01T00:00:00")
    private LocalDateTime fechaInicio;

    @Schema(description = "Fecha de fin del presupuesto", example = "2024-01-31T23:59:59")
    private LocalDateTime fechaFin;

    public PresupuestoResponseDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
}