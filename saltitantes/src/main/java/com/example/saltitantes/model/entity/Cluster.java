package com.example.saltitantes.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa um cluster de criaturas saltitantes.
 * Um cluster é formado quando duas ou mais criaturas ocupam a mesma posição.
 */
@Getter
@Setter
public class Cluster {

    private List<Integer> idscriaturas; // IDs das criaturas que formam o cluster
    private int ouroTotal;
    private double posicaox;
    private int idCluster;

    /**
     * Construtor do cluster a partir de duas criaturas.
     * 
     * @param criatura1 primeira criatura do cluster
     * @param criatura2 segunda criatura do cluster
     */
    public Cluster(Criaturas criatura1, Criaturas criatura2) {
        this.idscriaturas = new ArrayList<>();
        this.idscriaturas.add(criatura1.getId());
        this.idscriaturas.add(criatura2.getId());
        this.ouroTotal = criatura1.getOuro() + criatura2.getOuro();
        this.posicaox = criatura1.getPosicaox(); // Ambas têm a mesma posição
        this.idCluster = gerarIdCluster();
    }

    /**
     * Adiciona uma criatura ao cluster existente.
     * 
     * @param criatura criatura a ser adicionada
     */
    public void adicionarCriatura(Criaturas criatura) {
        this.idscriaturas.add(criatura.getId());
        this.ouroTotal += criatura.getOuro();
    }

    /**
     * Move o cluster no horizonte usando a mesma lógica das criaturas.
     * 
     * @return nova posição do cluster
     */
    public double moverX() {
        double r = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
        this.posicaox += r * this.ouroTotal;
        return this.posicaox;
    }

    /**
     * Rouba metade do ouro da criatura mais próxima.
     * 
     * @param ouroRoubado quantidade de ouro roubada
     */
    public void roubarOuro(int ouroRoubado) {
        this.ouroTotal += ouroRoubado;
    }

    /**
     * Verifica se o cluster está na mesma posição que uma coordenada.
     * 
     * @param posicao posição a ser verificada
     * @return true se as posições são iguais
     */
    public boolean estaNaPosicao(double posicao) {
        return Math.abs(this.posicaox - posicao) < 1e-10;
    }

    /**
     * Gera um ID único para o cluster baseado nos IDs das criaturas.
     * 
     * @return ID único do cluster
     */
    private int gerarIdCluster() {
        return this.idscriaturas.hashCode();
    }

    /**
     * Retorna o número de criaturas no cluster.
     * 
     * @return tamanho do cluster
     */
    public int getTamanho() {
        return this.idscriaturas.size();
    }

    /**
     * Verifica se o cluster contém uma criatura específica.
     * 
     * @param idCriatura ID da criatura a ser verificada
     * @return true se a criatura está no cluster
     */
    public boolean contemCriatura(int idCriatura) {
        return this.idscriaturas.contains(idCriatura);
    }
}
