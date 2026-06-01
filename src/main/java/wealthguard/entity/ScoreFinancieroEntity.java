package wealthguard.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "score_financiero")

public class ScoreFinancieroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuarioId;

    @Column(name = "valor_maximo", nullable = false)
    private Integer valorMaximo;

    @Column(name = "nivel", nullable = false)
    private Integer nivel;

     @Column(name = "fecha_calculo", nullable = false)
    private LocalDateTime fechaCalculo;

     public Integer getId() {
         return id;
     }

     public void setId(Integer id) {
         this.id = id;
     }

     public UsuarioEntity getUsuario() {
         return usuarioId;
     }

     public void setUsuario(UsuarioEntity usuario) {
         this.usuarioId = usuario;
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
