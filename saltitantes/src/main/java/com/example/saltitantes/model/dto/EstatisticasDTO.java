package com.example.saltitantes.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para estatísticas da simulação.
 */
@Getter
@Setter
@AllArgsConstructor
public class EstatisticasDTO {

    private List<UsuarioDTO> pontuacaoUsuarios;
    private int quantidadeTotalSimulacoes;
    private double mediaSimulacoesSucessoUsuario;
    private double mediaTotalSimulacoesSucesso;
    private int totalUsuarios;
}
