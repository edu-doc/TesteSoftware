package com.example.saltitantes.model.service;

import com.example.saltitantes.model.entity.Criaturas;
import com.example.saltitantes.model.dto.CriaturasDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Simulador {

    private final List<Criaturas> criaturas = new ArrayList<>();

    public void inicializar(int n) {
        criaturas.clear();
        for (int i = 0; i < n; i++) {
            criaturas.add(new Criaturas());
        }
    }

    public void simular(int iteracoes) {
        for (int i = 0; i < iteracoes; i++) {
            for (Criaturas c : criaturas) {
                c.moverX();
                c.moverY();
                Criaturas vizinha = encontrarMaisProxima(c);
                if (vizinha != null && vizinha.getOuro() > 0) {
                    int ouroRoubado = vizinha.getOuro() / 2;
                    vizinha.perderOuro(ouroRoubado);
                    c.adicionarOuro(ouroRoubado);
                }
            }
        }
    }

    private Criaturas encontrarMaisProxima(Criaturas atual) {
        return criaturas.stream()
                .filter(c -> c != atual)
                .min(Comparator.comparingDouble((Criaturas c) -> distancia(atual, c))
                        .thenComparingInt(Criaturas::getIdentificador))
                .orElse(null);
    }

    private double distancia(Criaturas a, Criaturas b) {
        double dx = a.getPosicaox() - b.getPosicaox();
        double dy = a.getPosicaoy() - b.getPosicaoy();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public List<CriaturasDTO> getEstadoAtual() {
        return criaturas.stream()
                .map(c -> new CriaturasDTO(
                        c.getIdentificador(),
                        c.getOuro(),
                        c.getPosicaox(),
                        c.getPosicaoy()
                )).collect(Collectors.toList());
    }
}

