package com.example.saltitantes.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimularResponseDTO {
    private int iteracao;
    private CriaturasDTO[] criaturas;
    private List<ClusterDTO> clusters;
    private GuardiaoDTO guardiao;
    private boolean simulacaoBemSucedida;
}
