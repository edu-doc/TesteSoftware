package com.example.saltitantes.model.service;

import com.example.saltitantes.model.dto.CriaturasDTO;
import com.example.saltitantes.model.dto.SimularResponseDTO;
import com.example.saltitantes.model.entity.Criaturas;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SimuladorService {

    private final List<Criaturas> criaturas = new ArrayList<>();
    private final List<List<CriaturasDTO>> historicoSimulacoes = new ArrayList<>();

    // apenas para teste
    public List<Criaturas> getCriaturasParaTeste() {
        return criaturas;
    }
    /**
     * Inicia a simulação com a quantidade especificada de criaturas.
     *
     * @param n a quantidade de criaturas a serem simulada
     * @pre n > 1
     * @pre n <= 1000
     * @post 
     * @throws IllegalArgumentException se a quantidade de criaturas for menor ou
     * igual a 1 ou maior que 1000
     */

    public void inicializar(int n) {

        
        if (n <= 1) {
            throw new IllegalArgumentException("A quantidade de criaturas deve ser maior que zero.");
        }
        if (n > 1000) {
            throw new IllegalArgumentException("A quantidade de criaturas deve ser menor ou igual a 1000.");
        }

        /*
            Teste	    n	  Deve lançar exceção?	    Motivo
            __________________________________________________
            T1	        -1	        Sim	                n <= 1
            T2	         0	        Sim	                n <= 1
            T3	         1	        sim 	            n <= 1
            T4	         2	        Não 	            Valor válido
            T5	         1000	    Não	                Valor válido
            T6	         1001	    Sim	                n > 1000

         */


        criaturas.clear();
        Criaturas.resetarContador();
        historicoSimulacoes.clear();
        for (int i = 0; i < n; i++) {
            criaturas.add(new Criaturas());
        }
    }
/**
     * simula todas as iterações do simulador
     *
     * @return retorna toda a lista de iterações
     * @param iteracoes a quantidade de iterações a serem simuladas
     * @pre iteracoes > 0
     * @pre  iteracoes <= 1000
     * @post 
     * @throws IllegalStateException se a simulação não foi iniciada
     * corretamente.
     * @throws IllegalArgumentException se a quantidade de iterações for menor ou
     * igual a 1 ou maior que 1000
     */
    public List<SimularResponseDTO> simular(int iteracoes) {
        if (iteracoes <= 0) {
            throw new IllegalArgumentException("A quantidade de iterações deve ser maior que zero.");
        }
        if (iteracoes > 1000) {
            throw new IllegalArgumentException("A quantidade de iterações deve ser menor ou igual a 1000.");
        }
        historicoSimulacoes.clear();

        for (int i = 0; i < iteracoes; i++) {
            List<CriaturasDTO> criaturasDaIteracao = new ArrayList<>();

            for (Criaturas c : criaturas) {
                
                c.moverX();

                Criaturas vizinha = encontrarMaisProxima(c);
                int idRoubada = -1;

                if (vizinha != null && vizinha.getOuro() > 0) {

                    /*
                    Resultado da Condição =	Deve roubar

                    C1 = (vizinha != null)	C2 = (vizinha.getOuro() > 0)

                    C1      C2	           Deve roubar
                    false	false	false	❌ Não
                    false	true	false	❌ Não
                    true	false	false	❌ Não
                    true	true	true	✅ Sim

                    Quando C2 é falsa, não importa o valor de C1, a condição é falsa.
                    Quando C1 é falsa, não importa o valor de C2, a condição é falsa.
                    Quando C1 é verdadeira, C2 deve ser verdadeira para a condição ser verdadeira.

                    */

                    int ouroRoubado = vizinha.getOuro() / 2;
                    vizinha.perderOuro(ouroRoubado);
                    c.adicionarOuro(ouroRoubado);
                    idRoubada = vizinha.getId();
                }

                CriaturasDTO dto = new CriaturasDTO(
                        c.getId(),
                        c.getOuro(),
                        c.getPosicaox(),
                        idRoubada);

                criaturasDaIteracao.add(dto);
            }

            historicoSimulacoes.add(criaturasDaIteracao);
        }
        List<SimularResponseDTO> resposta = new ArrayList<>();
        for (int i = 0; i < historicoSimulacoes.size(); i++) {
            SimularResponseDTO dto = new SimularResponseDTO(i + 1,
                    historicoSimulacoes.get(i).toArray(new CriaturasDTO[0]));
            resposta.add(dto);
        }

        return resposta;
    }

    /**
     * Encontra a criatura mais próxima da criatura atual.
     * @param atual criatura de referência (não pode ser null)
     * @return criatura mais próxima ou null se não houver
     * @throws IllegalArgumentException se a criatura atual for null
     */
    public Criaturas encontrarMaisProxima(Criaturas atual) {
        if (atual == null) {
            throw new IllegalArgumentException("A criatura de referência não pode ser null.");
        }
        return criaturas.stream()
                .filter(c -> c != atual)
                .min(Comparator.comparingDouble((Criaturas c) -> distancia(atual, c))
                        .thenComparingInt(Criaturas::getId))
                .orElse(null);
    }

    /**
     * Calcula a distância absoluta entre duas criaturas.
     * @param a criatura a (não pode ser null)
     * @param b criatura b (não pode ser null)
     * @return distância absoluta no eixo x
     * @throws IllegalArgumentException se qualquer criatura for null
     */
    public double distancia(Criaturas a, Criaturas b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Nenhuma criatura pode ser null para o cálculo da distância.");
        }
        double dx = a.getPosicaox() - b.getPosicaox();
        return Math.abs(dx);
    }

    public List<List<CriaturasDTO>> getHistoricoSimulacoes() {
        return historicoSimulacoes;
    }
}