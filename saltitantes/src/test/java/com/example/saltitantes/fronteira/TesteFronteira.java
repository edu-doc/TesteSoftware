package com.example.saltitantes.fronteira;

import com.example.saltitantes.model.dto.CriaturasDTO;
import com.example.saltitantes.model.service.SimuladorService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de Fronteira - Validam limites de entrada e comportamento em extremos.
 * 
 * Focam em:
 * - Casos limite (valores mínimos e máximos)
 * - Situações extremas do sistema
 * - Comportamento em bordas dos intervalos válidos
 */
public class TesteFronteira {

    /**
     * Testa o comportamento quando não há vizinhas disponíveis para roubo.
     * 
     * Caso limite: apenas uma criatura no sistema
     * Valida que o sistema lida corretamente com ausência de interações.
     */
    @Test
    void testSemVizinhaNaoRoubada() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(2);
        // Remove uma criatura para simular o caso extremo de apenas uma
        simulador.getCriaturasParaTeste().remove(1);
        
        var resultado = simulador.simular(1);
        CriaturasDTO[] criaturas = resultado.get(0).getCriaturas();
        
        assertThat(criaturas.length).isEqualTo(1);
        assertThat(criaturas[0].getIdCriaturaRoubada())
            .as("Quando não há vizinha, idCriaturaRoubada deve ser -1")
            .isEqualTo(-1);
    }

    /**
     * Testa o comportamento com zero iterações.
     * 
     * Caso limite: nenhuma iteração executada
     * Valida que o sistema rejeita adequadamente este caso extremo.
     */
    @ParameterizedTest
    @MethodSource("iteracaoZeroProvider")
    void testIteracaoZeroComoCasoLimite(int nCriaturas) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(nCriaturas);

        assertThatThrownBy(() -> simulador.simular(0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("iterações");
    }

    static Stream<Arguments> iteracaoZeroProvider() {
        return Stream.of(
            Arguments.of(2),   // mínimo de criaturas
            Arguments.of(5),   // valor médio
            Arguments.of(10)   // valor maior
        );
    }

    /**
     * Testa os limites exatos dos intervalos válidos para inicialização.
     * 
     * Foca especificamente nos valores de fronteira:
     * - Limite inferior válido (2)
     * - Limite superior válido (1000)
     * - Valores imediatamente fora dos limites
     */
    @ParameterizedTest
    @MethodSource("valoresFronteiraProvider")
    void testValoresExatosDeFronteira(int n, boolean devePassar, String categoria) {
        SimuladorService simulador = new SimuladorService();

        if (devePassar) {
            assertThatCode(() -> simulador.inicializar(n))
                .as("Valor de fronteira válido (%s) deve ser aceito", categoria)
                .doesNotThrowAnyException();
        } else {
            assertThatThrownBy(() -> simulador.inicializar(n))
                .as("Valor fora da fronteira (%s) deve ser rejeitado", categoria)
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    static Stream<Arguments> valoresFronteiraProvider() {
        return Stream.of(
            Arguments.of(1, false, "abaixo do limite inferior"),
            Arguments.of(2, true, "limite inferior válido"),
            Arguments.of(1000, true, "limite superior válido"),
            Arguments.of(1001, false, "acima do limite superior")
        );
    }

    /**
     * Testa comportamento com valores extremos de iterações.
     * 
     * Valida os casos limite para número de iterações:
     * - Mínimo válido (1)
     * - Máximo válido (1000)
     * - Valores fora dos limites
     */
    @ParameterizedTest
    @MethodSource("iteracoesFronteiraProvider")
    void testIteracoesCasosLimite(int iteracoes, boolean devePassar, String categoria) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(2); // Usar mínimo válido de criaturas

        if (devePassar) {
            assertThatCode(() -> simulador.simular(iteracoes))
                .as("Iterações de fronteira válidas (%s) devem ser aceitas", categoria)
                .doesNotThrowAnyException();
        } else {
            assertThatThrownBy(() -> simulador.simular(iteracoes))
                .as("Iterações fora da fronteira (%s) devem ser rejeitadas", categoria)
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    static Stream<Arguments> iteracoesFronteiraProvider() {
        return Stream.of(
            Arguments.of(0, false, "zero iterações"),
            Arguments.of(1, true, "mínima iteração válida"),
            Arguments.of(1000, true, "máxima iteração válida"),
            Arguments.of(1001, false, "acima do limite superior")
        );
    }
}
