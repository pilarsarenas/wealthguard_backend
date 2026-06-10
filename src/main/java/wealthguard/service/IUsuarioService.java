package wealthguard.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import wealthguard.dto.LoginRequestDTO;
import wealthguard.dto.LoginResponseDTO;
import wealthguard.dto.UsuarioRequestDTO;
import wealthguard.dto.UsuarioResponseDTO;
import wealthguard.exception.UsuarioException;

public interface IUsuarioService {

    /**
     * Autentica un usuario por nick o email y contraseña.
     *
     * @param loginRequestDTO Credenciales de acceso
     * @return LoginResponseDTO con datos de sesión
     * @throws UsuarioException si las credenciales no son válidas
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws UsuarioException;

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param usuarioRequestDTO Datos de entrada del usuario
     * @return UsuarioResponseDTO con los datos creados
     * @throws UsuarioException si el nick ya está en uso
     */
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioRequestDTO) throws UsuarioException;

    /**
     * Actualiza los datos del perfil del usuario.
     *
     * @param idUsuario         ID del usuario a actualizar
     * @param usuarioRequestDTO Datos nuevos del usuario
     * @return UsuarioResponseDTO con los datos actualizados
     * @throws UsuarioException si el usuario no existe o el nick ya está en uso
     */
    public UsuarioResponseDTO actualizarUsuario(int idUsuario, UsuarioRequestDTO usuarioRequestDTO)
            throws UsuarioException;

    /**
     * Elimina de forma permanente la cuenta del usuario y todos sus datos
     * asociados. Operación irreversible.
     *
     * @param idUsuario ID del usuario a eliminar
     * @return true si la eliminación fue exitosa, false si no se encontró el
     *         usuario
     */
    public boolean eliminarCuenta(int idUsuario);

    /**
     * Genera un fichero con todo el historial financiero del usuario para cumplir
     * con el derecho de portabilidad (RGPD).
     *
     * @param idUsuario ID del usuario
     * @return Array de bytes del fichero generado (PDF o CSV)
     */
    public byte[] exportarDatos(int idUsuario);

    /**
     * Cambia la contraseña del usuario siguiendo estos pasos:
     * 1. Verifica que passwordAntigua coincide con la almacenada.
     * 2. Hashea passwordNueva.
     * 3. Actualiza la fecha del último cambio de contraseña.
     *
     * @param idUsuario       ID del usuario
     * @param passwordAntigua Contraseña actual en texto plano
     * @param passwordNueva   Nueva contraseña en texto plano
     * @return true si el cambio fue exitoso
     * @throws UsuarioException si la contraseña antigua es incorrecta o el usuario
     *                          no existe
     */
    public boolean cambiarPassword(int idUsuario, String passwordAntigua, String passwordNueva)
            throws UsuarioException;

    /**
     * Obtiene los datos del perfil del usuario autenticado.
     *
     * @param idUsuario ID del usuario
     * @return UsuarioResponseDTO con todos sus datos, incluida la URL de la foto
     *         de perfil
     */
    public UsuarioResponseDTO obtenerPerfil(int idUsuario);

    /**
     * Lista todos los usuarios registrados en el sistema.
     *
     * @return Lista de UsuarioResponseDTO
     */
    public List<UsuarioResponseDTO> listarUsuarios();

    /**
     * Actualiza la foto de perfil del usuario.
     * La imagen recibida se almacena en el sistema de archivos o servicio de
     * almacenamiento configurado, y se guarda la URL resultante en el usuario.
     *
     * @param idUsuario ID del usuario
     * @param imagen    Archivo de imagen recibido (JPG, PNG, etc.)
     * @return URL o ruta donde quedó almacenada la imagen
     * @throws UsuarioException si el usuario no existe o la imagen no es válida
     */
    public String actualizarFotoPerfil(int idUsuario, MultipartFile imagen) throws UsuarioException;

}