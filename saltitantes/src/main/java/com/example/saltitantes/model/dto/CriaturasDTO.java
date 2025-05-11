package com.example.saltitantes.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriaturasDTO {

    private int identificador;
    private int ouro;
    private double posicaox;
    private double posicaoy;

    public CriaturasDTO(int identificador, int ouro, double posicaox, double posicaoy) {
        this.identificador = identificador;
        this.ouro = ouro;
        this.posicaox = posicaox;
        this.posicaoy = posicaoy;
    }

}
