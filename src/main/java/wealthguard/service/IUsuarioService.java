package wealthguard.service;

import java.util.List;

import wealthguard.dto.UsuarioRequestDTO;
import wealthguard.dto.UsuarioResponseDTO;
import wealthguard.exception.UsuarioException;

public interface IUsuarioService {

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param usuarioRequestDTO Datos de entrada del usuario
     * @return UsuarioResponseDTO con los datos creados
     * @throws UsuarioException si el nick ya está en uso
     */
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioRequestDTO) throws UsuarioException;

    /**
     * Actualiza los datos del perfil del usuario. El usuario DEBE tener ID para
     * poder actualizarse.
     *
     * @param idUsuario ID del usuario a actualizar
     * @param usuarioRequestDTO Datos nuevos del usuario
     * @return UsuarioResponseDTO con los datos actualizados
     * @throws UsuarioException si el usuario no existe o el nick ya está en uso
     */
    public UsuarioResponseDTO actualizarUsuario(int idUsuario, UsuarioRequestDTO usuarioRequestDTO) throws UsuarioException;

    /**
     * Elimina de forma permanente la cuenta del usuario y todos sus datos
     * asociados. Operación irreversible.
     *
     * @param idUsuario ID del usuario a eliminar
     * @return true si la eliminación fue exitosa, false si no se encontró el
     * usuario
     */
    public boolean eliminarCuenta(int idUsuario);

    /**
     * Genera un fichero con todo el historial financiero del usuario para
     * cumplir con el derecho de portabilidad (RGPD).
     *
     * @param idUsuario ID del usuario
     * @return Array de bytes del fichero generado (PDF o CSV)
     */
    public byte[] exportarDatos(int idUsuario);

    /**
     * Cambia la contraseña del usuario siguiendo estos pasos: 1. Verifica que
     * passwordAntigua coincide con la almacenada (BCrypt) 2. Hashea
     * passwordNueva con BCrypt 3. Actualiza fechaUltimoCambioPassword a
     * LocalDateTime.now()
     *
     * @param idUsuario ID del usuario
     * @param passwordAntigua Contraseña actual en texto plano para verificar
     * @param passwordNueva Nueva contraseña en texto plano para hashear y
     * guardar
     * @return true si el cambio fue exitoso
     * @throws UsuarioException si la contraseña antigua es incorrecta o el
     * usuario no existe
     */
    public boolean cambiarPassword(int idUsuario, String passwordAntigua, String passwordNueva) throws UsuarioException;

    /**
     * Carga los datos del usuario para mostrarlos en la pantalla de perfil.
     *
     * @param idUsuario ID del usuario autenticado
    * @return UsuarioResponseDTO con todos sus datos, incluida la URL de fotoPerfil
     */
    public UsuarioResponseDTO obtenerPerfil(int idUsuario);

    /**
    * Lista los usuarios registrados en el sistema.
    *
    * @return Lista de UsuarioResponseDTO
    */
    public List<UsuarioResponseDTO> listarUsuarios();

    /**
     * Actualiza la foto de perfil del usuario. Guarda los bytes de la imagen en
     * servidor o S3 y almacena la URL resultante en el campo fotoPerfil del
     * usuario.
     *
     * @param idUsuario ID del usuario
     * @param imagen Bytes de la imagen (JPG/PNG)
     * @return URL o ruta donde quedó almacenada la imagen
     * @throws UsuarioException si el usuario no existe
     */
    public String actualizarFotoPerfil(int idUsuario, byte[] imagen) throws UsuarioException;

}
