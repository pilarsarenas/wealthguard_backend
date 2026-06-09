package wealthguard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la solicitud de creación o edición de una categoría")
public class CategoriaRequestDTO {

    @Schema(description = "Nombre de la categoría", example = "Alimentación")
    private String nombre;

    public CategoriaRequestDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}