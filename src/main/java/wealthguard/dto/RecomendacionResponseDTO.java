package wealthguard.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import wealthguard.entity.TipoRecomendacionEntity;
import wealthguard.entity.UsuarioEntity;

@Schema(description = "DTO de respuesta con los datos de una recomendación financiera")
public class RecomendacionResponseDTO {

    @Schema(description = "ID único de la recomendación", example = "1")
    private Integer id;

    @Schema(description = "Usuario al que va dirigida la recomendación")
    private UsuarioEntity usuario;

    @Schema(description = "Tipo de recomendación aplicada")
    private TipoRecomendacionEntity tipoRecomendacion;

    @Schema(description = "Fecha en la que se generó la recomendación", example = "2024-06-01T10:00:00")
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