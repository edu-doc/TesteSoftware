package com.example.saltitantes.estrutural;

import com.example.saltitantes.model.dto.CriaturasDTO;
import com.example.saltitantes.model.entity.Criaturas;
import com.example.saltitantes.model.service.SimuladorService;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes Estruturais - Verificam as interações entre entidades e regras de negócio específicas.
 * 
 * Focam em:
 * - Interações entre criaturas (roubo)
 * - Validação de parâmetros e estado interno
 * - Comportamento de métodos específicos
 * - Integridade das relações entre objetos
 */
public class TesteEstrutural {

    /**
     * T9 — Verificar se alguma criatura roubou outra
     * 
     * Testa a interação de roubo entre criaturas:
     * - Verifica se o mecanismo de roubo está ativo
     * - Valida que pelo menos uma interação ocorre
     * - Confirma que o sistema registra adequadamente os roubos
     */
    @ParameterizedTest
    @MethodSource("rouboProvider")
    void testInteracaoRoubo(int nCriaturas) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(nCriaturas);
        var resultado = simulador.simular(1);

        CriaturasDTO[] criaturas = resultado.get(0).getCriaturas();
        
        assertThat(criaturas)
            .as("Com %d criaturas, pelo menos uma deve roubar outra", nCriaturas)
            .anyMatch(c -> c.getIdCriaturaRoubada() != -1);
    }

    static Stream<Arguments> rouboProvider() {
        return Stream.of(
                Arguments.of(2),   // Caso mínimo
                Arguments.of(5),   // Caso intermediário
                Arguments.of(10)   // Caso com mais interações
        );
    }

    /**
     * Testa a regra de negócio: criatura sem ouro não pode ser roubada.
     * 
     * Valida a interação específica onde:
     * - Uma criatura tem ouro, outra não tem
     * - A criatura sem ouro não deve ser alvo de roubo
     * - O sistema deve respeitar esta regra estrutural
     */
    @ParameterizedTest
    @MethodSource("vizinhaSemOuroProvider")
    void testVizinhaSemOuroNaoPodeSerRoubada(int iteracoes) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(2);

        List<Criaturas> lista = simulador.getCriaturasParaTeste();

        // Configurar cenário específico de teste
        lista.get(0).setPosicaox(0);
        lista.get(1).setPosicaox(1);
        lista.get(0).setOuro(10);   // Criatura com ouro
        lista.get(1).setOuro(0);    // Criatura sem ouro

        var resultado = simulador.simular(iteracoes);
        CriaturasDTO[] criaturas = resultado.get(0).getCriaturas();

        // Verificar que a criatura com ouro não conseguiu roubar da sem ouro
        for (CriaturasDTO c : criaturas) {
            if (c.getId() == lista.get(0).getId()) {
                assertThat(c.getIdCriaturaRoubada())
                        .as("Criatura não deveria conseguir roubar de vizinha sem ouro")
                        .isEqualTo(-1);
            }
        }
    }

    static Stream<Arguments> vizinhaSemOuroProvider() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(5)
        );
    }

    /**
     * Testa a regra de negócio: criatura com ouro pode ser roubada.
     * 
     * Valida a interação onde:
     * - Ambas as criaturas têm ouro
     * - O roubo deve ocorrer conforme esperado
     * - O sistema deve registrar adequadamente a transação
     */
    @ParameterizedTest
    @MethodSource("vizinhaComOuroProvider")
    void testVizinhaComOuroPodeSerRoubada(int iteracoes) {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(2);

        List<Criaturas> lista = simulador.getCriaturasParaTeste();

        // Configurar cenário onde ambas têm ouro
        lista.get(0).setPosicaox(0);
        lista.get(1).setPosicaox(1);
        lista.get(0).setOuro(10);
        lista.get(1).setOuro(10);

        var resultado = simulador.simular(iteracoes);
        CriaturasDTO[] criaturas = resultado.get(0).getCriaturas();

        boolean houveRoubo = Arrays.stream(criaturas)
                .anyMatch(c -> c.getIdCriaturaRoubada() == lista.get(1).getId());

        assertThat(houveRoubo)
                .as("Era esperado que uma criatura conseguisse roubar da outra")
                .isTrue();
    }

    static Stream<Arguments> vizinhaComOuroProvider() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(5)
        );
    }

    /**
     * Testa validação de parâmetros no método distancia().
     * 
     * Verifica a robustez estrutural:
     * - Rejeição de parâmetros nulos
     * - Mensagens de erro apropriadas
     * - Comportamento defensivo do código
     */
    @Test
    void testValidacaoParametrosDistancia() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(2);
        
        List<Criaturas> lista = simulador.getCriaturasParaTeste();
        Criaturas criatura1 = lista.get(0);
        
        // Teste com primeiro parâmetro nulo
        assertThatThrownBy(() -> simulador.distancia(null, criatura1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Nenhuma criatura pode ser null");
            
        // Teste com segundo parâmetro nulo
        assertThatThrownBy(() -> simulador.distancia(criatura1, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Nenhuma criatura pode ser null");
            
        // Teste com ambos os parâmetros nulos
        assertThatThrownBy(() -> simulador.distancia(null, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Nenhuma criatura pode ser null");
    }

    /**
     * Testa validação de parâmetros no método encontrarMaisProxima().
     * 
     * Verifica a integridade estrutural:
     * - Tratamento adequado de referências nulas
     * - Mensagens de erro descritivas
     * - Prevenção de estados inconsistentes
     */
    @Test
    void testValidacaoParametrosEncontrarMaisProxima() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(2);
        
        // Teste com parâmetro nulo
        assertThatThrownBy(() -> simulador.encontrarMaisProxima(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("criatura de referência não pode ser null");
    }

    /**
     * Testa o funcionamento correto do cálculo de distância.
     * 
     * Valida a lógica estrutural:
     * - Cálculo matemático correto
     * - Consistência dos resultados
     * - Comportamento com diferentes posições
     */
    @Test
    void testCalculoDistancia() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(2);
        
        List<Criaturas> lista = simulador.getCriaturasParaTeste();
        Criaturas criatura1 = lista.get(0);
        Criaturas criatura2 = lista.get(1);
        
        // Configurar posições conhecidas
        criatura1.setPosicaox(0.0);
        criatura2.setPosicaox(5.0);
        
        double distancia = simulador.distancia(criatura1, criatura2);
        
        assertThat(distancia)
            .as("Distância entre posições 0.0 e 5.0 deve ser 5.0")
            .isEqualTo(5.0);
        
        // Testar simetria
        double distanciaInversa = simulador.distancia(criatura2, criatura1);
        assertThat(distanciaInversa)
            .as("Distância deve ser simétrica")
            .isEqualTo(distancia);
    }    /**
     * Testa o comportamento do método encontrarMaisProxima() com diferentes cenários.
     * 
     * Valida a lógica de busca:
     * - Identificação correta da criatura mais próxima
     * - Exclusão da própria criatura da busca
     */
    @Test
    void testEncontrarMaisProximaComportamento() {
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(3);
        
        List<Criaturas> lista = simulador.getCriaturasParaTeste();
        
        // Configurar posições específicas
        lista.get(0).setPosicaox(0.0);   // Criatura de referência
        lista.get(1).setPosicaox(2.0);   // Mais próxima
        lista.get(2).setPosicaox(10.0);  // Mais distante
        
        Criaturas maisProxima = simulador.encontrarMaisProxima(lista.get(0));
        
        assertThat(maisProxima)
            .as("Deve encontrar a criatura mais próxima")
            .isNotNull()
            .extracting(Criaturas::getId)
            .isEqualTo(lista.get(1).getId());
    }

    @Test
    void testRemoverCriaturaPoucoOuro(){
        SimuladorService simulador = new SimuladorService();
        simulador.inicializar(3);

        List<Criaturas> lista = simulador.getCriaturasParaTeste();

        // Configurar ouro insuficiente
        lista.get(0).setOuro(100000);  // Criatura com pouco ouro
        lista.get(1).setOuro(500000);  // Criatura com mais ouro
        lista.get(2).setOuro(1000000); // Criatura com muito ouro

        // Remover criatura com pouco ouro
        simulador.eliminarCriaturasPoucoOuro(lista);

        assertThat(lista)
            .as("Deve remover criaturas com pouco ouro")
            .hasSize(2) // Apenas as duas restantes devem ter mais de 200 de ouro
            .extracting(Criaturas::getOuro)
            .allMatch(ouro -> ouro > 300000);
    }
}
