package com.example.saltitantes.funcional;

import com.example.saltitantes.model.service.SimuladorService;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

/**
 * Testes Funcionais - Validam o funcionamento correto dos métodos principais do sistema.
 * 
 * Focam em:
 * - Execução correta da simulação
 * - Retorno adequado de resultados
 * - Funcionamento dos algoritmos principais
 * - Integridade dos dados durante o processamento
 */
public class TesteFuncional {

    /**
     * T6 a T8 — Testes de funcionamento da simulação
     * 
     * Valida que a simulação executa corretamente:
     * - Retorna o número correto de iterações
     * - Mantém a quantidade adequada de criaturas por iteração
     * - Processa diferentes cenários de simulação
     */
    @ParameterizedTest
    @MethodSource("simulacaoProvider")
    void testFuncionamentoSimulacao(int nCriaturas, int iteracoes, int iteracaoParaVerificar, int qtdEsperada) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(nCriaturas);

        var resposta = simulador.simular(iteracoes);

        // Verificar se o número de iterações retornadas está correto
        assertThat(resposta)
            .as("Simulação deve retornar exatamente o número de iterações solicitadas")
            .hasSize(iteracoes);

        // Verificar se o estado das criaturas está consistente na iteração específica
        assertThat(resposta.get(iteracaoParaVerificar).getCriaturas())
            .as("Iteração %d deve conter %d criaturas", iteracaoParaVerificar, qtdEsperada)
            .hasSize(qtdEsperada);
    }

    static Stream<Arguments> simulacaoProvider() {
        return Stream.of(
                of(2, 1, 0, 2),   // T6: Caso simples - 2 criaturas, 1 iteração
                of(3, 3, 2, 3),   // T7: Múltiplas criaturas e iterações, verificar última
                of(5, 5, 1, 5)    // T8: Cenário maior, verificar iteração intermediária
        );
    }

    /**
     * Teste de funcionamento da inicialização do simulador.
     * 
     * Valida que a inicialização:
     * - Configura corretamente o estado inicial
     * - Limpa estados anteriores
     * - Prepara o sistema para nova simulação
     */
    @ParameterizedTest
    @MethodSource("inicializacaoFuncionalProvider")
    void testFuncionamentoInicializacao(int nCriaturas) {
        SimuladorService simulador = new SimuladorService();
        
        // Primeira inicialização
        simulador.inicializar(nCriaturas);
        assertThat(simulador.getCriaturasParaTeste())
            .as("Primeira inicialização deve criar %d criaturas", nCriaturas)
            .hasSize(nCriaturas);
        
        // Executar uma simulação para criar histórico
        simulador.simular(1);
        assertThat(simulador.getHistoricoSimulacoes())
            .as("Após simulação deve haver histórico")
            .isNotEmpty();
        
        // Segunda inicialização deve limpar o estado anterior
        simulador.inicializar(nCriaturas);
        assertThat(simulador.getHistoricoSimulacoes())
            .as("Nova inicialização deve limpar histórico anterior")
            .isEmpty();
        assertThat(simulador.getCriaturasParaTeste())
            .as("Nova inicialização deve recriar as criaturas")
            .hasSize(nCriaturas);
    }

    static Stream<Arguments> inicializacaoFuncionalProvider() {
        return Stream.of(
            Arguments.of(2),
            Arguments.of(5),
            Arguments.of(10)
        );
    }

    /**
     * Teste de funcionamento do guardião.
     * 
     * Valida que o guardião:
     * - É criado corretamente durante a inicialização
     * - Tem os atributos iniciais corretos
     * - É incluído no sistema
     */
    @ParameterizedTest
    @MethodSource("guardiaoFuncionalProvider")
    void testFuncionamentoGuardiao(int nCriaturas) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(nCriaturas);
        
        var guardiao = simulador.getGuardiaoParaTeste();
        
        assertThat(guardiao)
            .as("Guardião deve ser criado durante a inicialização")
            .isNotNull();
        
        assertThat(guardiao.getId())
            .as("Guardião deve ter ID = nCriaturas + 1")
            .isEqualTo(nCriaturas + 1);
        
        assertThat(guardiao.getOuro())
            .as("Guardião deve começar com 0 ouro")
            .isEqualTo(0);
        
        assertThat(guardiao.getPosicaox())
            .as("Guardião deve começar na posição 0")
            .isEqualTo(0.0);
    }

    static Stream<Arguments> guardiaoFuncionalProvider() {
        return Stream.of(
            Arguments.of(2),
            Arguments.of(10),
            Arguments.of(50)
        );
    }
}
