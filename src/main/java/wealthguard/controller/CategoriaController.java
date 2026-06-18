package wealthguard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import wealthguard.dto.CategoriaRequestDTO;
import wealthguard.dto.CategoriaResponseDTO;
import wealthguard.service.ICategoriaService;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
        RequestMethod.OPTIONS }, allowedHeaders = "*")
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @PostMapping("/crear")
    public CategoriaResponseDTO crearCategoria(
            @Valid @RequestBody CategoriaRequestDTO nombreCategoria,
            @RequestParam String nickUsuario,
            @RequestParam String nickContrasena) {
        return categoriaService.crearCategoria(nombreCategoria, nickUsuario, nickContrasena);
    }

    @PutMapping("/editar/{id}")
    public CategoriaResponseDTO editarCategoria(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequestDTO nombreCategoria,
            @RequestParam String nickUsuario,
            @RequestParam String nickContrasena) {
        return categoriaService.editarCategoria(id, nombreCategoria, nickUsuario, nickContrasena);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Boolean> eliminarCategoria(
            @PathVariable Integer id,
            @RequestParam String nickUsuario,
            @RequestParam String nickContrasena) {
        boolean eliminado = categoriaService.eliminarCategoria(id, nickUsuario, nickContrasena);
        return eliminado ? ResponseEntity.ok(true) : ResponseEntity.notFound().build();
    }

    @GetMapping("/listar")
    public List<CategoriaResponseDTO> obtenerCategorias(
            @RequestParam String nickUsuario,
            @RequestParam String nickContrasena) {
        return categoriaService.obtenerCategorias(nickUsuario, nickContrasena);
    }
}