package wealthguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import wealthguard.dto.ObjetivoRequestDTO;
import wealthguard.dto.ObjetivoResponseDTO;
import wealthguard.service.IObjetivoService;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowedHeaders = "*")
@RequestMapping("/objetivos")
@Tag(name = "Objetivos", description = "Gestión de objetivos financieros")
public class ObjetivoController {

    @Autowired
    private IObjetivoService objetivoService;

    @Operation(summary = "Crear un nuevo objetivo financiero")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objetivo creado correctamente", content = @Content(schema = @Schema(implementation = ObjetivoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping("/crear")
    public ObjetivoResponseDTO crearObjetivo(
            @Valid @RequestBody ObjetivoRequestDTO objetivoRequest) {
        return objetivoService.crearObjetivo(objetivoRequest);
    }
    
    @Operation(summary = "Editar un objetivo financiero existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objetivo actualizado correctamente", content = @Content(schema = @Schema(implementation = ObjetivoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Objetivo no encontrado", content = @Content)
    })
    @PutMapping("/editar/{id}")
    public ObjetivoResponseDTO editarObjetivo(
            @Parameter(description = "ID del objetivo a editar", required = true) @PathVariable Integer id,
            @Valid @RequestBody ObjetivoRequestDTO objetivoRequest) {
        return objetivoService.editarObjetivo(id, objetivoRequest);
    }

    @Operation(summary = "Eliminar un objetivo financiero")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objetivo eliminado correctamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Objetivo no encontrado", content = @Content)
    })
    @DeleteMapping("/eliminar/{id}")
    public void eliminarObjetivo(
            @Parameter(description = "ID del objetivo a eliminar", required = true) @PathVariable Integer id) {
        objetivoService.eliminarObjetivo(id);
    }
}

