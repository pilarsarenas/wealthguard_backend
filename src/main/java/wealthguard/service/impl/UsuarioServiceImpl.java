package wealthguard.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import wealthguard.dto.UsuarioRequestDTO;
import wealthguard.dto.UsuarioResponseDTO;
import wealthguard.entity.CategoriaEntity;
import wealthguard.entity.UsuarioEntity;
import wealthguard.exception.UsuarioException;
import wealthguard.mapper.UsuarioMapper;
import wealthguard.repository.CategoriaRepository;
import wealthguard.repository.UsuarioRepository;
import wealthguard.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioRequestDTO) throws UsuarioException {
        if (usuarioRepository.findByNickUsuario(usuarioRequestDTO.getNickUsuario()).isPresent()) {
            throw new UsuarioException();
        }

        UsuarioEntity usuario = usuarioMapper.convertirAEntity(usuarioRequestDTO);
        usuario.setPassword(passwordEncoder.encode(usuarioRequestDTO.getPassword()));
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setFechaUltimoCambioPassword(LocalDateTime.now());
        usuario.setActivo(true);
        usuario.setEsAdmin(false);
        usuario.setCuentaBloqueada(false);
        usuario.setContadorIntentos(0);

        UsuarioEntity guardado = usuarioRepository.save(usuario);
        return usuarioMapper.convertirADTO(guardado);
    }

    // Actualiza los datos del perfil. Requiere que el usuario ya tenga ID.
    @Override
    public UsuarioResponseDTO actualizarUsuario(int idUsuario, UsuarioRequestDTO usuarioRequestDTO) throws UsuarioException {
        UsuarioEntity existente = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioException());

        if (usuarioRepository.existsByNickUsuarioAndIdNot(usuarioRequestDTO.getNickUsuario(), idUsuario)) {
            throw new UsuarioException();
        }

        UsuarioEntity usuario = usuarioMapper.convertirAEntity(usuarioRequestDTO);
        usuario.setId(idUsuario);

        // Conservamos campos de sistema que no llegan en el request de perfil.
        usuario.setFechaRegistro(existente.getFechaRegistro());
        usuario.setEsAdmin(existente.getEsAdmin());
        usuario.setContadorIntentos(existente.getContadorIntentos());
        usuario.setCuentaBloqueada(existente.getCuentaBloqueada());
        usuario.setActivo(existente.getActivo());
        usuario.setFechaUltimoCambioPassword(existente.getFechaUltimoCambioPassword());

        if (usuarioRequestDTO.getPassword() != null && !usuarioRequestDTO.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(usuarioRequestDTO.getPassword()));
        } else {
            usuario.setPassword(existente.getPassword());
        }

        UsuarioEntity actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.convertirADTO(actualizado);
    }

    // Elimina de forma permanente la cuenta del usuario y todos sus datos asociados.
    @Override
    public boolean eliminarCuenta(int idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            return false;
        }
        usuarioRepository.deleteById(idUsuario);
        return true;
    }

    // Exporta los datos del usuario como CSV (portabilidad RGPD).
    @Override
    public byte[] exportarDatos(int idUsuario) {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String csv = "id,nick,nombre,primerApellido,segundoApellido,email,fechaRegistro,activo\n"
                + usuario.getId() + ","
                + usuario.getNickUsuario() + ","
                + usuario.getNombre() + ","
                + usuario.getPrimerApellido() + ","
                + (usuario.getSegundoApellido() != null ? usuario.getSegundoApellido() : "") + ","
                + usuario.getEmail() + ","
                + usuario.getFechaRegistro() + ","
                + usuario.getActivo();

        return csv.getBytes();
    }

    // Cambia la contraseña verificando la antigua con BCrypt y hasheando la nueva.
    @Override
    public boolean cambiarPassword(int idUsuario, String passwordAntigua, String passwordNueva) throws UsuarioException {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioException());

        if (!passwordEncoder.matches(passwordAntigua, usuario.getPassword())) {
            throw new UsuarioException();
        }

        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuario.setFechaUltimoCambioPassword(LocalDateTime.now());
        usuarioRepository.save(usuario);
        return true;
    }

    // Devuelve los datos del perfil del usuario para la pantalla de perfil.
    @Override
    public UsuarioResponseDTO obtenerPerfil(int idUsuario) {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.convertirADTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::convertirADTO)
                .collect(Collectors.toList());
    }

    // Guarda los bytes de imagen en disco y actualiza la URL de fotoPerfil.
    @Override
    public String actualizarFotoPerfil(int idUsuario, byte[] imagen) throws UsuarioException {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioException());

        try {
            Path directorio = Paths.get("uploads", "fotos-perfil");
            Files.createDirectories(directorio);
            Path rutaArchivo = directorio.resolve("usuario_" + idUsuario + ".jpg");
            Files.write(rutaArchivo, imagen);

            String url = rutaArchivo.toString();
            usuario.setFotoPerfil(url);
            usuarioRepository.save(usuario);
            return url;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen de perfil", e);
        }
    }

    // Elimina una categoría personalizada del usuario, siempre que no sea "General" o "Sin categoría".
    @Override
    public boolean eliminarCategoriaUsuario(int idCategoria, int idUsuario) {
        CategoriaEntity categoria = categoriaRepository.findById(idCategoria).orElse(null);

        if (categoria == null) {
            return false;
        }

        if (categoria.getUsuarioId() == null || categoria.getUsuarioId().getId() != idUsuario) {
            return false;
        }

        String nombre = categoria.getNombre().toLowerCase();
        if (nombre.equals("general") || nombre.equals("sin categoría")) {
            return false;
        }

        categoriaRepository.deleteById(idCategoria);
        return true;
    }

    // Crea una nueva categoría personalizada para el usuario si no existe ya con ese nombre.
    @Override
    public boolean crearCategoriaUsuario(String nombreCategoria, int idUsuario) {
        List<CategoriaEntity> existentes = categoriaRepository.buscarConFiltro(idUsuario, nombreCategoria);

        if (!existentes.isEmpty()) {
            return false;
        }

        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        CategoriaEntity nueva = new CategoriaEntity();
        nueva.setNombre(nombreCategoria);
        nueva.setUsuarioId(usuario);
        categoriaRepository.save(nueva);
        return true;
    }

    // Devuelve la lista de nombres de categorías personalizadas del usuario, excluyendo las globales.
    @Override
    public List<String> obtenerCategoriasUsuario(int idUsuario) {
        return categoriaRepository.buscarConFiltro(idUsuario, null)
                .stream()
                .map(CategoriaEntity::getNombre)
                .filter(nombre -> !nombre.equalsIgnoreCase("general") && !nombre.equalsIgnoreCase("sin categoría"))
                .collect(Collectors.toList());
    }

}

