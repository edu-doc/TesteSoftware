package com.example.saltitantes.model.entity;

import java.util.concurrent.ThreadLocalRandom;

public class Criaturas {

    static int contador = 0;
    int identificador;
    int ouro;
    double posicaox;
    double posicaoy;

    public Criaturas() {
        this.identificador = count();
        this.ouro = 1000000;
        this.posicaox = 0;
        this.posicaoy = 0;
    }

    public int count() {
        contador = contador + 1;
        return contador;
    }

    public void moverX() {
        double r = ThreadLocalRandom.current().nextDouble(-1, 1);
        this.posicaox += r * this.ouro;
    }

    public void moverY() {
        double r = ThreadLocalRandom.current().nextDouble(-1, 1);
        this.posicaoy += r * this.ouro;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Criaturas.contador = contador;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public int getOuro() {
        return ouro;
    }

    public void setOuro(int ouro) {
        this.ouro = ouro;
    }

    public double getPosicaox() {
        return posicaox;
    }

    public void setPosicaox(double posicaox) {
        this.posicaox = posicaox;
    }

    public double getPosicaoy() {
        return posicaoy;
    }

    public void setPosicaoy(double posicaoy) {
        this.posicaoy = posicaoy;
    }

    public void adicionarOuro(int quantidade) {
        this.ouro += quantidade;
    }

    public void perderOuro(int quantidade) {
        this.ouro -= quantidade;
    }

}
