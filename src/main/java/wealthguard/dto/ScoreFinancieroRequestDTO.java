package wealthguard.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import wealthguard.entity.UsuarioEntity;

@Schema(description = "DTO para la solicitud de cálculo del score financiero de un usuario")
public class ScoreFinancieroRequestDTO {

    @Schema(description = "Usuario al que pertenece el score financiero")
    private UsuarioEntity usuario;

    @Schema(description = "Valor máximo posible del score", example = "1000")
    private Integer valorMaximo;

    @Schema(description = "Nivel del score financiero (1 = bajo, 5 = excelente)", example = "3")
    private Integer nivel;

    @Schema(description = "Fecha en la que se calculó el score", example = "2024-06-01T10:00:00")
    private LocalDateTime fechaCalculo;

    public ScoreFinancieroRequestDTO() {
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public Integer getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(Integer valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public LocalDateTime getFechaCalculo() {
        return fechaCalculo;
    }

    public void setFechaCalculo(LocalDateTime fechaCalculo) {
        this.fechaCalculo = fechaCalculo;
    }
}