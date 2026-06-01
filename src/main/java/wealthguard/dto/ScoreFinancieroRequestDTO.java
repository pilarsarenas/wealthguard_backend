package wealthguard.dto;
import java.time.LocalDateTime;

import wealthguard.entity.UsuarioEntity;

public class ScoreFinancieroRequestDTO {

    private UsuarioEntity usuario;
    private Integer valorMaximo;
    private Integer nivel;
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
