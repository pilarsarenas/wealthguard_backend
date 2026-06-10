package wealthguard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import wealthguard.dto.TipoRecomendacionRequestDTO;
import wealthguard.dto.TipoRecomendacionResponseDTO;
import wealthguard.service.ITipoRecomendacionService;

@RestController
@RequestMapping("/tipo-recomendacion")
@Tag(name = "Tipos de Recomendación", description = "Gestión del catálogo de tipos de recomendaciones financieras")
public class TipoRecomendacionController {

    @Autowired
    private ITipoRecomendacionService service;

    @Operation(summary = "Crear un nuevo tipo de recomendación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de recomendación creado correctamente", content = @Content(schema = @Schema(implementation = TipoRecomendacionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public TipoRecomendacionResponseDTO crear(@RequestBody TipoRecomendacionRequestDTO dto) {
        return service.crearTipoRecomendacion(dto);
    }

    @Operation(summary = "Listar todos los tipos de recomendación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de tipos de recomendación obtenido correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TipoRecomendacionResponseDTO.class))))
    })
    @GetMapping
    public List<TipoRecomendacionResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @Operation(summary = "Obtener un tipo de recomendación por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de recomendación encontrado", content = @Content(schema = @Schema(implementation = TipoRecomendacionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de recomendación no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public TipoRecomendacionResponseDTO obtenerTipoRecomendacionPorId(
            @Parameter(description = "ID del tipo de recomendación", required = true) @PathVariable Integer id) {
        return service.obtenerTipoRecomendacionPorId(id);
    }

    @Operation(summary = "Actualizar un tipo de recomendación por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de recomendación actualizado correctamente", content = @Content(schema = @Schema(implementation = TipoRecomendacionResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de recomendación no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public TipoRecomendacionResponseDTO actualizarTipoRecomendacion(
            @Parameter(description = "ID del tipo de recomendación a actualizar", required = true) @PathVariable Integer id,
            @RequestBody TipoRecomendacionRequestDTO dto) {
        return service.actualizarTipoRecomendacion(id, dto);
    }

    @Operation(summary = "Eliminar un tipo de recomendación por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de recomendación eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Tipo de recomendación no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public void eliminarTipoRecomendacion(
            @Parameter(description = "ID del tipo de recomendación a eliminar", required = true) @PathVariable Integer id) {
        service.eliminarTipoRecomendacion(id);
    }
}
