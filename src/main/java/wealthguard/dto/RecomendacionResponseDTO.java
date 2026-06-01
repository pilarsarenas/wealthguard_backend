package wealthguard.dto;

import java.time.LocalDateTime;

import wealthguard.entity.TipoRecomendacionEntity;
import wealthguard.entity.UsuarioEntity;

public class RecomendacionResponseDTO {

    private Integer id;
    private UsuarioEntity usuario;
    private TipoRecomendacionEntity tipoRecomendacion;
    private LocalDateTime fechaRecomendacion;

    public RecomendacionResponseDTO() {
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
