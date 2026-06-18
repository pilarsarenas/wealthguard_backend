package wealthguard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wealthguard.dto.ScoreFinancieroRequestDTO;
import wealthguard.dto.ScoreFinancieroResponseDTO;
import wealthguard.service.IScoreFinancieroService;

@RestController
@RequestMapping("/score-financiero")
public class ScoreFinancieroController {

        @Autowired
        private IScoreFinancieroService service;

        @PostMapping
        public ScoreFinancieroResponseDTO crear(@RequestBody ScoreFinancieroRequestDTO dto,
                        @RequestParam String nickUsuario,
                        @RequestParam String nickContrasena) {
                return service.crearScore(dto, nickUsuario, nickContrasena);
        }

        @GetMapping("/{id}")
        public ScoreFinancieroResponseDTO obtenerScorePorId(
                        @PathVariable Integer id,
                        @RequestParam String nickUsuario,
                        @RequestParam String nickContrasena) {
                return service.obtenerScorePorId(id, nickUsuario, nickContrasena);
        }

        @GetMapping
        public List<ScoreFinancieroResponseDTO> listarTodos(
                        @RequestParam String nickUsuario,
                        @RequestParam String nickContrasena) {
                return service.listarTodos(nickUsuario, nickContrasena);
        }

        @PutMapping("/{id}")
        public ScoreFinancieroResponseDTO actualizarScore(
                        @PathVariable Integer id,
                        @RequestBody ScoreFinancieroRequestDTO dto,
                        @RequestParam String nickUsuario,
                        @RequestParam String nickContrasena) {
                return service.actualizarScore(id, dto, nickUsuario, nickContrasena);
        }

        @DeleteMapping("/{id}")
        public void eliminarScore(
                        @PathVariable Integer id,
                        @RequestParam String nickUsuario,
                        @RequestParam String nickContrasena) {
                service.eliminarScore(id, nickUsuario, nickContrasena);
        }
}