package wealthguard.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import wealthguard.dto.LoginRequestDTO;
import wealthguard.dto.LoginResponseDTO;
import wealthguard.dto.UsuarioRequestDTO;
import wealthguard.dto.UsuarioResponseDTO;
import wealthguard.entity.UsuarioEntity;
import wealthguard.exception.UsuarioException;
import wealthguard.mapper.UsuarioMapper;
import wealthguard.repository.ObjetivoRepository;
import wealthguard.repository.PresupuestoRepository;
import wealthguard.repository.RecomendacionRepository;
import wealthguard.repository.TransaccionRepository;
import wealthguard.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private UsuarioMapper usuarioMapper;
    @Mock private TransaccionRepository transaccionRepository;
    @Mock private RecomendacionRepository recomendacionRepository;
    @Mock private PresupuestoRepository presupuestoRepository;
    @Mock private ObjetivoRepository objetivoRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private UsuarioEntity usuarioEntity;
    private UsuarioResponseDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(1);
        usuarioEntity.setNickUsuario("juanito");
        usuarioEntity.setEmail("juan@test.com");
        usuarioEntity.setPassword(encoder.encode("password123"));
        usuarioEntity.setNombre("Juan");
        usuarioEntity.setPrimerApellido("García");
        usuarioEntity.setActivo(true);
        usuarioEntity.setFechaRegistro(LocalDateTime.now());
        usuarioEntity.setPreguntaSeguridad("¿Color favorito?");
        usuarioEntity.setRespuestaSeguridad("azul");

        usuarioDTO = new UsuarioResponseDTO();
    }

    // ─── login ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("login: credenciales nulas — lanza UsuarioException")
    void login_credencialesNulas_lanzaExcepcion() {
        assertThatThrownBy(() -> usuarioService.login(null))
                .isInstanceOf(UsuarioException.class);
    }

    @Test
    @DisplayName("login: usuario en blanco — lanza UsuarioException")
    void login_usuarioEnBlanco_lanzaExcepcion() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsuario("  ");
        dto.setPass("pass");

        assertThatThrownBy(() -> usuarioService.login(dto))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Credenciales_invalidas");
    }

    @Test
    @DisplayName("login: por email, usuario no existe — lanza UsuarioException")
    void login_porEmail_usuarioNoExiste_lanzaExcepcion() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsuario("noexiste@test.com");
        dto.setPass("pass");

        when(usuarioRepository.findByEmailIgnoreCase("noexiste@test.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.login(dto))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Usuario_no_encontrado");
    }

    @Test
    @DisplayName("login: por nick, contraseña incorrecta — lanza UsuarioException")
    void login_passwordIncorrecta_lanzaExcepcion() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsuario("juanito");
        dto.setPass("wrongpass");

        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        assertThatThrownBy(() -> usuarioService.login(dto))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Credenciales_incorrectas");
    }

    @Test
    @DisplayName("login: credenciales correctas por nick — devuelve LoginResponseDTO")
    void login_credencialesCorrectasPorNick_devuelveResponse() throws UsuarioException {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsuario("juanito");
        dto.setPass("password123");

        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        LoginResponseDTO resultado = usuarioService.login(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNickUsuario()).isEqualTo("juanito");
        assertThat(resultado.getMensaje()).isEqualTo("Login correcto");
    }

    @Test
    @DisplayName("login: credenciales correctas por email — devuelve LoginResponseDTO")
    void login_credencialesCorrectasPorEmail_devuelveResponse() throws UsuarioException {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsuario("juan@test.com");
        dto.setPass("password123");

        when(usuarioRepository.findByEmailIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuarioEntity));

        LoginResponseDTO resultado = usuarioService.login(dto);

        assertThat(resultado.getEmail()).isEqualTo("juan@test.com");
    }

    // ─── crearUsuario ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("crearUsuario: nick ya existe — lanza UsuarioException")
    void crearUsuario_nickExistente_lanzaExcepcion() {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO();
        requestDTO.setNickUsuario("juanito");

        when(usuarioRepository.findByNickUsuario("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        assertThatThrownBy(() -> usuarioService.crearUsuario(requestDTO))
                .isInstanceOf(UsuarioException.class);
    }

    @Test
    @DisplayName("crearUsuario: nick disponible — guarda con password encriptada y devuelve DTO")
    void crearUsuario_nickDisponible_guardaYdevuelveDTO() throws UsuarioException {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO();
        requestDTO.setNickUsuario("nuevo");
        requestDTO.setPassword("pass123");

        when(usuarioRepository.findByNickUsuario("nuevo")).thenReturn(Optional.empty());
        when(usuarioMapper.convertirAEntity(requestDTO)).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any())).thenReturn(usuarioEntity);
        when(usuarioMapper.convertirADTO(usuarioEntity)).thenReturn(usuarioDTO);

        UsuarioResponseDTO resultado = usuarioService.crearUsuario(requestDTO);

        assertThat(resultado).isNotNull();
        verify(usuarioRepository).save(argThat(u ->
                u.getActivo() == Boolean.TRUE &&
                u.getFechaRegistro() != null
        ));
    }

    // ─── actualizarUsuario ────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizarUsuario: usuario no existe — lanza UsuarioException")
    void actualizarUsuario_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.actualizarUsuario(99, new UsuarioRequestDTO()))
                .isInstanceOf(UsuarioException.class);
    }

    @Test
    @DisplayName("actualizarUsuario: nick ya usado por otro — lanza UsuarioException")
    void actualizarUsuario_nickDuplicado_lanzaExcepcion() {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO();
        requestDTO.setNickUsuario("otro");
        requestDTO.setPassword("password123");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.existsByNickUsuarioAndIdNot("otro", 1)).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.actualizarUsuario(1, requestDTO))
                .isInstanceOf(UsuarioException.class);
    }

    @Test
    @DisplayName("actualizarUsuario: password en blanco — lanza UsuarioException con mensaje Password_obligatoria")
    void actualizarUsuario_passwordEnBlanco_lanzaExcepcion() {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO();
        requestDTO.setNickUsuario("juanito");
        requestDTO.setPassword("  ");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.existsByNickUsuarioAndIdNot("juanito", 1)).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.actualizarUsuario(1, requestDTO))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Password_obligatoria");
    }

    @Test
    @DisplayName("actualizarUsuario: password incorrecta — lanza UsuarioException con mensaje Password_incorrecta")
    void actualizarUsuario_passwordIncorrecta_lanzaExcepcion() {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO();
        requestDTO.setNickUsuario("juanito");
        requestDTO.setPassword("wrongpass");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.existsByNickUsuarioAndIdNot("juanito", 1)).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.actualizarUsuario(1, requestDTO))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Password_incorrecta");
    }

    @Test
    @DisplayName("actualizarUsuario: datos válidos — actualiza conservando fecha registro y activo")
    void actualizarUsuario_datosValidos_actualizaYdevuelveDTO() throws UsuarioException {
        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO();
        requestDTO.setNickUsuario("juanito");
        requestDTO.setPassword("password123");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.existsByNickUsuarioAndIdNot("juanito", 1)).thenReturn(false);
        when(usuarioMapper.convertirAEntity(requestDTO)).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any())).thenReturn(usuarioEntity);
        when(usuarioMapper.convertirADTO(usuarioEntity)).thenReturn(usuarioDTO);

        UsuarioResponseDTO resultado = usuarioService.actualizarUsuario(1, requestDTO);

        assertThat(resultado).isNotNull();
        verify(usuarioRepository).save(argThat(u ->
                u.getFechaRegistro() != null && u.getActivo() != null
        ));
    }

    // ─── eliminarCuenta ───────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminarCuenta: usuario no existe — devuelve false")
    void eliminarCuenta_noExiste_devuelveFalse() {
        when(usuarioRepository.existsById(99)).thenReturn(false);

        boolean resultado = usuarioService.eliminarCuenta(99);

        assertThat(resultado).isFalse();
        verify(usuarioRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("eliminarCuenta: usuario existe — borra todos sus datos y devuelve true")
    void eliminarCuenta_existe_borraYdevuelveTrue() {
        when(usuarioRepository.existsById(1)).thenReturn(true);

        boolean resultado = usuarioService.eliminarCuenta(1);

        assertThat(resultado).isTrue();
        verify(transaccionRepository).deleteByUsuarioId(1);
        verify(recomendacionRepository).deleteByUsuarioId(1);
        verify(presupuestoRepository).deleteByUsuarioId(1);
        verify(objetivoRepository).deleteByUsuarioId(1);
        verify(usuarioRepository).deleteById(1);
    }

    // ─── exportarDatos ────────────────────────────────────────────────────────

    @Test
    @DisplayName("exportarDatos: usuario no existe — lanza RuntimeException")
    void exportarDatos_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.exportarDatos(99))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("exportarDatos: usuario existe — devuelve CSV en bytes no vacío")
    void exportarDatos_existe_devuelveCSV() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));

        byte[] resultado = usuarioService.exportarDatos(1);

        assertThat(resultado).isNotEmpty();
        assertThat(new String(resultado)).contains("juan@test.com");
    }

    // ─── cambiarPassword ──────────────────────────────────────────────────────

    @Test
    @DisplayName("cambiarPassword: usuario no existe — lanza UsuarioException")
    void cambiarPassword_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.cambiarPassword(99, "old", "new"))
                .isInstanceOf(UsuarioException.class);
    }

    @Test
    @DisplayName("cambiarPassword: password antigua incorrecta — lanza UsuarioException")
    void cambiarPassword_passwordAntiguaIncorrecta_lanzaExcepcion() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));

        assertThatThrownBy(() -> usuarioService.cambiarPassword(1, "wrongold", "nueva123"))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Password_incorrecta");
    }

    @Test
    @DisplayName("cambiarPassword: correcta — actualiza password y fecha de cambio")
    void cambiarPassword_correcta_actualizaPasswordYFecha() throws UsuarioException {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));

        boolean resultado = usuarioService.cambiarPassword(1, "password123", "nueva123");

        assertThat(resultado).isTrue();
        verify(usuarioRepository).save(argThat(u ->
                u.getFechaUltimoCambioPassword() != null
        ));
    }

    // ─── obtenerPerfil ────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPerfil: usuario no existe — lanza RuntimeException")
    void obtenerPerfil_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.obtenerPerfil(99))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("obtenerPerfil: usuario existe — devuelve DTO")
    void obtenerPerfil_existe_devuelveDTO() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEntity));
        when(usuarioMapper.convertirADTO(usuarioEntity)).thenReturn(usuarioDTO);

        UsuarioResponseDTO resultado = usuarioService.obtenerPerfil(1);

        assertThat(resultado).isNotNull();
    }

    // ─── listarUsuarios ───────────────────────────────────────────────────────

    @Test
    @DisplayName("listarUsuarios: devuelve todos mapeados")
    void listarUsuarios_devuelveTodosMapeados() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity));
        when(usuarioMapper.convertirADTO(usuarioEntity)).thenReturn(usuarioDTO);

        List<UsuarioResponseDTO> resultado = usuarioService.listarUsuarios();

        assertThat(resultado).hasSize(1);
    }

    // ─── existeNick / existeEmail ─────────────────────────────────────────────

    @Test
    @DisplayName("existeNick: nick existe — devuelve true")
    void existeNick_existente_devuelveTrue() {
        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        assertThat(usuarioService.existeNick("juanito")).isTrue();
    }

    @Test
    @DisplayName("existeNick: nick no existe — devuelve false")
    void existeNick_noExiste_devuelveFalse() {
        when(usuarioRepository.findByNickUsuarioIgnoreCase("nuevo"))
                .thenReturn(Optional.empty());

        assertThat(usuarioService.existeNick("nuevo")).isFalse();
    }

    @Test
    @DisplayName("existeEmail: email existe — devuelve true")
    void existeEmail_existente_devuelveTrue() {
        when(usuarioRepository.findByEmailIgnoreCase("juan@test.com"))
                .thenReturn(Optional.of(usuarioEntity));

        assertThat(usuarioService.existeEmail("juan@test.com")).isTrue();
    }

    // ─── obtenerPreguntaSeguridad ─────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPreguntaSeguridad: usuario no encontrado — lanza UsuarioException")
    void obtenerPreguntaSeguridad_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findByNickUsuarioIgnoreCase("noexiste"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.obtenerPreguntaSeguridad("noexiste"))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Usuario_no_encontrado");
    }

    @Test
    @DisplayName("obtenerPreguntaSeguridad: sin pregunta configurada — lanza UsuarioException")
    void obtenerPreguntaSeguridad_sinPregunta_lanzaExcepcion() {
        usuarioEntity.setPreguntaSeguridad(null);
        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        assertThatThrownBy(() -> usuarioService.obtenerPreguntaSeguridad("juanito"))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Usuario_sin_pregunta_seguridad");
    }

    @Test
    @DisplayName("obtenerPreguntaSeguridad: existe y tiene pregunta — la devuelve")
    void obtenerPreguntaSeguridad_existe_devuelvePregunta() throws UsuarioException {
        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        String resultado = usuarioService.obtenerPreguntaSeguridad("juanito");

        assertThat(resultado).isEqualTo("¿Color favorito?");
    }

    // ─── verificarRespuestaSeguridad ──────────────────────────────────────────

    @Test
    @DisplayName("verificarRespuestaSeguridad: respuesta correcta (case-insensitive) — devuelve true")
    void verificarRespuestaSeguridad_correcta_devuelveTrue() throws UsuarioException {
        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        boolean resultado = usuarioService.verificarRespuestaSeguridad("juanito", "AZUL");

        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("verificarRespuestaSeguridad: respuesta incorrecta — devuelve false")
    void verificarRespuestaSeguridad_incorrecta_devuelveFalse() throws UsuarioException {
        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        boolean resultado = usuarioService.verificarRespuestaSeguridad("juanito", "rojo");

        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("verificarRespuestaSeguridad: respuesta en blanco — devuelve false")
    void verificarRespuestaSeguridad_enBlanco_devuelveFalse() throws UsuarioException {
        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        boolean resultado = usuarioService.verificarRespuestaSeguridad("juanito", "  ");

        assertThat(resultado).isFalse();
    }

    // ─── resetearPassword ─────────────────────────────────────────────────────

    @Test
    @DisplayName("resetearPassword: respuesta incorrecta — lanza UsuarioException Respuesta_incorrecta")
    void resetearPassword_respuestaIncorrecta_lanzaExcepcion() {
        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        assertThatThrownBy(() -> usuarioService.resetearPassword("juanito", "rojo", "nueva123"))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Respuesta_incorrecta");
    }

    @Test
    @DisplayName("resetearPassword: nueva password en blanco — lanza UsuarioException Password_obligatoria")
    void resetearPassword_passwordNuevaEnBlanco_lanzaExcepcion() {
        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        assertThatThrownBy(() -> usuarioService.resetearPassword("juanito", "azul", "  "))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("Password_obligatoria");
    }

    @Test
    @DisplayName("resetearPassword: todo correcto — actualiza password y devuelve true")
    void resetearPassword_correcto_actualizaYdevuelveTrue() throws UsuarioException {
        when(usuarioRepository.findByNickUsuarioIgnoreCase("juanito"))
                .thenReturn(Optional.of(usuarioEntity));

        boolean resultado = usuarioService.resetearPassword("juanito", "azul", "nueva123");

        assertThat(resultado).isTrue();
        verify(usuarioRepository).save(argThat(u ->
                u.getFechaUltimoCambioPassword() != null
        ));
    }
}