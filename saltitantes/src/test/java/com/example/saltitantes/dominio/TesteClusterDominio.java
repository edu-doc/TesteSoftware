package com.example.saltitantes.dominio;

import com.example.saltitantes.model.entity.Cluster;
import com.example.saltitantes.model.entity.Criaturas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

/**
 * Testes de Domínio para Clusters - Validam regras de negócio dos clusters.
 * 
 * Cobertura:
 * - Formação de clusters
 * - Soma de ouro
 * - Identificação única
 * - Comportamento de movimento
 */
public class TesteClusterDominio {

    private Criaturas criatura1;
    private Criaturas criatura2;

    @BeforeEach
    void setUp() {
        Criaturas.resetarContador();
        criatura1 = new Criaturas();
        criatura2 = new Criaturas();
    }

    /**
     * Testa a formação básica de cluster.
     * 
     * @pre Duas criaturas com ouro específico
     * @post Cluster formado com ouro total correto e IDs das criaturas
     */
    @Test
    void testFormacaoClusterBasico() {
        // Given
        criatura1.perderOuro(500000); // Fica com 500000
        criatura2.adicionarOuro(200000); // Fica com 1200000

        // When
        Cluster cluster = new Cluster(criatura1, criatura2);

        // Then
        assertThat(cluster.getOuroTotal())
                .as("Ouro total deve ser a soma das criaturas")
                .isEqualTo(1700000);

        assertThat(cluster.getIdscriaturas())
                .as("Cluster deve conter IDs das duas criaturas")
                .hasSize(2)
                .contains(criatura1.getId(), criatura2.getId());

        assertThat(cluster.getIdCluster())
                .as("ID do cluster deve ser positivo")
                .isPositive();
    }

    /**
     * Testa adição de criatura ao cluster existente.
     * 
     * @pre Cluster com duas criaturas, terceira criatura disponível
     * @post Terceira criatura adicionada, ouro total incrementado
     */
    @Test
    void testAdicaoTerceiraCriatura() {
        // Given
        Cluster cluster = new Cluster(criatura1, criatura2);
        Criaturas criatura3 = new Criaturas();
        criatura3.adicionarOuro(500000); // Fica com 1500000

        int ouroAnterior = cluster.getOuroTotal();

        // When
        cluster.adicionarCriatura(criatura3);

        // Then
        assertThat(cluster.getOuroTotal())
                .as("Ouro deve ser incrementado com a nova criatura")
                .isEqualTo(ouroAnterior + 1500000);

        assertThat(cluster.getIdscriaturas())
                .as("Cluster deve conter ID da terceira criatura")
                .hasSize(3)
                .contains(criatura3.getId());
    }

    /**
     * Testa comportamento de movimento do cluster.
     * 
     * @pre Cluster formado com ouro específico
     * @post Cluster se move conforme ouro disponível
     */
    @ParameterizedTest
    @MethodSource("movimentoClusterProvider")
    void testMovimentoCluster(int ouroCluster, boolean deveMover) {
        // Given
        criatura1.perderOuro(criatura1.getOuro() - ouroCluster / 2);
        criatura2.perderOuro(criatura2.getOuro() - ouroCluster / 2);

        Cluster cluster = new Cluster(criatura1, criatura2);
        double posicaoInicial = cluster.getPosicaox();

        // When
        cluster.moverX();

        // Then
        if (deveMover && ouroCluster > 0) {
            assertThat(cluster.getPosicaox())
                    .as("Cluster com ouro deve se mover")
                    .isNotEqualTo(posicaoInicial);
        } else if (ouroCluster == 0) {
            assertThat(cluster.getPosicaox())
                    .as("Cluster sem ouro não deve se mover")
                    .isEqualTo(posicaoInicial);
        }
    }

    static Stream<Arguments> movimentoClusterProvider() {
        return Stream.of(
                of(0, false), // Sem ouro - não move
                of(100000, true), // Pouco ouro - move pouco
                of(1000000, true), // Ouro normal - movimento normal
                of(5000000, true) // Muito ouro - movimento amplo
        );
    }

    /**
     * Testa roubo de ouro pelo cluster.
     * 
     * @pre Cluster formado, ouro disponível para roubar
     * @post Ouro do cluster incrementado com valor roubado
     */
    @Test
    void testRouboOuroCluster() {
        // Given
        Cluster cluster = new Cluster(criatura1, criatura2);
        int ouroParaRoubar = 800000;

        // When
        cluster.roubarOuro(ouroParaRoubar);

        // Then
        assertThat(cluster.getOuroTotal())
                .as("Cluster deve ter seu ouro aumentado")
                .isEqualTo(2000000 + ouroParaRoubar);
    }

    /**
     * Testa regras de domínio para posicionamento do cluster.
     * 
     * @pre Primeira criatura em posição específica
     * @post Cluster assume posição da primeira criatura
     */
    @Test
    void testPosicionamentoCluster() {
        // Given
        criatura1.moverX(); // Mover para posição diferente de 0
        double posicaoEsperada = criatura1.getPosicaox();

        // When
        Cluster cluster = new Cluster(criatura1, criatura2);

        // Then
        assertThat(cluster.getPosicaox())
                .as("Cluster deve assumir posição da primeira criatura")
                .isEqualTo(posicaoEsperada);
    }
}
