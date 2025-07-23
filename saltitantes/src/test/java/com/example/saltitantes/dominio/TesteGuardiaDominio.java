package com.example.saltitantes.dominio;

import com.example.saltitantes.model.entity.Guardiao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

/**
 * Testes de Domínio para Guardião - Validam regras de negócio do guardião.
 * 
 * Cobertura:
 * - Inicialização correta
 * - Comportamento de movimento
 * - Absorção de ouro dos clusters
 * - Detecção de posições ocupadas
 */
public class TesteGuardiaDominio {

    private Guardiao guardiao;

    @BeforeEach
    void setUp() {
        guardiao = new Guardiao(10); // ID exemplo
    }

    /**
     * Testa inicialização do guardião.
     * 
     * @pre ID fornecido para criação
     * @post Guardião criado com ID correto, ouro inicial 1000000, posição 0
     */
    @Test
    void testInicializacaoGuardiao() {
        // Given/When
        Guardiao novoGuardiao = new Guardiao(5);

        // Then
        assertThat(novoGuardiao.getId())
                .as("ID deve ser definido corretamente")
                .isEqualTo(5);

        assertThat(novoGuardiao.getOuro())
                .as("Ouro inicial deve permitir movimento")
                .isEqualTo(1000000);

        assertThat(novoGuardiao.getPosicaox())
                .as("Posição inicial deve ser zero")
                .isEqualTo(0.0);
    }

    /**
     * Testa comportamento de movimento do guardião.
     * 
     * @param ouroGuardiao Quantidade de ouro do guardião
     * @pre Guardião com quantidade específica de ouro
     * @post Movimento ocorre apenas se ouro > 0
     */
    @ParameterizedTest
    @ValueSource(ints = { 0, 100000, 1000000, 5000000 })
    void testMovimentoGuardiao(int ouroGuardiao) {
        // Given
        guardiao.getOuro(); // Reset para o valor fornecido
        guardiao = new Guardiao(10);
        guardiao.adicionarOuro(ouroGuardiao - guardiao.getOuro());

        double posicaoInicial = guardiao.getPosicaox();

        // When
        guardiao.moverX();

        // Then
        if (ouroGuardiao > 0) {
            assertThat(guardiao.getPosicaox())
                    .as("Guardião com ouro > 0 deve se mover")
                    .isNotEqualTo(posicaoInicial);
        } else {
            assertThat(guardiao.getPosicaox())
                    .as("Guardião sem ouro não deve se mover")
                    .isEqualTo(posicaoInicial);
        }
    }

    /**
     * Testa absorção de ouro pelo guardião.
     * 
     * @param ouroInicial   Ouro inicial do guardião
     * @param ouroAbsorvido Ouro a ser absorvido
     * @param ouroEsperado  Ouro esperado após absorção
     * @pre Guardião com ouro inicial, ouro para absorver
     * @post Ouro do guardião incrementado corretamente
     */
    @ParameterizedTest
    @MethodSource("absorcaoOuroProvider")
    void testAbsorcaoOuro(int ouroInicial, int ouroAbsorvido, int ouroEsperado) {
        // Given
        guardiao.adicionarOuro(ouroInicial - guardiao.getOuro());

        // When
        guardiao.adicionarOuro(ouroAbsorvido);

        // Then
        assertThat(guardiao.getOuro())
                .as("Ouro deve ser acumulado corretamente")
                .isEqualTo(ouroEsperado);
    }

    static Stream<Arguments> absorcaoOuroProvider() {
        return Stream.of(
                of(1000000, 500000, 1500000), // Absorção normal
                of(1000000, 1000000, 2000000), // Absorção igual ao inicial
                of(1000000, 0, 1000000), // Absorção zero
                of(1000000, 5000000, 6000000) // Absorção grande
        );
    }

    /**
     * Testa detecção de posições ocupadas.
     * 
     * @param posicaoGuardiao Posição do guardião
     * @param posicaoAlvo     Posição alvo a verificar
     * @param esperaOcupada   Se espera que a posição seja considerada ocupada
     * @pre Guardião em posição específica
     * @post Detecção correta considerando tolerância
     */
    @ParameterizedTest
    @MethodSource("posicaoOcupadaProvider")
    void testDeteccaoPosicaoOcupada(double posicaoGuardiao, double posicaoAlvo, boolean esperaOcupada) {
        // Given
        guardiao.setPosicaox(posicaoGuardiao);

        // When
        boolean resultado = guardiao.estaNaPosicao(posicaoAlvo);

        // Then
        assertThat(resultado)
                .as("Detecção de posição ocupada deve considerar tolerância")
                .isEqualTo(esperaOcupada);
    }

    static Stream<Arguments> posicaoOcupadaProvider() {
        return Stream.of(
                of(0.0, 0.0, true), // Posições exatamente iguais
                of(100.0, 100.0, true), // Posições iguais positivas
                of(-50.0, -50.0, true), // Posições iguais negativas
                of(0.0, 1e-11, true), // Diferença dentro da tolerância
                of(0.0, 1e-9, false), // Diferença fora da tolerância
                of(100.0, 200.0, false), // Posições claramente diferentes
                of(-100.0, 100.0, false) // Posições opostas
        );
    }

    /**
     * Testa comportamento com múltiplas absorções.
     * 
     * @pre Guardião com ouro inicial
     * @post Ouro acumulado corretamente após múltiplas absorções
     */
    @Test
    void testMultiplasAbsorcoes() {
        // Given
        int ouroInicial = guardiao.getOuro();
        int[] valoresAbsorvidos = { 300000, 500000, 200000, 800000 };
        int somaAbsorvida = 1800000;

        // When
        for (int valor : valoresAbsorvidos) {
            guardiao.adicionarOuro(valor);
        }

        // Then
        assertThat(guardiao.getOuro())
                .as("Múltiplas absorções devem ser acumuladas")
                .isEqualTo(ouroInicial + somaAbsorvida);
    }

    /**
     * Testa invariantes do guardião.
     * 
     * @pre Guardião criado com ID específico
     * @post ID imutável, ouro não-negativo após operações
     */
    @Test
    void testInvariantesGuardiao() {
        // Given
        int idOriginal = guardiao.getId();

        // When - Operações que não devem alterar ID
        guardiao.moverX();
        guardiao.adicionarOuro(500000);
        guardiao.setPosicaox(123.45);

        // Then
        assertThat(guardiao.getId())
                .as("ID do guardião deve ser imutável")
                .isEqualTo(idOriginal);

        assertThat(guardiao.getOuro())
                .as("Ouro nunca deve ser negativo")
                .isNotNegative();
    }
}
