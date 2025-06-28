package com.example.saltitantes.model.entity;

import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa o guardião do horizonte na simulação de criaturas saltitantes.
 * O guardião é uma criatura especial que pode eliminar clusters e absorver seu
 * ouro.
 */
@Getter
@Setter
public class Guardiao {

    private int id;
    private int ouro;
    private double posicaox;

    /**
     * Construtor do guardião.
     * 
     * @param id identificador único do guardião (deve ser n+1 onde n é o número de
     *           criaturas)
     */
    public Guardiao(int id) {
        this.id = id;
        this.ouro = 1000000; // Mesmo valor inicial das criaturas para que possa se mover
        this.posicaox = 0.0;
    }

    /**
     * Move o guardião no horizonte usando a mesma lógica das criaturas saltitantes.
     * A nova posição é calculada como: x = x + r * ouro
     * onde r é um número aleatório entre -1 e 1.
     */
    public void moverX() {
        double r = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
        this.posicaox += r * this.ouro;
    }

    /**
     * Adiciona ouro ao guardião (usado quando elimina um cluster).
     * 
     * @param quantidade quantidade de ouro a ser adicionada
     */
    public void adicionarOuro(int quantidade) {
        this.ouro += quantidade;
    }

    /**
     * Verifica se o guardião está na mesma posição que outra entidade.
     * 
     * @param posicao posição a ser verificada
     * @return true se as posições são iguais (considerando precisão de ponto
     *         flutuante)
     */
    public boolean estaNaPosicao(double posicao) {
        return Math.abs(this.posicaox - posicao) < 1e-10; // Tolerância para comparação de double
    }
}
