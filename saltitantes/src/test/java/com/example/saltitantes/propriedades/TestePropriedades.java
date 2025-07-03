package com.example.saltitantes.propriedades;

import com.example.saltitantes.model.dto.CriaturasDTO;
import com.example.saltitantes.model.dto.SimularResponseDTO;
import com.example.saltitantes.model.entity.Criaturas;
import com.example.saltitantes.model.service.SimuladorService;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class TestePropriedades {

    /**
     * PROPRIEDADE: A inicialização com um número válido de criaturas (2 a 1000) deve sempre funcionar.
     */
    @Property(tries = 100)
    void inicializacaoValida(@ForAll @IntRange(min = 2, max = 1000) int n) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(n);

        assertThat(simulador.getCriaturasParaTeste()).hasSize(n);
        assertThat(simulador.getGuardiaoParaTeste()).isNotNull();
    }

    /**
     * PROPRIEDADE: A inicialização com um número de criaturas <= 1 deve sempre lançar exceção.
     */
    @Property
    void inicializacaoInvalidaAbaixoDoLimite(@ForAll @IntRange(max = 1) int n) {
        SimuladorService simulador = new SimuladorService();
        assertThatThrownBy(() -> simulador.inicializar(n))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     * PROPRIEDADE: A simulação com um número de iterações inválido (<= 0 ou > 1000) deve sempre lançar exceção.
     */
    @Property
    void simulacaoComIteracoesInvalidasSempreFalha(@ForAll("iteracoesInvalidas") int iteracoes) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(10); // Inicializa com um estado válido
        assertThatThrownBy(() -> simulador.simular(iteracoes))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Provide
    Arbitrary<Integer> iteracoesInvalidas() {
        // Gera números inválidos combinando dois intervalos distintos
        return Arbitraries.oneOf(
                Arbitraries.integers().lessOrEqual(0),
                Arbitraries.integers().greaterOrEqual(1001)
        );
    }

    // --- Testes de Invariantes de Estado da Simulação ---

    /**
     * PROPRIEDADE: Em qualquer iteração, os IDs de todas as entidades ativas devem ser únicos.
     */
    @Property(tries = 50)
    void idsDeEntidadesSaoSempreUnicos(
            @ForAll @IntRange(min = 2, max = 50) int nCriaturas,
            @ForAll @IntRange(min = 1, max = 20) int iteracoes
    ) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(nCriaturas);
        // IMPORTANTE: Garante que as criaturas não sejam eliminadas pela regra de pouco ouro
        simulador.getCriaturasParaTeste().forEach(c -> c.adicionarOuro(500_000));

        var resultado = simulador.simular(iteracoes);

        for (SimularResponseDTO iteracao : resultado) {
            List<Integer> todosOsIds = new ArrayList<>();
            Arrays.stream(iteracao.getCriaturas()).map(CriaturasDTO::getId).forEach(todosOsIds::add);
            iteracao.getClusters().forEach(cl -> todosOsIds.add(cl.getIdCluster()));
            todosOsIds.add(iteracao.getGuardiao().getId());

            assertThat(todosOsIds)
                    .as("Iteração %d: IDs não devem ter duplicatas", iteracao.getIteracao())
                    .doesNotHaveDuplicates();
        }
    }

    /**
     * PROPRIEDADE: Uma criatura nunca pode ser independente e parte de um cluster ao mesmo tempo.
     */
    @Property(tries = 50)
    void criaturaNaoPodeSerIndependenteEParteDeUmCluster(
            @ForAll @IntRange(min = 10, max = 100) int nCriaturas,
            @ForAll @IntRange(min = 1, max = 30) int iteracoes
    ) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(nCriaturas);
        simulador.getCriaturasParaTeste().forEach(c -> c.adicionarOuro(500_000));

        var resultado = simulador.simular(iteracoes);

        for (SimularResponseDTO iteracao : resultado) {
            List<Integer> idsDeCriaturasEmClusters = iteracao.getClusters().stream()
                    .flatMap(cl -> cl.getIdsCriaturas().stream())
                    .toList();

            // Apenas executa a asserção se houver clusters para verificar.
            if (!idsDeCriaturasEmClusters.isEmpty()) {
                List<Integer> idsDeCriaturasIndependentes = Arrays.stream(iteracao.getCriaturas())
                        .map(CriaturasDTO::getId)
                        .toList();

                assertThat(idsDeCriaturasIndependentes)
                        .as("Iteração %d: Nenhuma criatura independente deve ter seu ID na lista de membros de clusters", iteracao.getIteracao())
                        .doesNotContainAnyElementsOf(idsDeCriaturasEmClusters);
            }
        }
    }

    // --- Testes de Regras de Negócio ---

    /**
     * PROPRIEDADE: Uma entidade nunca deve conseguir roubar outra que tenha 0 de ouro.
     */
    @Property
    void umaEntidadeNuncaRoubaOutraQueNaoTemOuro(@ForAll("cenarioComAlvoSemOuro") SimuladorService simulador) {
        // O provedor "cenarioComAlvoSemOuro" já configura o estado inicial
        List<Criaturas> criaturasIniciais = simulador.getCriaturasParaTeste();
        int idCriaturaAlvoSemOuro = criaturasIniciais.get(1).getId(); // O ID do alvo que não deve ser roubado

        var resultado = simulador.simular(1);

        SimularResponseDTO primeiraIteracao = resultado.get(0);

        // 1. Verifica se alguma CRIATURA roubou o alvo errado
        boolean algumaCriaturaRoubouOAlvo = Arrays.stream(primeiraIteracao.getCriaturas())
                .anyMatch(c -> c.getIdCriaturaRoubada() == idCriaturaAlvoSemOuro);

        // 2. Verifica se algum CLUSTER roubou o alvo errado
        boolean algumClusterRoubouOAlvo = primeiraIteracao.getClusters().stream()
                .anyMatch(cl -> cl.getIdCriaturaRoubada() == idCriaturaAlvoSemOuro);

        // A propriedade falha se qualquer um dos dois (criatura ou cluster) tiver roubado o alvo sem ouro.
        boolean rouboInvalidoOcorreu = algumaCriaturaRoubouOAlvo || algumClusterRoubouOAlvo;

        assertThat(rouboInvalidoOcorreu)
                .as("Nenhuma entidade (criatura ou cluster) deveria ter conseguido roubar o alvo sem ouro")
                .isFalse();
    }

    @Provide
    Arbitrary<SimuladorService> cenarioComAlvoSemOuro() {
        // Gera um cenário específico para testar a regra de não roubar de quem não tem ouro
        return Arbitraries.randoms().map(random -> {
            SimuladorService simulador = new SimuladorService();
            simulador.inicializar(2);
            List<Criaturas> criaturas = simulador.getCriaturasParaTeste();

            // Força o estado para o teste
            // Criatura 1: tem ouro e é o potencial ladrão
            criaturas.get(0).setOuro(1_000_000);
            criaturas.get(0).setPosicaox(100);

            // Criatura 2: não tem ouro e é o alvo em potencial
            criaturas.get(1).setOuro(0);
            criaturas.get(1).setPosicaox(101); // Posição bem próxima para garantir que seria o alvo

            return simulador;
        });
    }

}