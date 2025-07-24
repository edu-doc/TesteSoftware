package com.example.saltitantes.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimuladorService {

    @Autowired
    private UsuarioService usuarioService;

    private final List<Criaturas> criaturas = new ArrayList<>();
    private final List<Cluster> clusters = new ArrayList<>();
    private Guardiao guardiao;
    private final List<SimularResponseDTO> historicoSimulacoes = new ArrayList<>();

    // apenas para teste
    public List<Criaturas> getCriaturasParaTeste() {
        return criaturas;
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

        historicoSimulacoes.clear();
        boolean simulacaoFinalizada = false;

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

            // Criar snapshot da iteração atual usando estado ATUAL (após processamento)
            SimularResponseDTO iteracaoAtual = criarSnapshotIteracao(i + 1, clusterEliminado, todosRoubos);
            historicoSimulacoes.add(iteracaoAtual);

            // Verificar condições de vitória
            if (verificarGanhador()) {
                simulacaoFinalizada = true;
                break; // Encerra o loop pois a simulação terminou
            }
        }

<<<<<<< Updated upstream:saltitantes/src/main/java/com/example/saltitantes/service/SimuladorService.java
        // Definir o flag de sucesso apenas na ÚLTIMA iteração se a simulação foi
        // finalizada
        if (!historicoSimulacoes.isEmpty()) {
            SimularResponseDTO ultimaIteracao = historicoSimulacoes.get(historicoSimulacoes.size() - 1);
            ultimaIteracao.setSimulacaoBemSucedida(simulacaoFinalizada);
        }
=======
        // Definir o flag de sucesso apenas na ÚLTIMA iteração se a simulação foi finalizada
        SimularResponseDTO ultimaIteracao = historicoSimulacoes.get(historicoSimulacoes.size() - 1);
        ultimaIteracao.setSimulacaoBemSucedida(simulacaoFinalizada);

>>>>>>> Stashed changes:saltitantes/src/main/java/com/example/saltitantes/model/service/SimuladorService.java

        return historicoSimulacoes;
    }

    /**
     * Executa a simulação e registra o resultado para o usuário.
     *
     * @param iteracoes    número de iterações a serem executadas
     * @param loginUsuario login do usuário que está executando a simulação
     * @return lista contendo o snapshot de cada iteração ocorrida
     * @throws IllegalStateException    se a simulação não foi iniciada corretamente
     * @throws IllegalArgumentException se a quantidade de iterações for menor ou
     *                                  igual a 1 ou maior que 1000
     */
    public List<SimularResponseDTO> simular(int iteracoes, String loginUsuario) {
        List<SimularResponseDTO> resultado = simular(iteracoes);

        // Verifica se a simulação foi bem-sucedida
        boolean bemSucedida = resultado.stream()
                .anyMatch(SimularResponseDTO::isSimulacaoBemSucedida);

        // Registra a simulação para o usuário (se o login foi fornecido)
        if (loginUsuario != null && !loginUsuario.trim().isEmpty()) {
            try {
                usuarioService.registrarSimulacao(loginUsuario, bemSucedida);
            } catch (IllegalArgumentException e) {
                // Log de erro, mas não interrompe a simulação
                System.err.println("Erro ao registrar simulação para usuário " + loginUsuario + ": " + e.getMessage());
            }
        }

        return resultado;
    }

    /**
     * Processa as criaturas individuais, movendo-as e formando clusters quando
     * necessário.
     * * @return mapa com criaturas/clusters e IDs das criaturas roubadas
     **/
    private Map<Integer, Integer> processarCriaturas() {
        Map<Integer, Integer> roubos = new HashMap<>();

        // PRIMEIRO: Mover todas as criaturas
        for (Criaturas criatura : criaturas) {
            criatura.moverX();
        }

        // SEGUNDO: Processar roubo das criaturas individuais
        List<Criaturas> criaturasParaProcessamento = new ArrayList<>(criaturas);
        for (Criaturas criatura : criaturasParaProcessamento) {
            // Apenas processa criaturas que ainda existem na lista principal
<<<<<<< Updated upstream:saltitantes/src/main/java/com/example/saltitantes/service/SimuladorService.java
            if (!criaturas.contains(criatura))
                continue;
=======
>>>>>>> Stashed changes:saltitantes/src/main/java/com/example/saltitantes/model/service/SimuladorService.java

            Criaturas vizinha = encontrarMaisProxima(criatura);
            if (vizinha != null && vizinha.getOuro() > 0) {
                // Criatura individual rouba METADE do ouro da mais próxima
                int ouroRoubado = vizinha.getOuro() / 2;
                vizinha.perderOuro(ouroRoubado);
                criatura.adicionarOuro(ouroRoubado);
                roubos.put(criatura.getId(), vizinha.getId());
            } else {
                roubos.put(criatura.getId(), -1);
            }
        }

        // Bloco para eliminar criaturas com menos de 300 mil de ouro
        eliminarCriaturasPoucoOuro(criaturas);

        // TERCEIRO: Formar clusters baseado na proximidade após movimento
        List<Criaturas> criaturasRestantes = new ArrayList<>(criaturas);
        List<List<Criaturas>> gruposProximos = new ArrayList<>();
        boolean[] jaProcessada = new boolean[criaturasRestantes.size()];

        for (int i = 0; i < criaturasRestantes.size(); i++) {
            if (jaProcessada[i])
                continue;

            Criaturas c1 = criaturasRestantes.get(i);
            List<Criaturas> proximas = new ArrayList<>();
            proximas.add(c1);
            jaProcessada[i] = true;

            for (int j = i + 1; j < criaturasRestantes.size(); j++) {
                if (jaProcessada[j])
                    continue;

                Criaturas c2 = criaturasRestantes.get(j);
                double distancia = Math.abs(c1.getPosicaox() - c2.getPosicaox());

                // Usar tolerância realista considerando a escala do jogo
                if (distancia <= 5000.0) {
                    proximas.add(c2);
                    jaProcessada[j] = true;
                }
            }

            if (proximas.size() > 1) {
                gruposProximos.add(proximas);
            }
        }

        // Criar clusters a partir dos grupos identificados
        for (List<Criaturas> grupo : gruposProximos) {
                Cluster novoCluster = new Cluster(grupo.get(0), grupo.get(1));

                for (int k = 2; k < grupo.size(); k++) {
                    novoCluster.adicionarCriatura(grupo.get(k));
                }

                criaturas.removeAll(grupo);
                clusters.add(novoCluster);

                int criaturaSendoRoubada = roubarDaCriaturaMaisProxima(novoCluster);
                roubos.put(novoCluster.getIdCluster(), criaturaSendoRoubada);

        }

        return roubos;
    }

    /**
     * Processa os clusters existentes, movendo-os e fazendo-os roubar.
     * * @return mapa com clusters e IDs das criaturas roubadas
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
     * * @return ID do cluster eliminado ou -1 se nenhum foi eliminado
     */
    private int processarGuardiao() {
        guardiao.moverX();

        // Verificar se o guardião está próximo o suficiente de algum cluster para
        // eliminá-lo
        Iterator<Cluster> iteratorClusters = clusters.iterator();
        while (iteratorClusters.hasNext()) {
            Cluster cluster = iteratorClusters.next();

            // Usar tolerância alta para permitir movimento com alto ouro
            // Com 1.000.000 de ouro, movimento pode ser de até 2.000.000 total
            double distancia = Math.abs(guardiao.getPosicaox() - cluster.getPosicaox());

            if (distancia <= 500000.0) { // Tolerância balanceada para permitir alguns clusters sobreviverem
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
     * * @param cluster cluster que vai roubar
     * 
     * @return ID da criatura roubada ou -1 se nenhuma foi roubada
     */
    public int roubarDaCriaturaMaisProxima(Cluster cluster) {
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
     * Remove criaturas da lista que possuem menos de 300.000 de ouro.
     * Este método modifica a lista original.
     *
     * @param criaturas A lista de criaturas a ser modificada.
     */
    public void eliminarCriaturasPoucoOuro(List<Criaturas> criaturas) {
        criaturas.removeIf(criatura -> criatura.getOuro() < 300000);
    }

    /**
     * Encontra a criatura mais próxima de um cluster.
     * * @param cluster cluster de referência
     * 
     * @return criatura mais próxima ou null se não houver
     */
    public Criaturas encontrarCriaturaMaisProximaDoCluster(Cluster cluster) {
        return criaturas.stream()
                .min(Comparator.comparingDouble((Criaturas c) -> Math.abs(c.getPosicaox() - cluster.getPosicaox()))
                        .thenComparingInt(Criaturas::getId))
                .orElse(null);
    }

    /**
     * Cria um snapshot da iteração atual.
     * * @param numeroIteracao número da iteração
     * 
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
     * Verifica as condições de vitória da simulação.
     * A simulação é considerada vencida (bem-sucedida) se:
     * 1. Apenas o guardião resta (listas de criaturas e clusters estão vazias).
     * 2. Restam apenas o guardião e uma única criatura (lista de clusters vazia e
     * lista de criaturas com 1 elemento).
     *
     * @return true se uma das condições de vitória for atendida, false caso
     *         contrário.
     */
    private boolean verificarGanhador() {
        // Conta o número de criaturas e clusters restantes.
        int numeroDeCriaturas = criaturas.size();
        int numeroDeClusters = clusters.size();

        // Condição 1: Apenas o guardião está vivo.
        // Isso acontece quando não há mais criaturas individuais nem clusters.
        boolean apenasGuardiaoVivo = (numeroDeCriaturas == 0 && numeroDeClusters == 0);

        // Condição 2: Guardião e uma única criatura estão vivos.
        // Isso acontece quando a lista de criaturas tem exatamente um elemento e não há
        // clusters.
        boolean guardiaoMaisUmaCriatura = (numeroDeCriaturas == 1 && numeroDeClusters == 0);

        // Se qualquer uma das condições for verdadeira, a simulação tem um ganhador.

        return apenasGuardiaoVivo || guardiaoMaisUmaCriatura;
    }

    /**
     * Encontra a criatura mais próxima da criatura atual.
     * * @param atual criatura de referência (não pode ser null)
     * 
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
     * * @param a criatura a (não pode ser null)
     * 
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