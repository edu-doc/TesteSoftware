package com.example.saltitantes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para representar o guardião do horizonte nas respostas da API.
 */
@Getter
@Setter
@AllArgsConstructor
public class GuardiaoDTO {

    private int id;
    private int ouro;
    private double posicaox;
    private int idClusterEliminado; // ID do cluster eliminado na iteração (-1 se nenhum)
}
