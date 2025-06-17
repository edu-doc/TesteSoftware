package com.example.saltitantes.model.service;

import com.example.saltitantes.model.dto.CriarUsuarioDTO;
import com.example.saltitantes.model.dto.EstatisticasDTO;
import com.example.saltitantes.model.dto.LoginDTO;
import com.example.saltitantes.model.dto.UsuarioDTO;
import com.example.saltitantes.model.entity.Usuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Serviço para gerenciar usuários e estatísticas do sistema.
 */
@Service
public class UsuarioService {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    /**
     * Cria um novo usuário no sistema.
     * 
     * @param criarUsuarioDTO dados do usuário a ser criado
     * @return DTO do usuário criado
     * @throws IllegalArgumentException se login já existe ou dados inválidos
     */
    public UsuarioDTO criarUsuario(CriarUsuarioDTO criarUsuarioDTO) {
        if (criarUsuarioDTO.getLogin() == null || criarUsuarioDTO.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("Login não pode ser vazio.");
        }

        if (criarUsuarioDTO.getSenha() == null || criarUsuarioDTO.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser vazia.");
        }

        if (usuarios.containsKey(criarUsuarioDTO.getLogin())) {
            throw new IllegalArgumentException("Login já existe.");
        }

        Usuario usuario = new Usuario(
                criarUsuarioDTO.getLogin(),
                criarUsuarioDTO.getSenha(),
                criarUsuarioDTO.getAvatar());

        usuarios.put(usuario.getLogin(), usuario);

        return converterParaDTO(usuario);
    }

    /**
     * Realiza login do usuário.
     * 
     * @param loginDTO dados de login
     * @return DTO do usuário logado
     * @throws IllegalArgumentException se credenciais inválidas
     */
    public UsuarioDTO login(LoginDTO loginDTO) {
        Usuario usuario = usuarios.get(loginDTO.getLogin());

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        if (!usuario.verificarSenha(loginDTO.getSenha())) {
            throw new IllegalArgumentException("Senha incorreta.");
        }

        return converterParaDTO(usuario);
    }

    /**
     * Remove um usuário do sistema.
     * 
     * @param login login do usuário a ser removido
     * @throws IllegalArgumentException se usuário não existe
     */
    public void excluirUsuario(String login) {
        if (!usuarios.containsKey(login)) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        usuarios.remove(login);
    }

    /**
     * Lista todos os usuários cadastrados.
     * 
     * @return lista de DTOs dos usuários
     */
    public List<UsuarioDTO> listarUsuarios() {
        return usuarios.values().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Registra uma simulação executada por um usuário.
     * 
     * @param login       login do usuário
     * @param bemSucedida se a simulação foi bem-sucedida
     * @throws IllegalArgumentException se usuário não existe
     */
    public void registrarSimulacao(String login, boolean bemSucedida) {
        Usuario usuario = usuarios.get(login);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        usuario.incrementarTotalSimulacoes();
        if (bemSucedida) {
            usuario.incrementarPontuacao();
        }
    }

    /**
     * Obtém as estatísticas do sistema.
     * 
     * @return DTO com as estatísticas
     */
    public EstatisticasDTO obterEstatisticas() {
        List<UsuarioDTO> usuariosDTO = listarUsuarios();

        int totalSimulacoes = usuarios.values().stream()
                .mapToInt(Usuario::getTotalSimulacoes)
                .sum();

        int totalSimulacoesSucesso = usuarios.values().stream()
                .mapToInt(Usuario::getPontuacao)
                .sum();

        double mediaSimulacoesSucessoUsuario = usuarios.isEmpty() ? 0.0
                : usuarios.values().stream()
                        .mapToDouble(Usuario::getTaxaSucesso)
                        .average()
                        .orElse(0.0);

        double mediaTotalSimulacoesSucesso = totalSimulacoes == 0 ? 0.0
                : (double) totalSimulacoesSucesso / totalSimulacoes * 100.0;

        return new EstatisticasDTO(
                usuariosDTO,
                totalSimulacoes,
                mediaSimulacoesSucessoUsuario,
                mediaTotalSimulacoesSucesso,
                usuarios.size());
    }

    /**
     * Converte uma entidade Usuario para DTO.
     * 
     * @param usuario entidade a ser convertida
     * @return DTO correspondente
     */
    private UsuarioDTO converterParaDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getLogin(),
                null, // Não retornar senha por segurança
                usuario.getAvatar(),
                usuario.getPontuacao(),
                usuario.getTotalSimulacoes(),
                usuario.getTaxaSucesso());
    }

    /**
     * Verifica se um usuário existe.
     * 
     * @param login login do usuário
     * @return true se o usuário existe
     */
    public boolean usuarioExiste(String login) {
        return usuarios.containsKey(login);
    }

    /**
     * Método para testes - limpa todos os usuários.
     */
    public void limparUsuarios() {
        usuarios.clear();
    }
}
