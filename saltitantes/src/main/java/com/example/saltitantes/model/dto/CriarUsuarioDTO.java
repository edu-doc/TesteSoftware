package com.example.saltitantes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para requisições de criação de usuário.
 */
@Getter
@Setter
@AllArgsConstructor
public class CriarUsuarioDTO {

    private String login;
    private String senha;
    private String avatar;
}
