package com.example.saltitantes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para login de usuário.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {

    private String login;
    private String senha;
}
