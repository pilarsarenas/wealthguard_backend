package wealthguard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wealthguard.dto.TipoRecomendacionRequestDTO;
import wealthguard.dto.TipoRecomendacionResponseDTO;
import wealthguard.service.ITipoRecomendacionService;

@RestController
@RequestMapping("/tipo-recomendacion")
public class TipoRecomendacionController {

        @Autowired
        private ITipoRecomendacionService service;

        @PostMapping
        public TipoRecomendacionResponseDTO crear(@RequestBody TipoRecomendacionRequestDTO dto,
                        @RequestParam String nickUsuario,
                        @RequestParam String nickContrasena) {
                return service.crearTipoRecomendacion(dto, nickUsuario, nickContrasena);
        }

        @GetMapping
        public List<TipoRecomendacionResponseDTO> listarTodos(
                        @RequestParam String nickUsuario,
                        @RequestParam String nickContrasena) {
                return service.listarTodos(nickUsuario, nickContrasena);
        }

        @GetMapping("/{id}")
        public TipoRecomendacionResponseDTO obtenerTipoRecomendacionPorId(
                        @PathVariable Integer id,
                        @RequestParam String nickUsuario,
                        @RequestParam String nickContrasena) {
                return service.obtenerTipoRecomendacionPorId(id, nickUsuario, nickContrasena);
        }

        @PutMapping("/{id}")
        public TipoRecomendacionResponseDTO actualizarTipoRecomendacion(
                        @PathVariable Integer id,
                        @RequestBody TipoRecomendacionRequestDTO dto,
                        @RequestParam String nickUsuario,
                        @RequestParam String nickContrasena) {
                return service.actualizarTipoRecomendacion(id, dto, nickUsuario, nickContrasena);
        }

        @DeleteMapping("/{id}")
        public void eliminarTipoRecomendacion(
                        @PathVariable Integer id,
                        @RequestParam String nickUsuario,
                        @RequestParam String nickContrasena) {
                service.eliminarTipoRecomendacion(id, nickUsuario, nickContrasena);
        }
}