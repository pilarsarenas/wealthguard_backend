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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                        @RequestParam String contrasena) throws Exception {
                return service.crearScore(dto, nickUsuario, contrasena);
        }

        @GetMapping("/{id}")
        public ScoreFinancieroResponseDTO obtenerScorePorId(
                        @PathVariable Integer id,
                        @RequestParam String nickUsuario,
                        @RequestParam String contrasena) throws Exception {
                return service.obtenerScorePorId(id, nickUsuario, contrasena);
        }

        @GetMapping
        public List<ScoreFinancieroResponseDTO> listarTodos(
                        @RequestParam String nickUsuario,
                        @RequestParam String contrasena) throws Exception {
                return service.listarTodos(nickUsuario, contrasena);
        }

        @PutMapping("/{id}")
        public ScoreFinancieroResponseDTO actualizarScore(
                        @PathVariable Integer id,
                        @RequestBody ScoreFinancieroRequestDTO dto,
                        @RequestParam String nickUsuario,
                        @RequestParam String contrasena) throws Exception {
                return service.actualizarScore(id, dto, nickUsuario, contrasena);
        }

        @DeleteMapping("/{id}")
        public void eliminarScore(
                        @PathVariable Integer id,
                        @RequestParam String nickUsuario,
                        @RequestParam String contrasena) throws Exception {
                service.eliminarScore(id, nickUsuario, contrasena);
        }
}