package wealthguard.dto;

import java.time.LocalDateTime;

import wealthguard.entity.TipoRecomendacionEntity;
import wealthguard.entity.UsuarioEntity;

public class RecomendacionRequestDTO {

    private UsuarioEntity usuario;
    private TipoRecomendacionEntity tipoRecomendacion;
    private LocalDateTime fechaRecomendacion;

    public RecomendacionRequestDTO() {
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public TipoRecomendacionEntity getTipoRecomendacion() {
        return tipoRecomendacion;
    }

    public void setTipoRecomendacion(TipoRecomendacionEntity tipoRecomendacion) {
        this.tipoRecomendacion = tipoRecomendacion;
    }

    public LocalDateTime getFechaRecomendacion() {
        return fechaRecomendacion;
    }

    public void setFechaRecomendacion(LocalDateTime fechaRecomendacion) {
        this.fechaRecomendacion = fechaRecomendacion;
    }

}
