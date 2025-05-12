package com.example.saltitantes.model.dto;

public class ParametrosDTO {

    private int quantidade;
    private int iteracoes;

    ParametrosDTO(int quantidade, int iteracoes) {
        this.quantidade = quantidade;
        this.iteracoes = iteracoes;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getIteracoes() {
        return iteracoes;
    }

}
