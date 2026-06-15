package wealthguard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import wealthguard.dto.ObjetivoRequestDTO;
import wealthguard.dto.ObjetivoResponseDTO;
import wealthguard.service.IObjetivoService;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
                RequestMethod.OPTIONS }, allowedHeaders = "*")
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
        public ResponseEntity<ObjetivoResponseDTO> crearObjetivo(@RequestBody ObjetivoRequestDTO objetivoRequestDTO) {
                return ResponseEntity.ok(objetivoService.crearObjetivo(objetivoRequestDTO));
        }

        @Operation(summary = "Editar un objetivo financiero existente")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Objetivo actualizado correctamente", content = @Content(schema = @Schema(implementation = ObjetivoResponseDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Objetivo no encontrado", content = @Content)
        })
        @PutMapping("/editar/{id}")
        public ResponseEntity<ObjetivoResponseDTO> editarObjetivo(@PathVariable int id,
                        @RequestBody ObjetivoRequestDTO objetivoRequestDTO) {
                return ResponseEntity.ok(objetivoService.editarObjetivo(id, objetivoRequestDTO));
        }

        @Operation(summary = "Eliminar un objetivo financiero")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Objetivo eliminado correctamente", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Objetivo no encontrado", content = @Content)
        })
        @DeleteMapping("/eliminar/{id}")
        public ResponseEntity<Boolean> eliminarObjetivo(@PathVariable Integer id) {
                return ResponseEntity.ok(objetivoService.eliminarObjetivo(id));
        }

        @GetMapping("/activa/{idUsuario}")
        public ResponseEntity<ObjetivoResponseDTO> obtenerMetaActiva(@PathVariable Integer idUsuario) {
                return ResponseEntity.ok(objetivoService.obtenerObjetivo(idUsuario));
        }

}
