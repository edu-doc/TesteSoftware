package com.example.saltitantes.model.dto;

import lombok.*;

/**
 * DTO para resposta de login.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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
