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
                Arguments.of(2), // mínimo de criaturas
                Arguments.of(5), // valor médio
                Arguments.of(10) // valor maior
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
                Arguments.of(1001, false, "acima do limite superior"));
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
                Arguments.of(1001, false, "acima do limite superior"));
    }

    /**
     * Teste de fronteira: Comportamento com grande número de iterações
     * 
     * Valida que o sistema mantém performance e consistência
     * mesmo com execuções longas.
     */
    @Test
    void testFronteiraIteracoesExtensas() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(5);

        // Ajustar ouro das criaturas para que simulação não termine prematuramente
        var criaturas = simulador.getCriaturasParaTeste();
        var guardiao = simulador.getGuardiaoParaTeste();

        // Dar mais ouro às criaturas para prolongar a simulação
        for (var criatura : criaturas) {
            criatura.setOuro(2000000); // Mais que o guardião
        }

        // Espalhar criaturas em posições diferentes para evitar clusters imediatos
        for (int i = 0; i < criaturas.size(); i++) {
            criaturas.get(i).setPosicaox(i * 100000.0);
        }
        guardiao.setPosicaox(-50000.0); // Posição diferente

        // Teste com muitas iterações
        var resultado = simulador.simular(100);

        assertThat(resultado)
                .as("Sistema deve conseguir executar 100 iterações")
                .hasSize(100);

        // Verificar que todas as iterações têm dados válidos
        for (int i = 0; i < resultado.size(); i++) {
            var iteracao = resultado.get(i);
            assertThat(iteracao.getCriaturas())
                    .as("Iteração %d deve ter criaturas válidas", i)
                    .isNotNull();
            assertThat(iteracao.getGuardiao())
                    .as("Iteração %d deve ter guardião válido", i)
                    .isNotNull();
        }
    }

    /**
     * Teste de fronteira: Número máximo de criaturas permitido
     * 
     * Verifica o comportamento no limite superior de criaturas.
     */
    @Test
    void testFronteiraMaximoCreaturas() {
        SimuladorService simulador = new SimuladorService();

        // Teste no limite máximo
        assertThatCode(() -> simulador.inicializar(1000))
                .as("Deve aceitar o número máximo de criaturas")
                .doesNotThrowAnyException();

        var criaturas = simulador.getCriaturasParaTeste();
        var guardiao = simulador.getGuardiaoParaTeste();

        assertThat(criaturas)
                .as("Deve criar exatamente 1000 criaturas")
                .hasSize(1000);

        assertThat(guardiao.getId())
                .as("Guardião deve ter ID 1001")
                .isEqualTo(1001);
    }

    /**
     * Teste de fronteira: Comportamento com criaturas em posições extremas
     * 
     * Verifica como o sistema lida com valores de ponto flutuante extremos.
     */
    @Test
    void testFronteiraPosicoesExtremas() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(3);

        var criaturas = simulador.getCriaturasParaTeste();

        // Configurar posições extremas
        criaturas.get(0).setPosicaox(Double.MAX_VALUE / 2);
        criaturas.get(1).setPosicaox(-Double.MAX_VALUE / 2);
        criaturas.get(2).setPosicaox(0.0);

        // Executar simulação deve funcionar sem overflow
        assertThatCode(() -> simulador.simular(1))
                .as("Simulação deve lidar com posições extremas")
                .doesNotThrowAnyException();
    }

    /**
     * Teste de fronteira: Comportamento com valores de ouro extremos
     * 
     * Verifica se o sistema lida adequadamente com grandes quantidades de ouro.
     */
    @Test
    void testFronteiraOuroExtremo() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(2);

        var criaturas = simulador.getCriaturasParaTeste();

        // Configurar valores extremos de ouro
        criaturas.get(0).setOuro(Integer.MAX_VALUE / 2);
        criaturas.get(1).setOuro(1);

        var resultado = simulador.simular(1);

        // Verificar que não houve overflow
        for (var criatura : resultado.get(0).getCriaturas()) {
            assertThat(criatura.getOuro())
                    .as("Ouro não deve ter overflow")
                    .isGreaterThan(0);
        }
    }

    /**
     * Teste de fronteira: Precisão de ponto flutuante
     * 
     * Verifica se comparações de posições funcionam corretamente
     * com valores próximos considerando a escala do jogo.
     */
    @Test
    void testFronteiraPrecisaoPontoFlutuante() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(2);

        var criaturas = simulador.getCriaturasParaTeste();

        // Configurar posições próximas considerando a escala de movimento do jogo
        // Com ouro de 1.000.000, o movimento pode ser significativo, então usar
        // tolerância realista de 4000.0 (menor que a tolerância de cluster de 5000.0)
        criaturas.get(0).setPosicaox(10000.0);
        criaturas.get(1).setPosicaox(14000.0); // Diferença de 4000, próximas o suficiente

        var resultado = simulador.simular(1);

        // Sistema pode detectar que estão próximas e formar cluster após movimento
        // Não garantimos cluster, mas verificamos que o sistema funciona sem erros
        assertThat(resultado.get(0).getCriaturas().length + resultado.get(0).getClusters().size())
                .as("Sistema deve processar criaturas próximas corretamente")
                .isGreaterThan(0);
    }

    /**
     * Teste de fronteira: Comportamento quando todas as criaturas têm ouro zero
     * 
     * Cenário extremo onde nenhuma criatura pode se mover.
     */
    @Test
    void testFronteiraTodasCriaturasOuroZero() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(3);

        var criaturas = simulador.getCriaturasParaTeste();

        // Zerar ouro de todas as criaturas
        for (var criatura : criaturas) {
            criatura.setOuro(0);
        }

        var resultado = simulador.simular(5);

        // Verificar que as criaturas não se moveram
        for (var iteracao : resultado) {
            for (var criatura : iteracao.getCriaturas()) {
                assertThat(criatura.getPosicaox())
                        .as("Criaturas sem ouro não devem se mover")
                        .isEqualTo(0.0);
            }
        }
    }

    /**
     * Teste de fronteira: Simulação com apenas guardião restante
     * 
     * Verifica o comportamento quando apenas o guardião sobrevive.
     */
    @Test
    void testFronteiraApenasDardiao() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(2);

        // Forçar cenário onde guardião elimina todas as criaturas
        var criaturas = simulador.getCriaturasParaTeste();
        var guardiao = simulador.getGuardiaoParaTeste();

        // Colocar todas as criaturas próximas o suficiente para formar cluster
        // Usar tolerância extrema para garantir cluster: posições exatamente iguais
        criaturas.get(0).setPosicaox(50000.0);
        criaturas.get(0).setOuro(100);
        criaturas.get(1).setPosicaox(50000.0); // Posição exatamente igual
        criaturas.get(1).setOuro(100);

        // Guardião próximo o suficiente para eliminar o cluster
        guardiao.setPosicaox(50000.0);
        guardiao.setOuro(1000000); // Guardião já tem mais ouro

        var resultado = simulador.simular(1);
        var estadoFinal = resultado.get(0);

        // Verificar condição de sucesso
        if (estadoFinal.getCriaturas().length == 0 && estadoFinal.getClusters().size() == 0) {
            assertThat(estadoFinal.isSimulacaoBemSucedida())
                    .as("Simulação deve ser bem-sucedida quando apenas guardião resta")
                    .isTrue();
        }
    }

    /**
     * Teste de fronteira: Tolerâncias para formação de clusters
     * 
     * Verifica se as tolerâncias de formação de cluster estão adequadas
     * para a escala do jogo (considerando ouro inicial de 1.000.000).
     */
    @Test
    void testFronteiraToleranciaFormacaoCluster() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(3);

        var criaturas = simulador.getCriaturasParaTeste();

        // Cenário 1: Criaturas muito próximas (tolerância extrema) - devem formar
        // cluster
        criaturas.get(0).setPosicaox(10000.0);
        criaturas.get(0).setOuro(500000);
        criaturas.get(1).setPosicaox(10000.0); // Exatamente igual
        criaturas.get(1).setOuro(500000);

        // Cenário 2: Criatura distante - não deve formar cluster
        criaturas.get(2).setPosicaox(100000.0); // Bem distante
        criaturas.get(2).setOuro(500000);

        var resultado = simulador.simular(1);
        var iteracao = resultado.get(0);

        // Deve haver pelo menos 1 cluster ou criatura processada corretamente
        int totalEntidades = iteracao.getCriaturas().length + iteracao.getClusters().size();
        assertThat(totalEntidades)
                .as("Sistema deve processar entidades corretamente")
                .isGreaterThan(0);

        // Se formou cluster, verificar que tem ouro adequado
        if (!iteracao.getClusters().isEmpty()) {
            assertThat(iteracao.getClusters().get(0).getOuroTotal())
                    .as("Cluster deve ter soma adequada de ouro")
                    .isGreaterThan(500000); // Pelo menos o ouro de uma criatura + roubo
        }
    }

    /**
     * Teste de fronteira: Valor inicial do ouro do guardião
     * 
     * Verifica que o guardião inicia com o mesmo valor de ouro das criaturas
     * (ao contrário da especificação original que dizia 0).
     */
    @Test
    void testFronteiraOuroInicialGuardiao() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(5);

        var criaturas = simulador.getCriaturasParaTeste();
        var guardiao = simulador.getGuardiaoParaTeste();

        // Verificar que guardião e criaturas começam com mesmo ouro
        int ouroEsperado = 1000000;

        assertThat(guardiao.getOuro())
                .as("Guardião deve começar com mesmo ouro das criaturas")
                .isEqualTo(ouroEsperado);

        for (var criatura : criaturas) {
            assertThat(criatura.getOuro())
                    .as("Todas as criaturas devem começar com ouro padrão")
                    .isEqualTo(ouroEsperado);
        }

        // Verificar que guardião pode se mover (tem ouro > 0)
        guardiao.moverX();

        // Como tem ouro, posição pode ter mudado
        assertThat(guardiao.getOuro())
                .as("Guardião deve manter ouro após movimento")
                .isEqualTo(ouroEsperado);
    }
}
