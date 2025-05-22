package com.example.saltitantes;

import com.example.saltitantes.model.dto.CriaturasDTO;
import com.example.saltitantes.model.entity.Criaturas;
import com.example.saltitantes.model.service.SimuladorService;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;

public class SaltitantesApplicationTests {

	@ParameterizedTest // T1 a T5 — Testes de inicialização (domínio e fronteira)
	@MethodSource("inicializacaoProvider")
	void testInicializacao(int n, boolean expectException) {
		SimuladorService simulador = new SimuladorService();

		if (expectException) {
			assertThatThrownBy(() -> simulador.inicializar(n))
					.isInstanceOf(IllegalArgumentException.class);
		} else {
			simulador.inicializar(n);
			assertThat(simulador.getHistoricoSimulacoes()).isEmpty();
		}
	}

	static Stream<Arguments> inicializacaoProvider() {
		return Stream.of(
				of(1, true), // fronteria inferior
				of(-1, true), // negativo
				of(1000, false), // limite superior
				of(1001, true) // acima do limite
		);
	}

	@ParameterizedTest // T6 a T8 — Testes de simulação
	@MethodSource("simulacaoProvider")
	void testSimulacao(int nCriaturas, int iteracoes, int iteracaoEsperada, int qtdEsperada) {
		SimuladorService simulador = new SimuladorService();
		simulador.inicializar(nCriaturas);

		var resposta = simulador.simular(iteracoes);

		assertThat(resposta).hasSize(iteracoes);
		assertThat(resposta.get(iteracaoEsperada).getCriaturas()).hasSize(qtdEsperada);
	}

	static Stream<Arguments> simulacaoProvider() {
		return Stream.of(
				of(2, 1, 0, 2), // T6: 2 criaturas, 1 iteração
				of(3, 3, 2, 3), // T7: 3 criaturas, 3 iterações, pegar última
				of(5, 5, 1, 5) // T8: 5 criaturas, 5 iterações, pegar a segunda
		);
	}

	@ParameterizedTest // T9 — Verificar se alguma criatura roubou outra
	@MethodSource("rouboProvider")
	void testRoubos(int nCriaturas) {
		SimuladorService simulador = new SimuladorService();
		simulador.inicializar(nCriaturas);
		var resultado = simulador.simular(1);

		CriaturasDTO[] criaturas = resultado.get(0).getCriaturas();
		assertThat(criaturas).anyMatch(c -> c.getIdCriaturaRoubada() != -1);
	}

	static Stream<Arguments> rouboProvider() {
		return Stream.of(
				of(2),
				of(5),
				of(10));
	}

	@ParameterizedTest
	@MethodSource("iteracoesZeroProvider")
	void testSimulacaoComZeroIteracoes(int nCriaturas) {
		SimuladorService simulador = new SimuladorService();
		simulador.inicializar(nCriaturas);
		var resultado = simulador.simular(0);

		assertThat(resultado).isEmpty(); // nenhuma iteração = nenhuma resposta
	}

	static Stream<Arguments> iteracoesZeroProvider() {
		return Stream.of(
				of(2),
				of(5),
				of(10));
}
	static Stream<Arguments> criaturaIsoladaProvider() {
		return Stream.of(
				of(1),
				of(3));
	}

	@ParameterizedTest
	@MethodSource("vizinhaSemOuroProvider")
	void testVizinhaNaoPodeSerRoubadaSeNaoTemOuro(int iteracoes) {
		SimuladorService simulador = new SimuladorService();
		simulador.inicializar(2);

		List<Criaturas> lista = simulador.getCriaturasParaTeste();

		lista.get(0).setPosicaox(0);
		lista.get(1).setPosicaox(1);

		lista.get(0).setOuro(10);
		lista.get(1).setOuro(0);

		var resultado = simulador.simular(iteracoes);
		CriaturasDTO[] criaturas = resultado.get(0).getCriaturas();

		for (CriaturasDTO c : criaturas) {
			if (c.getId() == lista.get(0).getId()) {
				assertThat(c.getIdCriaturaRoubada())
						.as("Criatura 0 não deveria conseguir roubar a criatura 1")
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

	@ParameterizedTest
	@MethodSource("vizinhaComOuroProvider")
	void testVizinhaPodeSerRoubadaSeTemOuro(int iteracoes) {
		SimuladorService simulador = new SimuladorService();
		simulador.inicializar(2);

		List<Criaturas> lista = simulador.getCriaturasParaTeste();

		lista.get(0).setPosicaox(0);
		lista.get(1).setPosicaox(1);

		lista.get(0).setOuro(10);
		lista.get(1).setOuro(10);

		var resultado = simulador.simular(iteracoes);
		CriaturasDTO[] criaturas = resultado.get(0).getCriaturas();

		boolean houveRoubo = Arrays.stream(criaturas)
				.anyMatch(c -> c.getIdCriaturaRoubada() == lista.get(1).getId());

		assertThat(houveRoubo)
				.as("Era esperado que uma criatura conseguisse roubar")
				.isTrue();
	}

	static Stream<Arguments> vizinhaComOuroProvider() {
		return Stream.of(
				Arguments.of(1),
				Arguments.of(2),
				Arguments.of(5)
		);
	}
}
