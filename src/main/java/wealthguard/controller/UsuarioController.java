package wealthguard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wealthguard.dto.UsuarioRequestDTO;
import wealthguard.dto.UsuarioResponseDTO;
import wealthguard.exception.UsuarioException;
import wealthguard.service.IUsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/crear")
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(@RequestBody UsuarioRequestDTO requestDTO) {
        try {
            UsuarioResponseDTO creado = usuarioService.crearUsuario(requestDTO);
            return ResponseEntity.ok(creado);
        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/listar")
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    // Obtiene los datos del perfil del usuario autenticado
    @GetMapping("/perfil/{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPerfil(@PathVariable int idUsuario) {
        UsuarioResponseDTO usuario = usuarioService.obtenerPerfil(idUsuario);
        return ResponseEntity.ok(usuario);
    }

    // Actualiza los datos del perfil. El ID se toma de la URL y se inyecta en el body
    @PutMapping("/actualizar/{idUsuario}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable int idUsuario,
            @RequestBody UsuarioRequestDTO requestDTO) {
        try {
            UsuarioResponseDTO actualizado = usuarioService.actualizarUsuario(idUsuario, requestDTO);
            return ResponseEntity.ok(actualizado);
        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Elimina de forma permanente la cuenta del usuario
    @DeleteMapping("/eliminar/{idUsuario}")
    public ResponseEntity<Boolean> eliminarCuenta(@PathVariable int idUsuario) {
        boolean eliminado = usuarioService.eliminarCuenta(idUsuario);
        if (eliminado) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Cambia la contraseña del usuario
    @PutMapping("/cambiar-password/{idUsuario}")
    public ResponseEntity<Boolean> cambiarPassword(
            @PathVariable int idUsuario,
            @RequestParam String passwordAntigua,
            @RequestParam String passwordNueva) {
        try {
            boolean resultado = usuarioService.cambiarPassword(idUsuario, passwordAntigua, passwordNueva);
            return ResponseEntity.ok(resultado);
        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Exporta los datos del usuario como fichero CSV (RGPD)
    @GetMapping("/exportar/{idUsuario}")
    public ResponseEntity<byte[]> exportarDatos(@PathVariable int idUsuario) {
        byte[] datos = usuarioService.exportarDatos(idUsuario);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"datos_usuario_" + idUsuario + ".csv\"")
                .header("Content-Type", "text/csv")
                .body(datos);
    }

    // Actualiza la foto de perfil enviando los bytes de la imagen en el body
    @PutMapping("/foto-perfil/{idUsuario}")
    public ResponseEntity<String> actualizarFotoPerfil(
            @PathVariable int idUsuario,
            @RequestBody byte[] imagen) {
        try {
            String url = usuarioService.actualizarFotoPerfil(idUsuario, imagen);
            return ResponseEntity.ok(url);
        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtiene las categorías personalizadas del usuario
    @GetMapping("/categorias/{idUsuario}")
    public List<String> obtenerCategoriasUsuario(@PathVariable int idUsuario) {
        return usuarioService.obtenerCategoriasUsuario(idUsuario);
    }

    // Crea una nueva categoría personalizada para el usuario
    @PostMapping("/categorias/{idUsuario}")
    public ResponseEntity<Boolean> crearCategoriaUsuario(
            @PathVariable int idUsuario,
            @RequestParam String nombreCategoria) {
        boolean creada = usuarioService.crearCategoriaUsuario(nombreCategoria, idUsuario);
        if (creada) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    // Elimina una categoría personalizada del usuario
    @DeleteMapping("/categorias/{idUsuario}/{idCategoria}")
    public ResponseEntity<Boolean> eliminarCategoriaUsuario(
            @PathVariable int idUsuario,
            @PathVariable int idCategoria) {
        boolean eliminada = usuarioService.eliminarCategoriaUsuario(idCategoria, idUsuario);
        if (eliminada) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

