package com.example.saltitantes.model.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
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

    public void adicionarOuro(int quantidade) {
        this.ouro += quantidade;
    }

    public void perderOuro(int quantidade) {
        this.ouro -= quantidade;
    }

}
