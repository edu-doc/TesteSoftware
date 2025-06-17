package com.example.saltitantes.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Usuario {

    String login;
    String senha;
    String avatar;
    int pontuação;

    void adicionarPontos(int pontos) {
        this.pontuação += pontos;
    }

}
