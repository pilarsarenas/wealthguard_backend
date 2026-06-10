package wealthguard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import wealthguard.dto.RecomendacionResponseDTO;
import wealthguard.service.IRecomendacionService;

@RestController
@RequestMapping("/api/recomendaciones")
@CrossOrigin(origins = "*")
@Tag(name = "Recomendaciones", description = "Recomendaciones financieras generadas automáticamente según el score del usuario")
public class RecomendacionController {

    @Autowired
    private IRecomendacionService recomendacionService;

    @PostMapping("/generar")
    @Operation(summary = "Generar recomendaciones por score", description = "Borra las recomendaciones anteriores del usuario y genera las nuevas según su score actual")
    public ResponseEntity<List<RecomendacionResponseDTO>> generar(
            @Parameter(description = "ID del usuario", required = true) @RequestParam int idUsuario,
            @Parameter(description = "Score financiero actual (0-100)", required = true) @RequestParam int score) {
        return ResponseEntity.ok(recomendacionService.generarRecomendaciones(idUsuario, score));
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Listar recomendaciones del usuario", description = "Devuelve todas las recomendaciones del usuario de más reciente a más antigua")
    public ResponseEntity<List<RecomendacionResponseDTO>> listar(
            @Parameter(description = "ID del usuario", required = true) @PathVariable int idUsuario) {
        return ResponseEntity.ok(recomendacionService.obtenerRecomendaciones(idUsuario));
    }

    @DeleteMapping("/{idRecomendacion}")
    @Operation(summary = "Eliminar recomendación", description = "Elimina una recomendación existente por su ID")
    public ResponseEntity<Boolean> eliminar(
            @Parameter(description = "ID de la recomendación", required = true) @PathVariable int idRecomendacion) {
        return ResponseEntity.ok(recomendacionService.eliminarRecomendacion(idRecomendacion));
    }
}