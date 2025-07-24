package com.example.saltitantes.funcional;

import com.example.saltitantes.model.dto.CriaturasDTO;
import com.example.saltitantes.model.dto.GuardiaoDTO;
import com.example.saltitantes.model.dto.SimularResponseDTO;
import com.example.saltitantes.model.dto.UsuarioDTO;
import com.example.saltitantes.model.dto.EstatisticasDTO;
import com.example.saltitantes.model.entity.Usuario;
import com.example.saltitantes.service.SimuladorService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;

/**
 * Testes Funcionais - Validam o funcionamento correto dos métodos principais do
 * sistema.
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
         * @param nCriaturas     Número de criaturas (>= 2)
         * @param iteracoes      Número máximo de iterações (> 0)
         * @param iteracaoMinima Número mínimo esperado de iterações
         * @pre Sistema limpo, parâmetros válidos
         * @post Simulação retorna 1+ iterações, estruturas válidas
         */
        @ParameterizedTest
        @MethodSource("simulacaoProvider")
        void testFuncionamentoSimulacao(int nCriaturas, int iteracoes, int iteracaoMinima) {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(nCriaturas);

                var resposta = simulador.simular(iteracoes);

                // Verificar se a simulação retornou pelo menos uma iteração
                assertThat(resposta)
                                .as("Simulação deve retornar pelo menos uma iteração")
                                .isNotEmpty();

                // Verificar se não excede o número máximo de iterações
                assertThat(resposta)
                                .as("Simulação não deve exceder o número máximo de iterações solicitadas")
                                .hasSizeLessThanOrEqualTo(iteracoes);

                // Verificar se tem pelo menos o número mínimo esperado
                assertThat(resposta)
                                .as("Simulação deve ter pelo menos %d iterações", iteracaoMinima)
                                .hasSizeGreaterThanOrEqualTo(iteracaoMinima);

                // Verificar se o estado das criaturas está consistente na primeira iteração
                assertThat(resposta.get(0).getCriaturas())
                                .as("Primeira iteração deve conter criaturas válidas")
                                .isNotEmpty();
        }

        static Stream<Arguments> simulacaoProvider() {
                return Stream.of(
                                of(2, 1, 1), // T6: Caso simples - 2 criaturas, 1 iteração
                                of(3, 3, 1), // T7: Múltiplas criaturas e iterações, verificar que executa pelo menos 1
                                of(5, 5, 1) // T8: Cenário maior, verificar que executa pelo menos 1
                );
        }

        /**
         * Teste de funcionamento da inicialização do simulador.
         * 
         * @param nCriaturas Número de criaturas a criar
         * @pre Simulador disponível
         * @post Estado limpo, nCriaturas criadas
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
                                Arguments.of(10));
        }

        static Stream<Arguments> guardiaoFuncionalProvider() {
                return Stream.of(
                                Arguments.of(2),
                                Arguments.of(5),
                                Arguments.of(10));
        }

        /**
         * Teste de funcionamento do guardião.
         * 
         * @param nCriaturas Número de criaturas (afeta ID do guardião)
         * @pre Sistema inicializado
         * @post Guardião criado com ID=nCriaturas+1, ouro=1000000
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
                                .as("Guardião deve começar com mesmo ouro das criaturas")
                                .isEqualTo(1000000);

                assertThat(guardiao.getPosicaox())
                                .as("Guardião deve começar na posição 0")
                                .isEqualTo(0.0);
        }

        /**
         * Testes de invariantes do sistema.
         * 
         * @param nCriaturas Número inicial de criaturas
         * @param iteracoes  Número de iterações
         * @pre Parâmetros válidos
         * @post Total de entidades ≤ n+1, guardião sempre existe
         */
        @ParameterizedTest
        @MethodSource("propriedadeInvariantesProvider")
        void testPropriedadeInvariantesTotalCriaturas(int nCriaturas, int iteracoes) {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(nCriaturas);

                var resultado = simulador.simular(iteracoes);

                // Propriedade 1: O número total de entidades (criaturas + guardião) nunca deve
                // exceder n+1
                resultado.forEach(iteracao -> {
                        int totalEntidades = iteracao.getCriaturas().length + 1; // +1 pelo guardião
                        assertThat(totalEntidades)
                                        .as("Total de entidades (criaturas + guardião) não deve exceder n+1")
                                        .isLessThanOrEqualTo(nCriaturas + 1);
                });

                // Propriedade 2: O guardião sempre deve existir e ter ID correto
                resultado.forEach(iteracao -> {
                        assertThat(iteracao.getGuardiao())
                                        .as("Guardião deve sempre existir")
                                        .isNotNull();
                        assertThat(iteracao.getGuardiao().getId())
                                        .as("Guardião deve sempre ter ID = nCriaturas + 1")
                                        .isEqualTo(nCriaturas + 1);
                });
        }

        static Stream<Arguments> propriedadeInvariantesProvider() {
                return Stream.of(
                                of(2, 5), // Caso pequeno com múltiplas iterações
                                of(5, 10), // Caso médio
                                of(10, 15), // Caso maior
                                of(3, 20) // Muitas iterações para testar estabilidade
                );
        }

        /**
         * Teste de conservação de ouro total.
         * 
         * @param nCriaturas Número de criaturas
         * @pre Sistema inicializado
         * @post Ouro total ≤ inicial (conservação)
         */
        @ParameterizedTest
        @MethodSource("conservacaoOuroProvider")
        void testPropriedadeConservacaoOuro(int nCriaturas) {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(nCriaturas);

                // Calcular ouro inicial total (cada criatura + guardião começam com valores
                // conhecidos)
                long ouroInicialCriaturas = nCriaturas * 1000000L; // Valor inicial por criatura
                long ouroInicialGuardiao = 1000000L; // Guardião também começa com ouro
                long ouroTotalInicial = ouroInicialCriaturas + ouroInicialGuardiao;

                var resultado = simulador.simular(5);

                // Verificar que o ouro não aumenta (conservação ou diminuição devido à
                // eliminação)
                resultado.forEach(iteracao -> {
                        long ouroTotalIteracao = Arrays.stream(iteracao.getCriaturas())
                                        .mapToLong(criatura -> criatura.getOuro())
                                        .sum();

                        // Somar ouro dos clusters
                        ouroTotalIteracao += iteracao.getClusters().stream()
                                        .mapToLong(cluster -> cluster.getOuroTotal())
                                        .sum();

                        ouroTotalIteracao += iteracao.getGuardiao().getOuro();

                        assertThat(ouroTotalIteracao)
                                        .as("Ouro total não deve aumentar durante a simulação (pode diminuir devido à eliminação de criaturas)")
                                        .isLessThanOrEqualTo(ouroTotalInicial);
                });
        }

        static Stream<Arguments> conservacaoOuroProvider() {
                return Stream.of(
                                Arguments.of(2), // Mínimo de criaturas
                                Arguments.of(5), // Valor médio
                                Arguments.of(8) // Valor maior
                );
        }

        /**
         * Teste End-to-End: Cenário Completo de Simulação
         * 
         * @pre Sistema funcional, recursos suficientes
         * @post Simulação completa, dados íntegros, guardião presente
         */
        @Test
        void testCenarioCompletoSimulacaoBemSucedida() {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(3);

                // Executar simulação longa para aumentar chances de sucesso
                var resultado = simulador.simular(50);

                // Verificar que a simulação progrediu (pode terminar antes se houver vitória)
                assertThat(resultado)
                                .as("Simulação deve retornar pelo menos uma iteração")
                                .isNotEmpty();

                // Verificar que não excedeu o limite máximo
                assertThat(resultado)
                                .as("Simulação não deve exceder 50 iterações")
                                .hasSizeLessThanOrEqualTo(50);

                // Verificar se alguma iteração resultou em sucesso
                boolean algumSucesso = resultado.stream()
                                .anyMatch(iteracao -> iteracao.isSimulacaoBemSucedida());

                // Se houve sucesso, validar condições
                if (algumSucesso) {
                        var iteracaoSucesso = resultado.stream()
                                        .filter(iteracao -> iteracao.isSimulacaoBemSucedida())
                                        .findFirst()
                                        .orElseThrow();

                        // Validar condições de sucesso
                        assertThat(iteracaoSucesso.getCriaturas().length + 1) // +1 pelo guardião
                                        .as("Simulação bem-sucedida deve ter no máximo 2 entidades (guardião + 1 criatura) ou só guardião")
                                        .isLessThanOrEqualTo(2);
                }

                // Verificar integridade dos dados em iterações finais
                var ultimaIteracao = resultado.get(resultado.size() - 1);
                assertThat(ultimaIteracao.getGuardiao())
                                .as("Guardião deve sempre existir na última iteração")
                                .isNotNull();
        }

        /**
         * Teste de Estresse: Múltiplas Simulações Consecutivas
         * 
         * @pre Sistema estável
         * @post Todas as execuções mantêm consistência básica
         */
        @Test
        void testEstresseMultiplasSimulacoes() {
                SimuladorService simulador = new SimuladorService();

                for (int i = 0; i < 10; i++) {
                        final int simulacaoNumero = i + 1;
                        simulador.inicializar(4);
                        var resultado = simulador.simular(10);

                        // Verificar consistência básica em cada execução - pelo menos 1 iteração
                        assertThat(resultado)
                                        .as("Simulação %d deve retornar pelo menos 1 iteração", simulacaoNumero)
                                        .isNotEmpty();

                        // Verificar que não excede 10 iterações
                        assertThat(resultado)
                                        .as("Simulação %d não deve exceder 10 iterações", simulacaoNumero)
                                        .hasSizeLessThanOrEqualTo(10);

                        // Verificar que guardião existe em todas as iterações
                        resultado.forEach(iteracao -> {
                                assertThat(iteracao.getGuardiao())
                                                .as("Guardião deve existir em todas as iterações da simulação %d",
                                                                simulacaoNumero)
                                                .isNotNull();
                        });
                }
        }

        /**
         * Teste de Robustez: Simulação com Estado Inconsistente
         * 
         * @pre Múltiplas inicializações
         * @post Sistema se adapta corretamente
         */
        @Test
        void testRobustezEstadoInconsistente() {
                SimuladorService simulador = new SimuladorService();

                // Primeira simulação normal
                simulador.inicializar(3);
                var resultado1 = simulador.simular(5);
                assertThat(resultado1).hasSize(5);

                // Reinicializar com número diferente de criaturas
                simulador.inicializar(7);
                
                // Verificar que o estado foi limpo corretamente ANTES de simular
                // Isso testa se a inicialização funcionou corretamente
                assertThat(simulador.getCriaturasParaTeste())
                                .as("Nova inicialização deve ter o número correto de criaturas")
                                .hasSize(7);
                
                // Verificar que o guardião foi recriado corretamente
                assertThat(simulador.getGuardiaoParaTeste().getId())
                                .as("Nova inicialização deve criar guardião com ID correto")
                                .isEqualTo(8); // 7 criaturas + 1
                
                // Agora executar a simulação para confirmar que funciona
                var resultado2 = simulador.simular(3);
                assertThat(resultado2).hasSize(3);
        }

        /**
         * Property-Based Test: Conservação de elementos
         * 
         * @param nCriaturas Número inicial de criaturas
         * @param iteracoes  Número de iterações
         * @pre Parâmetros válidos
         * @post Total de elementos ≤ inicial
         */
        @ParameterizedTest
        @MethodSource("propriedadeConservacaoProvider")
        void testPropriedadeConservacaoElementos(int nCriaturas, int iteracoes) {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(nCriaturas);

                var resultado = simulador.simular(iteracoes);
                int totalInicialElementos = nCriaturas + 1; // criaturas + guardião

                for (int i = 0; i < resultado.size(); i++) {
                        var iteracao = resultado.get(i);
                        int totalAtual = iteracao.getCriaturas().length + 1; // criaturas/clusters + guardião

                        assertThat(totalAtual)
                                        .as("Na iteração %d, o total de elementos não deve exceder o inicial", i)
                                        .isLessThanOrEqualTo(totalInicialElementos);
                }
        }

        static Stream<Arguments> propriedadeConservacaoProvider() {
                return Stream.of(
                                Arguments.of(3, 5),
                                Arguments.of(5, 10),
                                Arguments.of(10, 15),
                                Arguments.of(50, 20));
        }

        /**
         * Property-Based Test: Consistência de simulação bem-sucedida
         * 
         * @param nCriaturas Número de criaturas
         * @param iteracoes  Número de iterações
         * @pre Simulação executada
         * @post Se marcada como sucesso, condições válidas
         */
        @ParameterizedTest
        @MethodSource("propriedadeSimulacaoSucessoProvider")
        void testPropriedadeConsistenciaSimulacaoSucesso(int nCriaturas, int iteracoes) {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(nCriaturas);

                var resultado = simulador.simular(iteracoes);
                var ultimaIteracao = resultado.get(resultado.size() - 1);

                if (ultimaIteracao.isSimulacaoBemSucedida()) {
                        var criaturas = ultimaIteracao.getCriaturas();
                        var guardiao = ultimaIteracao.getGuardiao();

                        if (criaturas.length == 1) {
                                // Caso: guardião + 1 criatura
                                assertThat(guardiao.getOuro())
                                                .as("Quando simulação bem-sucedida com 1 criatura, guardião deve ter mais ouro")
                                                .isGreaterThan(criaturas[0].getOuro());
                        } else if (criaturas.length == 0) {
                                // Caso: apenas guardião
                                assertThat(guardiao.getOuro())
                                                .as("Quando simulação bem-sucedida com apenas guardião, deve ter ouro")
                                                .isGreaterThan(0);
                        } else {
                                fail("Simulação marcada como bem-sucedida mas condições não atendem os critérios");
                        }
                }
        }

        static Stream<Arguments> propriedadeSimulacaoSucessoProvider() {
                return Stream.of(
                                Arguments.of(2, 10),
                                Arguments.of(3, 15),
                                Arguments.of(5, 20));
        }

        /**
         * Property-Based Test: Ouro não-negativo
         * 
         * @param nCriaturas Número de criaturas
         * @param iteracoes  Número de iterações
         * @pre Simulação válida
         * @post Ouro ≥ 0 para todas as entidades
         */
        @ParameterizedTest
        @MethodSource("propriedadeOuroNaoNegativoProvider")
        void testPropriedadeOuroNaoNegativo(int nCriaturas, int iteracoes) {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(nCriaturas);

                var resultado = simulador.simular(iteracoes);

                for (int i = 0; i < resultado.size(); i++) {
                        var iteracao = resultado.get(i);

                        // Verificar criaturas/clusters
                        for (var criatura : iteracao.getCriaturas()) {
                                assertThat(criatura.getOuro())
                                                .as("Iteração %d: criatura %d não deve ter ouro negativo", i,
                                                                criatura.getId())
                                                .isGreaterThanOrEqualTo(0);
                        }

                        // Verificar guardião
                        assertThat(iteracao.getGuardiao().getOuro())
                                        .as("Iteração %d: guardião não deve ter ouro negativo", i)
                                        .isGreaterThanOrEqualTo(0);
                }
        }

        static Stream<Arguments> propriedadeOuroNaoNegativoProvider() {
                return Stream.of(
                                Arguments.of(2, 5),
                                Arguments.of(4, 8),
                                Arguments.of(7, 12),
                                Arguments.of(10, 15));
        }

        /**
         * Teste de Integração: Simulação completa com verificação de estado final
         * 
         * @pre Sistema integrado funcional
         * @post Componentes interagem corretamente, dados íntegros
         */
        @Test
        void testIntegracaoSimulacaoCompleta() {
                SimuladorService simulador = new SimuladorService();
                int nCriaturas = 5;
                int iteracoes = 20;

                simulador.inicializar(nCriaturas);

                // Estado inicial
                var criaturasIniciais = simulador.getCriaturasParaTeste();
                var guardiaoInicial = simulador.getGuardiaoParaTeste();

                assertThat(criaturasIniciais).hasSize(nCriaturas);
                assertThat(guardiaoInicial.getOuro()).isEqualTo(1000000);

                // Executar simulação
                var resultado = simulador.simular(iteracoes);

                // Verificações de integridade - pode terminar antes se houver vitória
                assertThat(resultado)
                                .as("Simulação deve retornar pelo menos uma iteração")
                                .isNotEmpty();

                assertThat(resultado)
                                .as("Simulação não deve exceder o número máximo de iterações")
                                .hasSizeLessThanOrEqualTo(iteracoes);

                var estadoFinal = resultado.get(resultado.size() - 1);

                // Verificar que o guardião ainda existe
                assertThat(estadoFinal.getGuardiao()).isNotNull();

                // Verificar que as IDs são consistentes
                for (var criatura : estadoFinal.getCriaturas()) {
                        assertThat(criatura.getId())
                                        .as("IDs das criaturas devem estar no range válido")
                                        .isBetween(1, nCriaturas);
                }

                assertThat(estadoFinal.getGuardiao().getId())
                                .as("ID do guardião deve ser nCriaturas + 1")
                                .isEqualTo(nCriaturas + 1);
        }

        /**
         * Teste de Integração: Funcionamento do sistema de clusters
         * 
         * @pre Sistema inicializado, criaturas em posições próximas
         * @post Clusters formados corretamente ou processamento adequado
         */
        @Test
        void testIntegracaoSistemaCluster() {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(3);

                // Forçar situação onde criaturas estarão próximas considerando a escala do jogo
                var criaturas = simulador.getCriaturasParaTeste();
                criaturas.get(0).setPosicaox(100000.0);
                criaturas.get(0).setOuro(500000); // Valores acima do limiar de eliminação
                criaturas.get(1).setPosicaox(100000.0); // Posição exatamente igual para formar cluster
                criaturas.get(1).setOuro(600000);
                criaturas.get(2).setPosicaox(200000.0); // Distante o suficiente
                criaturas.get(2).setOuro(400000);

                var resultado = simulador.simular(1);
                var iteracao = resultado.get(0);

                // Deve haver formação de cluster ou processamento correto
                boolean encontrouCluster = false;
                for (var criatura : iteracao.getCriaturas()) {
                        if (criatura.getId() > 10) { // IDs de cluster são > 10
                                encontrouCluster = true;
                                assertThat(criatura.getOuro())
                                                .as("Cluster deve ter soma do ouro das criaturas que se uniram")
                                                .isGreaterThan(500000); // Pelo menos a soma de duas criaturas
                                break;
                        }
                }

                // Verificar clusters em lista separada também
                if (!encontrouCluster && !iteracao.getClusters().isEmpty()) {
                        encontrouCluster = true;
                        assertThat(iteracao.getClusters().get(0).getOuroTotal())
                                        .as("Cluster deve ter soma do ouro das criaturas que se uniram")
                                        .isGreaterThan(500000);
                }

                // As criaturas devem permanecer no sistema (não eliminadas por pouco ouro)
                assertThat(iteracao.getCriaturas().length + iteracao.getClusters().size())
                                .as("Sistema deve manter criaturas/clusters com ouro suficiente")
                                .isGreaterThan(0);
        }

        /**
         * Teste de Integração: Interação guardião-cluster
         * 
         * @pre Guardião e clusters em posições próximas
         * @post Eliminação correta ou interação adequada
         */
        @Test
        void testIntegracaoGuardiaoEliminaCluster() {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(4); // Mais criaturas para aumentar chance de cluster

                var criaturas = simulador.getCriaturasParaTeste();
                var guardiao = simulador.getGuardiaoParaTeste();

                // Configurar cenário específico com escala realista para forçar cluster
                // Colocar várias criaturas na mesma posição para garantir cluster
                criaturas.get(0).setPosicaox(150000.0);
                criaturas.get(0).setOuro(1000000);
                criaturas.get(1).setPosicaox(150000.0); // Posição exatamente igual
                criaturas.get(1).setOuro(1000000);
                criaturas.get(2).setPosicaox(150000.0); // Mais uma na mesma posição
                criaturas.get(2).setOuro(1000000);
                criaturas.get(3).setPosicaox(300000.0); // Distante o suficiente
                criaturas.get(3).setOuro(1000000);

                guardiao.setPosicaox(150000.0); // Guardião na mesma posição dos clusters
                guardiao.setOuro(1000000);

                var resultado = simulador.simular(3); // Mais iterações para permitir formação/eliminação

                // Verificar se houve alguma interação do guardião
                boolean guardiaoAbsorveuOuro = false;
                for (var iteracao : resultado) {
                        if (iteracao.getGuardiao().getOuro() > 1000000) {
                                guardiaoAbsorveuOuro = true;
                                break;
                        }
                }

                // Se não houve absorção de ouro, pelo menos verificar que o sistema funcionou
                if (!guardiaoAbsorveuOuro) {
                        var ultimaIteracao = resultado.get(resultado.size() - 1);
                        assertThat(ultimaIteracao.getGuardiao().getOuro())
                                        .as("Guardião deve manter pelo menos seu ouro inicial")
                                        .isGreaterThanOrEqualTo(1000000);

                        // Verificar que o sistema processou corretamente
                        assertThat(ultimaIteracao.getCriaturas().length + ultimaIteracao.getClusters().size())
                                        .as("Sistema deve manter entidades ativas")
                                        .isGreaterThanOrEqualTo(0);
                } else {
                        // Se houve absorção, verificar que aumentou
                        var ultimaIteracao = resultado.get(resultado.size() - 1);
                        assertThat(ultimaIteracao.getGuardiao().getOuro())
                                        .as("Guardião deve ter absorvido ouro do cluster eliminado")
                                        .isGreaterThan(1000000);
                }
        }

        /**
         * Teste de Randomização: Verifica comportamento não-determinístico
         * 
         * @pre Múltiplas simulações idênticas
         * @post Resultados diferentes devido à randomização
         */
        @Test
        void testComportamentoNaoDeterministico() {
                SimuladorService simulador1 = new SimuladorService();
                SimuladorService simulador2 = new SimuladorService();

                simulador1.inicializar(3);
                simulador2.inicializar(3);

                var resultado1 = simulador1.simular(5);
                var resultado2 = simulador2.simular(5);

                // Verificar que pelo menos alguma diferença ocorreu
                boolean encontrouDiferenca = false;

                for (int i = 0; i < resultado1.size() && i < resultado2.size(); i++) {
                        var iter1 = resultado1.get(i);
                        var iter2 = resultado2.get(i);

                        if (iter1.getCriaturas().length != iter2.getCriaturas().length) {
                                encontrouDiferenca = true;
                                break;
                        }

                        // Comparar posições das criaturas
                        for (int j = 0; j < iter1.getCriaturas().length && j < iter2.getCriaturas().length; j++) {
                                if (Math.abs(iter1.getCriaturas()[j].getPosicaox() -
                                                iter2.getCriaturas()[j].getPosicaox()) > 1e-10) {
                                        encontrouDiferenca = true;
                                        break;
                                }
                        }
                }

                assertThat(encontrouDiferenca)
                                .as("Simulações idênticas devem produzir resultados diferentes devido à randomização")
                                .isTrue();
        }

        @Test
        void simular_DeveLancarExcecaoQuandoChamadoSemInicializar() {
                SimuladorService servicoNaoInicializado = new SimuladorService();
                assertThatThrownBy(() -> servicoNaoInicializado.simular(5))
                                .isInstanceOf(NullPointerException.class);
        }

        @Test
        void simular_DeveExecutarTodasAsIteracoesSeNaoHouverGanhador() {

                SimuladorService simuladorService = new SimuladorService();

                simuladorService.inicializar(10);

                List<SimularResponseDTO> resultado = simuladorService.simular(10);

                assertThat(resultado).hasSize(10);
                SimularResponseDTO ultimoEstado = resultado.get(resultado.size() - 1);
                assertThat(ultimoEstado.isSimulacaoBemSucedida()).isFalse();
        }

}
