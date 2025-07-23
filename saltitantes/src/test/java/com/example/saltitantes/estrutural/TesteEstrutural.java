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
 * Testes Estruturais - Verificam as interações entre entidades e regras de
 * negócio específicas.
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
         * @param nCriaturas Número de criaturas
         * @pre Sistema inicializado
         * @post Pelo menos uma criatura deve ter roubado (idCriaturaRoubada != -1)
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
                                Arguments.of(2), // Caso mínimo
                                Arguments.of(5), // Caso intermediário
                                Arguments.of(10) // Caso com mais interações
                );
        }

        /**
         * Testa a regra de negócio: criatura sem ouro não pode ser roubada.
         * 
         * @param iteracoes Número de iterações
         * @pre Uma criatura com ouro, outra sem ouro
         * @post Criatura sem ouro não é roubada (idCriaturaRoubada = -1)
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
                lista.get(0).setOuro(10); // Criatura com ouro
                lista.get(1).setOuro(0); // Criatura sem ouro

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
                                Arguments.of(5));
        }

        /**
         * Testa a regra de negócio: criatura com ouro pode ser roubada.
         * 
         * @param iteracoes Número de iterações
         * @pre Ambas as criaturas com ouro
         * @post Pelo menos uma criatura rouba outra
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
                lista.get(0).setOuro(1000000);
                lista.get(1).setOuro(1000000);

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
                                Arguments.of(5));
        }

        /**
         * Testa validação de parâmetros no método distancia().
         * 
         * @pre Sistema inicializado
         * @post IllegalArgumentException para parâmetros nulos
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
         * @pre Sistema inicializado
         * @post IllegalArgumentException para parâmetro nulo
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
         * @pre Sistema inicializado, posições configuradas
         * @post Distância calculada corretamente, simétrica
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
        }

        /**
         * Testa o comportamento do método encontrarMaisProxima().
         * 
         * @pre Sistema com 3 criaturas, posições específicas configuradas
         * @post Criatura mais próxima identificada corretamente
         */
        @Test
        void testEncontrarMaisProximaComportamento() {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(3);

                List<Criaturas> lista = simulador.getCriaturasParaTeste();

                // Configurar posições específicas
                lista.get(0).setPosicaox(0.0); // Criatura de referência
                lista.get(1).setPosicaox(2.0); // Mais próxima
                lista.get(2).setPosicaox(10.0); // Mais distante

                Criaturas maisProxima = simulador.encontrarMaisProxima(lista.get(0));

                assertThat(maisProxima)
                                .as("Deve encontrar a criatura mais próxima")
                                .isNotNull()
                                .extracting(Criaturas::getId)
                                .isEqualTo(lista.get(1).getId());
        }

        /**
         * Testa remoção de criaturas com pouco ouro.
         * 
         * @pre Sistema inicializado, ouro configurado
         * @post Criaturas com pouco ouro removidas
         */
        @Test
        void testRemoverCriaturaPoucoOuro() {
                SimuladorService simulador = new SimuladorService();
                simulador.inicializar(3);

                List<Criaturas> lista = simulador.getCriaturasParaTeste();

                // Configurar ouro insuficiente
                lista.get(0).setOuro(100000); // Criatura com pouco ouro
                lista.get(1).setOuro(500000); // Criatura com mais ouro
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
