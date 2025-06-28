package com.example.saltitantes.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParametrosDTO {

    @NotNull
    private int quantidade;
    @NotNull
    private int iteracoes;

    private String loginUsuario; // Login do usuário executando a simulação (opcional)
}
