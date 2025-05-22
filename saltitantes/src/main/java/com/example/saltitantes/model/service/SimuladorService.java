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
            T1	        -1	        Sim	                n <= 0
            T2	         0	        Sim	                n <= 0
            T3	         1	        Não 	            Valor válido
            T4	         1000	    Não	                Valor válido
            T5	         1001	    Sim	                n > 1000

         */


        criaturas.clear();
        Criaturas.resetarContador();
        historicoSimulacoes.clear();
        for (int i = 0; i < n; i++) {
            criaturas.add(new Criaturas());
        }
    }

    public List<SimularResponseDTO> simular(int iteracoes) {
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

    private Criaturas encontrarMaisProxima(Criaturas atual) {
        return criaturas.stream()
                .filter(c -> c != atual)
                .min(Comparator.comparingDouble((Criaturas c) -> distancia(atual, c))
                        .thenComparingInt(Criaturas::getId))
                .orElse(null);
    }

    private double distancia(Criaturas a, Criaturas b) {
        double dx = a.getPosicaox() - b.getPosicaox();
        return Math.abs(dx);
    }

    public List<List<CriaturasDTO>> getHistoricoSimulacoes() {
        return historicoSimulacoes;
    }
}