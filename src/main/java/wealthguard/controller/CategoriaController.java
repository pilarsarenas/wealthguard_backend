package wealthguard.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import wealthguard.dto.CategoriaRequestDTO;
import wealthguard.dto.CategoriaResponseDTO;
import wealthguard.service.ICategoriaService;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, allowedHeaders = "*")
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @PostMapping("/crear")
    public CategoriaResponseDTO crearCategoria(@Valid @RequestBody CategoriaRequestDTO nombreCategoria) {
        return categoriaService.crearCategoria(nombreCategoria);
    }

    @PutMapping("/editar/{id}")
    public CategoriaResponseDTO editarCategoria(@PathVariable Integer id, @Valid @RequestBody CategoriaRequestDTO nombreCategoria) {
        return categoriaService.editarCategoria(id, nombreCategoria);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Boolean> eliminarCategoria(@PathVariable Integer id) {
        boolean eliminado = categoriaService.eliminarCategoria(id);
        if (eliminado) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listar/{idUsuario}")
    public List<CategoriaResponseDTO> listarCategorias(@PathVariable Integer idUsuario, @RequestParam(required = false) String nombreCategoria) {
        return categoriaService.obtenerCategorias(idUsuario, nombreCategoria);
    }

}
