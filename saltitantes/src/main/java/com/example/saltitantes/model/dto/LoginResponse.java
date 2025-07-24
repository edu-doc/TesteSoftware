package com.example.saltitantes.model.dto;

import lombok.*;

/**
 * DTO para resposta de login simples.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LoginResponse {

    private String message;
    private boolean success;
    private UsuarioDTO usuario;
}
