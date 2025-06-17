package com.example.saltitantes.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para representar um cluster de criaturas nas respostas da API.
 */
@Getter
@Setter
@AllArgsConstructor
public class ClusterDTO {

    private int idCluster;
    private List<Integer> idsCriaturas;
    private int ouroTotal;
    private double posicaox;
    private int idCriaturaRoubada; // ID da criatura de quem o cluster roubou (-1 se nenhuma)
}
