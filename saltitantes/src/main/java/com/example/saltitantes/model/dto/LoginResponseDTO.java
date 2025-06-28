package com.example.saltitantes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para resposta de login.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String message;
    private String login;
    private UsuarioDTO usuario;

    public LoginResponseDTO(String message, UsuarioDTO usuario) {
        this.message = message;
        this.usuario = usuario;
        this.login = usuario != null ? usuario.getLogin() : null;
    }
}
