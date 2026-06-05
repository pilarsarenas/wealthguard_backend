package wealthguard.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import wealthguard.dto.TipoRecomendacionRequestDTO;
import wealthguard.dto.TipoRecomendacionResponseDTO;
import wealthguard.service.ITipoRecomendacionService;
public class TipoRecomendacionController {
    @RestController
    @RequestMapping("/tipo-recomendacion")

    public class TipoRecomendacionController {
    
        @Autowired
        private ITipoRecomendacionService service;
    
        @PostMapping
        public TipoRecomendacionResponseDTO crear(@RequestBody TipoRecomendacionRequestDTO dto){
            return service.crearTipoRecomendacion(dto); 
        }

        @GetMapping({"/{id}"})
        public TipoRecomendacionResponseDTO obtenerTipoRecomendacionPorId(@PathVariable Integer id) {
            return service.obtenerTipoRecomendacionPorId(id);
        }

        @PutMapping("/{id}")
        public TipoRecomendacionResponseDTO actualizarTipoRecomendacion
            (@PathVariable Integer id, 
            @RequestBody TipoRecomendacionRequestDTO dto) {
                return service.actualizarTipoRecomendacion(id, dto);
        }

        @DeleteMapping("/{id}")
        public void eliminarTipoRecomendacion(@PathVariable Integer id) {
            service.eliminarTipoRecomendacion(id);
        }
}

