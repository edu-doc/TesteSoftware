package com.example.saltitantes.model.dto;

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

    public int getIdentificador() {
        return identificador;
    }

    public int getOuro() {
        return ouro;
    }

    public double getPosicaox() {
        return posicaox;
    }

    public double getPosicaoy() {
        return posicaoy;
    }
}
