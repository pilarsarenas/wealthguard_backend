package wealthguard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import wealthguard.dto.CategoriaRequestDTO;
import wealthguard.dto.CategoriaResponseDTO;
import wealthguard.service.ICategoriaService;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías de transacciones")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @Operation(summary = "Crear una nueva categoría")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría creada correctamente", content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping("/crear")
    public CategoriaResponseDTO crearCategoria(
            @Valid @RequestBody CategoriaRequestDTO nombreCategoria) {
        return categoriaService.crearCategoria(nombreCategoria);
    }

    @Operation(summary = "Editar una categoría existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente", content = @Content(schema = @Schema(implementation = CategoriaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @PutMapping("/editar/{id}")
    public CategoriaResponseDTO editarCategoria(
            @Parameter(description = "ID de la categoría a editar", required = true) @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequestDTO nombreCategoria) {
        return categoriaService.editarCategoria(id, nombreCategoria);
    }

    @Operation(summary = "Eliminar una categoría por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría eliminada correctamente", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Boolean> eliminarCategoria(
            @Parameter(description = "ID de la categoría a eliminar", required = true) @PathVariable Integer id) {
        boolean eliminado = categoriaService.eliminarCategoria(id);
        if (eliminado) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar categorías de un usuario con filtro opcional por nombre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de categorías obtenido correctamente", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoriaResponseDTO.class))))
    })
    @GetMapping("/listar/{idUsuario}")
    public List<CategoriaResponseDTO> listarCategorias(
            @Parameter(description = "ID del usuario", required = true) @PathVariable Integer idUsuario,
            @Parameter(description = "Nombre de la categoría para filtrar (opcional)") @RequestParam(required = false) String nombreCategoria) {
        return categoriaService.obtenerCategorias(idUsuario, nombreCategoria);
    }
}