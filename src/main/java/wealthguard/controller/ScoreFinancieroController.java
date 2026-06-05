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
    public ScoreFinancieroResponseDTO crear(@RequestBody ScoreFinancieroRequestDTO dto){
    return service.crearScore(dto); 
    }   

    @GetMapping({"/{id}"})
    public ScoreFinancieroResponseDTO obtenerScorePorId(@PathVariable Integer id) {
        return service.obtenerScorePorId(id);
    }

    @GetMapping
    public List<ScoreFinancieroResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @PutMapping("/{id}")
    public ScoreFinancieroResponseDTO actualizarScore
        (@PathVariable Integer id, 
        @RequestBody ScoreFinancieroRequestDTO dto) {
            return service.actualizarScore(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminarScore(@PathVariable Integer id) {
        service.eliminarScore(id);
    }
}

    

