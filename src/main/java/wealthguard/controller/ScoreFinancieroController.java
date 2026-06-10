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
import wealthguard.dto.ScoreFinancieroRequestDTO;
import wealthguard.dto.ScoreFinancieroResponseDTO;
import wealthguard.service.IScoreFinancieroService;

@RestController
@RequestMapping("/score-financiero")
@Tag(name = "Score Financiero", description = "Gestión del score financiero del usuario")
public class ScoreFinancieroController {

    @Autowired
    private IScoreFinancieroService service;

    @Operation(summary = "Crear un nuevo score financiero")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Score financiero creado correctamente", content = @Content(schema = @Schema(implementation = ScoreFinancieroResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ScoreFinancieroResponseDTO crear(@RequestBody ScoreFinancieroRequestDTO dto) {
        return service.crearScore(dto);
    }

    @Operation(summary = "Obtener un score financiero por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Score financiero encontrado", content = @Content(schema = @Schema(implementation = ScoreFinancieroResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Score financiero no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ScoreFinancieroResponseDTO obtenerScorePorId(
            @Parameter(description = "ID del score financiero", required = true) @PathVariable Integer id) {
        return service.obtenerScorePorId(id);
    }

    @Operation(summary = "Listar todos los scores financieros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de scores obtenido correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ScoreFinancieroResponseDTO.class))))
    })
    @GetMapping
    public List<ScoreFinancieroResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @Operation(summary = "Actualizar un score financiero por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Score financiero actualizado correctamente", content = @Content(schema = @Schema(implementation = ScoreFinancieroResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Score financiero no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ScoreFinancieroResponseDTO actualizarScore(
            @Parameter(description = "ID del score financiero a actualizar", required = true) @PathVariable Integer id,
            @RequestBody ScoreFinancieroRequestDTO dto) {
        return service.actualizarScore(id, dto);
    }

    @Operation(summary = "Eliminar un score financiero por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Score financiero eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Score financiero no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public void eliminarScore(
            @Parameter(description = "ID del score financiero a eliminar", required = true) @PathVariable Integer id) {
        service.eliminarScore(id);
    }
}
