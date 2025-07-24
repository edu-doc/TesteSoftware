package com.example.saltitantes.service;

import com.example.saltitantes.model.dto.CriarUsuarioDTO;
import com.example.saltitantes.model.dto.EstatisticasDTO;
import com.example.saltitantes.model.dto.LoginDTO;
import com.example.saltitantes.model.dto.UsuarioDTO;
import com.example.saltitantes.model.entity.Usuario;
import com.example.saltitantes.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serviço para gerenciar usuários e estatísticas do sistema.
 */
@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

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

        // Verifica se o login já existe
        Optional<Usuario> usuarioExistente = userRepository.findByLogin(criarUsuarioDTO.getLogin());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("Login já existe.");
        }

        // Cria novo usuário com senha simples
        Usuario usuario = new Usuario(
                criarUsuarioDTO.getLogin(),
                criarUsuarioDTO.getSenha(), // Senha simples, sem criptografia
                criarUsuarioDTO.getAvatar());

        // Salva no banco de dados
        usuario = userRepository.save(usuario);

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
        Optional<Usuario> usuarioOpt = userRepository.findByLogin(loginDTO.getLogin());

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
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
        Optional<Usuario> usuarioOpt = userRepository.findByLogin(login);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        userRepository.delete(usuarioOpt.get());
    }

    /**
     * Lista todos os usuários cadastrados.
     *
     * @return lista de DTOs dos usuários
     */
    public List<UsuarioDTO> listarUsuarios() {
        return userRepository.findAll().stream()
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
        Optional<Usuario> usuarioOpt = userRepository.findByLogin(login);

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.incrementarTotalSimulacoes();
        if (bemSucedida) {
            usuario.incrementarPontuacao();
        }

        // Salva as alterações no banco
        userRepository.save(usuario);
    }

    /**
     * Obtém as estatísticas do sistema.
     *
     * @return DTO com as estatísticas
     */
    public EstatisticasDTO obterEstatisticas() {
        List<Usuario> usuarios = userRepository.findAll();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());

        int totalSimulacoes = usuarios.stream()
                .mapToInt(Usuario::getTotalSimulacoes)
                .sum();

        int totalSimulacoesSucesso = usuarios.stream()
                .mapToInt(Usuario::getPontuacao)
                .sum();

        double mediaSimulacoesSucessoUsuario = usuarios.isEmpty() ? 0.0
                : usuarios.stream()
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
     * Obtém as estatísticas de um usuário específico.
     *
     * @param login login do usuário
     * @return DTO do usuário com suas estatísticas
     * @throws IllegalArgumentException se usuário não existe
     */
    public UsuarioDTO obterEstatisticasUsuario(String login) {
        Optional<Usuario> usuarioOpt = userRepository.findByLogin(login);

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        return converterParaDTO(usuarioOpt.get());
    }

    /**
     * Converte uma entidade Usuario para DTO.
     *
     * @param usuario entidade a ser convertida
     * @return DTO correspondente (sem senha por segurança)
     */
    private UsuarioDTO converterParaDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getLogin(),
                usuario.getAvatar(),
                usuario.getPontuacao(),
                usuario.getTotalSimulacoes(),
                usuario.getTaxaSucesso());
    }

}
