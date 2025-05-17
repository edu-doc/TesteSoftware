package com.example.saltitantes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimularResponseDTO {
    private int iteracao;
    private CriaturasDTO criaturas[];
}
