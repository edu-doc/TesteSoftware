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

    public void inicializar(int n) {

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
            c.moverY();

            Criaturas vizinha = encontrarMaisProxima(c);
            int idRoubada = -1;
            if (vizinha != null && vizinha.getOuro() > 0) {
                int ouroRoubado = vizinha.getOuro() / 2;
                vizinha.perderOuro(ouroRoubado);
                c.adicionarOuro(ouroRoubado);
                idRoubada = vizinha.getId();
            }

            CriaturasDTO dto = new CriaturasDTO(
                c.getId(),
                c.getOuro(),
                c.getPosicaox(),
                c.getPosicaoy(),
                idRoubada
            );

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
        double dy = a.getPosicaoy() - b.getPosicaoy();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public List<List<CriaturasDTO>> getHistoricoSimulacoes() {
        return historicoSimulacoes;
    }
}
