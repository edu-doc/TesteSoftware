package com.example.saltitantes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParametrosDTO {

    private int quantidade;
    private int iteracoes;
    private String loginUsuario; // Login do usuário executando a simulação (opcional)
}
