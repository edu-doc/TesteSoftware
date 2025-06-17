package com.example.saltitantes.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um usuário do sistema de simulação.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    private String login;
    private String senha;
    private String avatar;
    private int pontuacao; // Quantidade de simulações bem-sucedidas
    private int totalSimulacoes; // Total de simulações executadas pelo usuário

    /**
     * Construtor para criar um novo usuário.
     * 
     * @param login  login único do usuário
     * @param senha  senha do usuário
     * @param avatar avatar do usuário
     */
    public Usuario(String login, String senha, String avatar) {
        this.login = login;
        this.senha = senha;
        this.avatar = avatar;
        this.pontuacao = 0;
        this.totalSimulacoes = 0;
    }

    /**
     * Incrementa a pontuação quando uma simulação é bem-sucedida.
     */
    public void incrementarPontuacao() {
        this.pontuacao++;
    }

    /**
     * Incrementa o total de simulações executadas.
     */
    public void incrementarTotalSimulacoes() {
        this.totalSimulacoes++;
    }

    /**
     * Calcula a taxa de sucesso do usuário.
     * 
     * @return percentual de simulações bem-sucedidas
     */
    public double getTaxaSucesso() {
        if (totalSimulacoes == 0) {
            return 0.0;
        }
        return (double) pontuacao / totalSimulacoes * 100.0;
    }

    /**
     * Verifica se a senha fornecida está correta.
     * 
     * @param senha senha a ser verificada
     * @return true se a senha está correta
     */
    public boolean verificarSenha(String senha) {
        return this.senha != null && this.senha.equals(senha);
    }
}
