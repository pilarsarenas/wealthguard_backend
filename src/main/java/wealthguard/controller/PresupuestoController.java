package wealthguard.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import wealthguard.dto.PresupuestoRequestDTO;
import wealthguard.dto.PresupuestoResponseDTO;
import wealthguard.service.IPresupuestoService;

@RestController
@RequestMapping("/presupuestos")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@Tag(
    name = "Presupuestos",
    description = "Endpoints para la gestión de presupuestos por categoría"
)
public class PresupuestoController {

    @Autowired
    private IPresupuestoService presupuestoService;

    @Operation(
        summary = "Obtener presupuestos del usuario",
        description = """
                Devuelve todos los presupuestos asociados a un usuario.

                Cada presupuesto incluye:
                - Límite configurado.
                - Gasto actual calculado.
                - Porcentaje consumido.
                - Categoría asociada.
                """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Presupuestos obtenidos correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PresupuestoResponseDTO.class)
            )
        )
    })
    @GetMapping("/listar/{idUsuario}")
    public List<PresupuestoResponseDTO> obtenerPresupuestos(
            @Parameter(
                description = "Identificador del usuario",
                example = "1",
                required = true
            )
            @PathVariable Integer idUsuario) {

        return presupuestoService.obtenerPresupuestos(idUsuario);
    }

    @Operation(
        summary = "Crear presupuesto",
        description = """
                Crea un nuevo presupuesto para una categoría.

                El presupuesto queda asociado a:
                - Usuario.
                - Categoría.
                - Límite de gasto.
                - Periodo de validez.
                """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Presupuesto creado correctamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = PresupuestoResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos incorrectos"
        )
    })
    @PostMapping("/crear")
    public PresupuestoResponseDTO crearPresupuesto(
            @RequestBody(
                description = "Datos necesarios para crear el presupuesto",
                required = true
            )
            @org.springframework.web.bind.annotation.RequestBody
            PresupuestoRequestDTO presupuestoRequestDTO) {

        return presupuestoService.crearPresupuesto(presupuestoRequestDTO);
    }

    @Operation(
        summary = "Editar presupuesto",
        description = """
                Modifica un presupuesto existente.

                Permite actualizar:
                - Categoría.
                - Límite.
                - Fecha de inicio.
                - Fecha de fin.
                """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Presupuesto actualizado correctamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Presupuesto no encontrado"
        )
    })
    @PutMapping("/editar/{idPresupuesto}")
    public ResponseEntity<Boolean> editarPresupuesto(

            @Parameter(
                description = "ID del presupuesto",
                example = "5",
                required = true
            )
            @PathVariable int idPresupuesto,

            @Parameter(
                description = "ID de la categoría",
                example = "2",
                required = true
            )
            @RequestParam int idCategoria,

            @Parameter(
                description = "Nuevo límite de gasto",
                example = "500.00",
                required = true
            )
            @RequestParam double limite,

            @Parameter(
                description = "Fecha de inicio",
                example = "2025-06-01T00:00:00",
                required = true
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaInicio,

            @Parameter(
                description = "Fecha de fin",
                example = "2025-06-30T23:59:59",
                required = true
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaFin) {

        boolean editado = presupuestoService.editarPresupuesto(
                idPresupuesto,
                idCategoria,
                limite,
                fechaInicio,
                fechaFin);

        return editado
                ? ResponseEntity.ok(true)
                : ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Eliminar presupuesto",
        description = "Elimina definitivamente un presupuesto."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Presupuesto eliminado correctamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Presupuesto no encontrado"
        )
    })
    @DeleteMapping("/eliminar/{idPresupuesto}")
    public ResponseEntity<Boolean> eliminarPresupuesto(

            @Parameter(
                description = "ID del presupuesto",
                example = "5",
                required = true
            )
            @PathVariable int idPresupuesto) {

        boolean eliminado = presupuestoService.eliminarPresupuesto(idPresupuesto);

        return eliminado
                ? ResponseEntity.ok(true)
                : ResponseEntity.notFound().build();
    }
}