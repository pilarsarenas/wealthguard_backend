package wealthguard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wealthguard.dto.LoginRequestDTO;
import wealthguard.exception.UsuarioException;

@Service
public class LoginService {

    @Autowired
    private IUsuarioService usuarioService;

    public void verificar(String nickUsuario, String nickContrasena) {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsuario(nickUsuario);
        loginRequestDTO.setPass(nickContrasena);

        try {
            usuarioService.login(loginRequestDTO);
        } catch (UsuarioException e) {
            throw new RuntimeException("Credenciales inválidas: Acceso denegado.");
        }
    }
}