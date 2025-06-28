package com.example.saltitantes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para operações de usuário (sem senha por segurança).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private String login;
    private String avatar;
    private int pontuacao;
    private int totalSimulacoes;
    private double taxaSucesso;
}
