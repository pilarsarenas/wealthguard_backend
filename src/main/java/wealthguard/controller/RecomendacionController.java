package wealthguard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import wealthguard.dto.RecomendacionResponseDTO;
import wealthguard.service.IRecomendacionService;

@RestController
@RequestMapping("/api/recomendaciones")
@CrossOrigin(origins = "*")
public class RecomendacionController {

    @Autowired
    private IRecomendacionService recomendacionService;

    @PostMapping("/generar")
    public ResponseEntity<List<RecomendacionResponseDTO>> generar(
            @RequestParam int idUsuario,
            @RequestParam int score,
            @RequestParam String nickUsuario,
            @RequestParam String nickContrasena) {
        return ResponseEntity
                .ok(recomendacionService.generarRecomendaciones(idUsuario, score, nickUsuario, nickContrasena));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<RecomendacionResponseDTO>> listar(
            @PathVariable int idUsuario,
            @RequestParam String nickUsuario,
            @RequestParam String nickContrasena) {
        return ResponseEntity.ok(recomendacionService.obtenerRecomendaciones(idUsuario, nickUsuario, nickContrasena));
    }

    @DeleteMapping("/{idRecomendacion}")
    public ResponseEntity<Boolean> eliminar(
            @PathVariable int idRecomendacion,
            @RequestParam String nickUsuario,
            @RequestParam String nickContrasena) {
        return ResponseEntity
                .ok(recomendacionService.eliminarRecomendacion(idRecomendacion, nickUsuario, nickContrasena));
    }
}