package wealthguard.dto;

import java.time.LocalDateTime;

import wealthguard.entity.CategoriaEntity;
import wealthguard.entity.UsuarioEntity;

public class PresupuestoRequestDTO {

    private UsuarioEntity usuario;
    private CategoriaEntity categoria;
    private double limite;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;


    public PresupuestoRequestDTO() {
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
