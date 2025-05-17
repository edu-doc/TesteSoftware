package com.example.saltitantes.model.entity;

import java.util.concurrent.ThreadLocalRandom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Criaturas {

    static int contador = 0;
    int id;
    int ouro;
    double posicaox;
    double posicaoy;

    public Criaturas() {
        this.id = count();
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

    public void adicionarOuro(int quantidade) {
        this.ouro += quantidade;
    }

    public void perderOuro(int quantidade) {
        this.ouro -= quantidade;
    }
    public static void resetarContador() {
    contador = 1;
}


}
