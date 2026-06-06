package wealthguard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta con los datos de una categoría")
public class CategoriaResponseDTO {

    @Schema(description = "ID único de la categoría", example = "1")
    private Integer id;

    @Schema(description = "Nombre de la categoría", example = "Alimentación")
    private String nombre;

    @Schema(description = "ID del usuario propietario de la categoría", example = "42")
    private Integer usuarioId;

    public CategoriaResponseDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}