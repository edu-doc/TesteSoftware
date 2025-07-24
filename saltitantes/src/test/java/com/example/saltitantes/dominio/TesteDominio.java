package com.example.saltitantes.dominio;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.example.saltitantes.service.SimuladorService;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

/**
 * Testes de Domínio - Validam regras de negócio e restrições essenciais do
 * sistema.
 * 
 * Focam em:
 * - Limites mínimos e máximos de entrada
 * - Condições de operação válidas
 * - Restrições de negócio fundamentais
 */
public class TesteDominio {

    /**
     * T1 a T5 — Testes de inicialização (domínio e fronteira)
     * 
     * @param n               Número de criaturas
     * @param expectException Se deve lançar exceção
     * @pre Simulador criado
     * @post Inicialização aceita ou exceção lançada conforme regras
     */
    @ParameterizedTest
    @MethodSource("inicializacaoProvider")
    void testInicializacaoRegrasNegocio(int n, boolean expectException) {
        SimuladorService simulador = new SimuladorService();

        if (expectException) {
            assertThatThrownBy(() -> simulador.inicializar(n))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("criaturas");
        } else {
            assertThatCode(() -> simulador.inicializar(n))
                    .doesNotThrowAnyException();
            assertThat(simulador.getHistoricoSimulacoes()).isEmpty();
        }
    }

    static Stream<Arguments> inicializacaoProvider() {
        return Stream.of(
                of(-1, true), // negativo - viola regra de negócio
                of(0, true), // zero - viola regra de negócio
                of(1, true), // insuficiente - viola regra de negócio
                of(2, false), // mínimo válido
                of(999, false), // valor válido no domínio
                of(1000, false), // máximo válido
                of(1001, true) // excede limite - viola regra de negócio
        );
    }

    /**
     * Testa as regras de domínio para número de iterações na simulação.
     * 
     * @param nCriaturas      Número de criaturas
     * @param iteracoes       Número de iterações
     * @param expectException Se deve lançar exceção
     * @pre Sistema inicializado
     * @post Simulação aceita ou exceção lançada conforme regras
     */
    @ParameterizedTest
    @MethodSource("iteracoesDominioProvider")
    void testIteracoesRegrasNegocio(int nCriaturas, int iteracoes, boolean expectException) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(nCriaturas);

        if (expectException) {
            assertThatThrownBy(() -> simulador.simular(iteracoes))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("iterações");
        } else {
            assertThatCode(() -> simulador.simular(iteracoes))
                    .doesNotThrowAnyException();
        }
    }

    static Stream<Arguments> iteracoesDominioProvider() {
        return Stream.of(
                of(2, -1, true), // negativo - viola regra
                of(2, 0, true), // zero - viola regra
                of(2, 1, false), // mínimo válido
                of(2, 500, false), // valor válido
                of(2, 1000, false), // máximo válido
                of(2, 1001, true) // excede limite - viola regra
        );
    }
}
