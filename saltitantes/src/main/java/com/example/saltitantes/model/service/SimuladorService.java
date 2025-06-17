package com.example.saltitantes.model.service;

import com.example.saltitantes.model.dto.ClusterDTO;
import com.example.saltitantes.model.dto.CriaturasDTO;
import com.example.saltitantes.model.dto.GuardiaoDTO;
import com.example.saltitantes.model.dto.SimularResponseDTO;
import com.example.saltitantes.model.entity.Cluster;
import com.example.saltitantes.model.entity.Criaturas;
import com.example.saltitantes.model.entity.Guardiao;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SimuladorService {

    private final List<Criaturas> criaturas = new ArrayList<>();
    private final List<Cluster> clusters = new ArrayList<>();
    private Guardiao guardiao;
    private final List<SimularResponseDTO> historicoSimulacoes = new ArrayList<>();

    // apenas para teste
    public List<Criaturas> getCriaturasParaTeste() {
        return criaturas;
    }

    public List<Cluster> getClustersParaTeste() {
        return clusters;
    }

    public Guardiao getGuardiaoParaTeste() {
        return guardiao;
    }

    /**
     * Inicia a simulação com a quantidade especificada de criaturas.
     *
     * @param n a quantidade de criaturas a serem simulada
     * @pre n > 1
     * @pre n <= 1000
     * @post cria uma lista de criaturas contendo a quantidade n
     * @throws IllegalArgumentException se a quantidade de criaturas for menor ou
     *                                  igual a 1 ou maior que 1000
     */
    public void inicializar(int n) {
        if (n <= 1) {
            throw new IllegalArgumentException("A quantidade de criaturas deve ser maior que zero.");
        }
        if (n > 1000) {
            throw new IllegalArgumentException("A quantidade de criaturas deve ser menor ou igual a 1000.");
        }

        /*
         * Teste n Deve lançar exceção? Motivo
         * __________________________________________________
         * T1 -1 Sim n <= 1
         * T2 0 Sim n <= 1
         * T3 1 sim n <= 1
         * T4 2 Não Valor válido
         * T5 1000 Não Valor válido
         * T6 1001 Sim n > 1000
         */

        criaturas.clear();
        clusters.clear();
        Criaturas.resetarContador();
        historicoSimulacoes.clear();

        for (int i = 0; i < n; i++) {
            criaturas.add(new Criaturas());
        }

        // Criar o guardião com ID n+1
        guardiao = new Guardiao(n + 1);
    }

    /**
     * simula todas as iterações do simulador
     *
     * @return retorna uma lista do tipo SimularResponseDTO
     * @param iteracoes a quantidade de iterações a serem simuladas
     * @pre iteracoes > 0
     * @pre iteracoes <= 1000
     * @post retorna um histórico em formato de lista com n iterações de simulações
     *       ocorridas
     * @throws IllegalStateException    se a simulação não foi iniciada corretamente
     * @throws IllegalArgumentException se a quantidade de iterações for menor ou
     *                                  igual a 1 ou maior que 1000
     */
    public List<SimularResponseDTO> simular(int iteracoes) {
        if (iteracoes <= 0) {
            throw new IllegalArgumentException("A quantidade de iterações deve ser maior que zero.");
        }
        if (iteracoes > 1000) {
            throw new IllegalArgumentException("A quantidade de iterações deve ser menor ou igual a 1000.");
        }

        if (guardiao == null) {
            throw new IllegalStateException("Simulação não foi inicializada corretamente.");
        }

        historicoSimulacoes.clear();

        for (int i = 0; i < iteracoes; i++) {
            // Processar criaturas individuais e formar clusters
            Map<Integer, Integer> roubosCriaturas = processarCriaturas();

            // Processar clusters existentes
            Map<Integer, Integer> roubosClusters = processarClusters();

            // Combinar todos os roubos
            Map<Integer, Integer> todosRoubos = new HashMap<>(roubosCriaturas);
            todosRoubos.putAll(roubosClusters);

            // Processar o guardião (após as criaturas/clusters)
            int clusterEliminado = processarGuardiao();

            // Criar snapshot da iteração atual
            SimularResponseDTO iteracaoAtual = criarSnapshotIteracao(i + 1, clusterEliminado, todosRoubos);
            historicoSimulacoes.add(iteracaoAtual);

            // Verificar condições de sucesso
            if (verificarCondicoesSucesso()) {
                iteracaoAtual.setSimulacaoBemSucedida(true);
                break;
            }
        }

        return historicoSimulacoes;
    }

    /**
     * Processa as criaturas individuais, movendo-as e formando clusters quando
     * necessário.
     * 
     * @return mapa com criaturas/clusters e IDs das criaturas roubadas
     */
    private Map<Integer, Integer> processarCriaturas() {
        Map<Integer, Integer> roubos = new HashMap<>();
        Map<Double, List<Criaturas>> posicoesOcupadas = new HashMap<>();

        // Primeiro, mover todas as criaturas
        for (Criaturas criatura : criaturas) {
            criatura.moverX();
            double posicao = criatura.getPosicaox();

            posicoesOcupadas.computeIfAbsent(posicao, k -> new ArrayList<>()).add(criatura);
        }

        // Formar clusters onde há mais de uma criatura na mesma posição
        for (Map.Entry<Double, List<Criaturas>> entry : posicoesOcupadas.entrySet()) {
            List<Criaturas> criaturasNaPosicao = entry.getValue();

            if (criaturasNaPosicao.size() > 1) {
                // Formar cluster
                Cluster novoCluster = new Cluster(criaturasNaPosicao.get(0), criaturasNaPosicao.get(1));

                // Adicionar criaturas restantes ao cluster
                for (int i = 2; i < criaturasNaPosicao.size(); i++) {
                    novoCluster.adicionarCriatura(criaturasNaPosicao.get(i));
                }

                // Remover criaturas individuais e adicionar cluster
                criaturas.removeAll(criaturasNaPosicao);
                clusters.add(novoCluster);

                // IMPORTANTE: Cluster rouba METADE das moedas da criatura mais próxima
                int criaturaSendoRoubada = roubarDaCriaturaMaisProxima(novoCluster);
                roubos.put(novoCluster.getIdCluster(), criaturaSendoRoubada);
            }
        }

        // Processar roubo das criaturas individuais restantes
        for (Criaturas criatura : criaturas) {
            Criaturas vizinha = encontrarMaisProxima(criatura);
            if (vizinha != null && vizinha.getOuro() > 0) {
                // Criatura individual também rouba METADE do ouro da mais próxima
                int ouroRoubado = vizinha.getOuro() / 2;
                vizinha.perderOuro(ouroRoubado);
                criatura.adicionarOuro(ouroRoubado);
                roubos.put(criatura.getId(), vizinha.getId());
            } else {
                roubos.put(criatura.getId(), -1);
            }
        }

        return roubos;
    }

    /**
     * Processa os clusters existentes, movendo-os e fazendo-os roubar.
     * 
     * @return mapa com clusters e IDs das criaturas roubadas
     */
    private Map<Integer, Integer> processarClusters() {
        Map<Integer, Integer> roubosDosClusters = new HashMap<>();

        for (Cluster cluster : clusters) {
            cluster.moverX();
            int criaturaSendoRoubada = roubarDaCriaturaMaisProxima(cluster);
            roubosDosClusters.put(cluster.getIdCluster(), criaturaSendoRoubada);
        }

        return roubosDosClusters;
    }

    /**
     * Processa o guardião, movendo-o e eliminando clusters se necessário.
     * 
     * @return ID do cluster eliminado ou -1 se nenhum foi eliminado
     */
    private int processarGuardiao() {
        guardiao.moverX();

        // Verificar se o guardião ocupa a mesma posição de algum cluster
        Iterator<Cluster> iteratorClusters = clusters.iterator();
        while (iteratorClusters.hasNext()) {
            Cluster cluster = iteratorClusters.next();

            if (guardiao.estaNaPosicao(cluster.getPosicaox())) {
                // Guardião elimina o cluster e absorve seu ouro
                guardiao.adicionarOuro(cluster.getOuroTotal());
                int clusterEliminado = cluster.getIdCluster();
                iteratorClusters.remove();
                return clusterEliminado;
            }
        }

        return -1; // Nenhum cluster eliminado
    }

    /**
     * Faz um cluster roubar da criatura mais próxima.
     * 
     * @param cluster cluster que vai roubar
     * @return ID da criatura roubada ou -1 se nenhuma foi roubada
     */
    private int roubarDaCriaturaMaisProxima(Cluster cluster) {
        Criaturas criaturaMaisProxima = encontrarCriaturaMaisProximaDoCluster(cluster);

        if (criaturaMaisProxima != null && criaturaMaisProxima.getOuro() > 0) {
            int ouroRoubado = criaturaMaisProxima.getOuro() / 2;
            criaturaMaisProxima.perderOuro(ouroRoubado);
            cluster.roubarOuro(ouroRoubado);
            return criaturaMaisProxima.getId();
        }
        return -1;
    }

    /**
     * Encontra a criatura mais próxima de um cluster.
     * 
     * @param cluster cluster de referência
     * @return criatura mais próxima ou null se não houver
     */
    private Criaturas encontrarCriaturaMaisProximaDoCluster(Cluster cluster) {
        return criaturas.stream()
                .min(Comparator.comparingDouble((Criaturas c) -> Math.abs(c.getPosicaox() - cluster.getPosicaox()))
                        .thenComparingInt(Criaturas::getId))
                .orElse(null);
    }

    /**
     * Cria um snapshot da iteração atual.
     * 
     * @param numeroIteracao   número da iteração
     * @param clusterEliminado ID do cluster eliminado pelo guardião
     * @param roubos           mapa com IDs de entidades e quem elas roubaram
     * @return DTO da resposta da simulação
     */
    private SimularResponseDTO criarSnapshotIteracao(int numeroIteracao, int clusterEliminado,
            Map<Integer, Integer> roubos) {
        // Criar DTOs das criaturas
        CriaturasDTO[] criaturasDTO = criaturas.stream()
                .map(c -> new CriaturasDTO(c.getId(), c.getOuro(), c.getPosicaox(),
                        roubos.getOrDefault(c.getId(), -1)))
                .toArray(CriaturasDTO[]::new);

        // Criar DTOs dos clusters
        List<ClusterDTO> clustersDTO = clusters.stream()
                .map(cluster -> new ClusterDTO(
                        cluster.getIdCluster(),
                        cluster.getIdscriaturas(),
                        cluster.getOuroTotal(),
                        cluster.getPosicaox(),
                        roubos.getOrDefault(cluster.getIdCluster(), -1)))
                .collect(Collectors.toList());

        // Criar DTO do guardião
        GuardiaoDTO guardiaoDTO = new GuardiaoDTO(
                guardiao.getId(),
                guardiao.getOuro(),
                guardiao.getPosicaox(),
                clusterEliminado);

        return new SimularResponseDTO(numeroIteracao, criaturasDTO, clustersDTO, guardiaoDTO, false);
    }

    /**
     * Verifica as condições de sucesso da simulação.
     * 
     * @return true se a simulação foi bem-sucedida
     */
    private boolean verificarCondicoesSucesso() {
        int totalEntidades = criaturas.size() + clusters.size();

        // Sucesso: resta apenas o guardião
        if (totalEntidades == 0) {
            return true;
        }

        // Sucesso: resta apenas o guardião e uma criatura, com guardião tendo mais ouro
        if (totalEntidades == 1 && criaturas.size() == 1) {
            return guardiao.getOuro() > criaturas.get(0).getOuro();
        }

        return false;
    }

    /**
     * Encontra a criatura mais próxima da criatura atual.
     * 
     * @param atual criatura de referência (não pode ser null)
     * @return objeto do tipo criatura
     * @pre nenhuma pré condição
     * @post retorna a criatura mais próxima da criatura atual
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
     * 
     * @param a criatura a (não pode ser null)
     * @param b criatura b (não pode ser null)
     * @return distância do tipo double
     * @pre nenhuma pré condição
     * @post compara e retorna a distância absoluta entre as criaturas
     * @throws IllegalArgumentException se qualquer criatura for null
     */
    public double distancia(Criaturas a, Criaturas b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Nenhuma criatura pode ser null para o cálculo da distância.");
        }
        double dx = a.getPosicaox() - b.getPosicaox();
        return Math.abs(dx);
    }

    public List<SimularResponseDTO> getHistoricoSimulacoes() {
        return historicoSimulacoes;
    }
}
