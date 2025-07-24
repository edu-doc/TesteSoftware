package com.example.saltitantes.dominio;

import com.example.saltitantes.model.dto.CriarUsuarioDTO;
import com.example.saltitantes.model.dto.LoginDTO;
import com.example.saltitantes.model.dto.UsuarioDTO;
import com.example.saltitantes.model.dto.EstatisticasDTO;
import com.example.saltitantes.model.entity.Usuario;
import com.example.saltitantes.repository.UserRepository;
import com.example.saltitantes.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes de Domínio para Sistema de Usuários - Validam regras de negócio.
 * 
 * Cobertura:
 * - Criação e validação de usuários
 * - Autenticação
 * - Gestão de pontuação
 * - Estatísticas do sistema
 */
public class TesteUsuarioDominio {

        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private UsuarioService usuarioService;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                // Configuração padrão: nenhum usuário existente no repositório
                when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
                when(userRepository.findAll()).thenReturn(new ArrayList<>());
                when(userRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
                        Usuario usuario = invocation.getArgument(0);
                        // Simula que o usuário foi salvo com ID gerado
                        return usuario;
                });
        }

        /**
         * Testa criação válida de usuário.
         * 
         * @pre Dados válidos fornecidos, nenhum usuário com login existente
         * @post Usuário criado com pontuação e simulações zeradas
         */
        @Test
        void testCriacaoUsuarioValido() {
                // Given
                CriarUsuarioDTO dto = new CriarUsuarioDTO("usuario1", "senha123", "avatar.png");

                // When
                UsuarioDTO resultado = usuarioService.criarUsuario(dto);

                // Then
                assertThat(resultado)
                                .as("Usuário deve ser criado com sucesso")
                                .isNotNull();

                assertThat(resultado.getLogin())
                                .as("Login deve ser preservado")
                                .isEqualTo("usuario1");

                assertThat(resultado.getAvatar())
                                .as("Avatar deve ser preservado")
                                .isEqualTo("avatar.png");

                assertThat(resultado.getPontuacao())
                                .as("Pontuação inicial deve ser zero")
                                .isEqualTo(0);

                assertThat(resultado.getTotalSimulacoes())
                                .as("Total de simulações inicial deve ser zero")
                                .isEqualTo(0);

                assertThat(resultado.getTaxaSucesso())
                                .as("Taxa de sucesso inicial deve ser zero")
                                .isEqualTo(0.0);
        }

        /**
         * Testa validação de login obrigatório.
         * 
         * @param loginInvalido Login inválido (nulo, vazio ou espaços)
         * @pre DTO com login inválido
         * @post IllegalArgumentException lançada
         */
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { "   ", "\t", "\n" })
        void testCriacaoUsuarioLoginInvalido(String loginInvalido) {
                // Given
                CriarUsuarioDTO dto = new CriarUsuarioDTO(loginInvalido, "senha123", "avatar.png");

                // When/Then
                assertThatThrownBy(() -> usuarioService.criarUsuario(dto))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("Login não pode ser vazio");
        }

        /**
         * Testa validação de senha obrigatória.
         * 
         * @param senhaInvalida Senha inválida (nula, vazia ou espaços)
         * @pre DTO com senha inválida
         * @post IllegalArgumentException lançada
         */
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = { "   ", "\t", "\n" })
        void testCriacaoUsuarioSenhaInvalida(String senhaInvalida) {
                // Given
                CriarUsuarioDTO dto = new CriarUsuarioDTO("usuario1", senhaInvalida, "avatar.png");

                // When/Then
                assertThatThrownBy(() -> usuarioService.criarUsuario(dto))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("Senha não pode ser vazia");
        }

        /**
         * Testa regra de unicidade do login.
         * 
         * @pre Primeiro usuário criado, segundo com mesmo login
         * @post IllegalArgumentException para login duplicado
         */
        @Test
        void testLoginDuplicado() {
                // Given
                CriarUsuarioDTO dto1 = new CriarUsuarioDTO("usuario1", "senha123", "avatar1.png");
                CriarUsuarioDTO dto2 = new CriarUsuarioDTO("usuario1", "outraSenha", "avatar2.png");

                // Primeiro usuário criado com sucesso
                usuarioService.criarUsuario(dto1);

                // Configurar mock para simular que o login já existe na segunda tentativa
                Usuario usuarioExistente = new Usuario("usuario1", "senha123", "avatar1.png");
                when(userRepository.findByLogin("usuario1")).thenReturn(Optional.of(usuarioExistente));

                // When/Then
                assertThatThrownBy(() -> usuarioService.criarUsuario(dto2))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("Login já existe");
        }

        /**
         * Testa autenticação bem-sucedida.
         * 
         * @pre Usuário criado, credenciais corretas fornecidas
         * @post Login retorna dados do usuário autenticado
         */
        @Test
        void testLoginBemSucedido() {
                // Given
                CriarUsuarioDTO criarDto = new CriarUsuarioDTO("usuario1", "senha123", "avatar.png");
                usuarioService.criarUsuario(criarDto);

                // Configurar mock para encontrar o usuário para login
                Usuario usuarioSalvo = new Usuario("usuario1", "senha123", "avatar.png");
                when(userRepository.findByLogin("usuario1")).thenReturn(Optional.of(usuarioSalvo));

                LoginDTO loginDto = new LoginDTO("usuario1", "senha123");

                // When
                UsuarioDTO resultado = usuarioService.login(loginDto);

                // Then
                assertThat(resultado)
                                .as("Login deve retornar dados do usuário")
                                .isNotNull();

                assertThat(resultado.getLogin())
                                .as("Login deve corresponder ao solicitado")
                                .isEqualTo("usuario1");
        }

        /**
         * Testa falha na autenticação.
         * 
         * @param login            Login a testar
         * @param senha            Senha a testar
         * @param mensagemEsperada Mensagem esperada na exceção
         * @pre Credenciais inválidas fornecidas
         * @post IllegalArgumentException com mensagem apropriada
         */
        @ParameterizedTest
        @MethodSource("credenciaisInvalidasProvider")
        void testLoginFalha(String login, String senha, String mensagemEsperada) {
                // Given
                CriarUsuarioDTO criarDto = new CriarUsuarioDTO("usuario1", "senha123", "avatar.png");
                usuarioService.criarUsuario(criarDto);

                // Configurar mock para cenários específicos
                if ("usuarioInexistente".equals(login)) {
                        when(userRepository.findByLogin("usuarioInexistente")).thenReturn(Optional.empty());
                } else {
                        Usuario usuarioSalvo = new Usuario("usuario1", "senha123", "avatar.png");
                        when(userRepository.findByLogin("usuario1")).thenReturn(Optional.of(usuarioSalvo));
                }

                LoginDTO loginDto = new LoginDTO(login, senha);

                // When/Then
                assertThatThrownBy(() -> usuarioService.login(loginDto))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining(mensagemEsperada);
        }

        static Stream<Arguments> credenciaisInvalidasProvider() {
                return Stream.of(
                                of("usuarioInexistente", "qualquerSenha", "Usuário não encontrado"),
                                of("usuario1", "senhaErrada", "Senha incorreta"));
        }

        /**
         * Testa registro de simulações.
         * 
         * @param resultados        Array de resultados das simulações
         * @param pontuacaoEsperada Pontuação esperada após simulações
         * @param taxaEsperada      Taxa de sucesso esperada
         * @pre Usuário criado, simulações registradas
         * @post Pontuação e estatísticas atualizadas corretamente
         */
        @ParameterizedTest
        @MethodSource("simulacoesProvider")
        void testRegistroSimulacoes(boolean[] resultados, int pontuacaoEsperada, double taxaEsperada) {
                // Given
                CriarUsuarioDTO dto = new CriarUsuarioDTO("usuario1", "senha123", "avatar.png");
                usuarioService.criarUsuario(dto);

                // Configurar mock para encontrar o usuário durante as operações
                Usuario usuarioSalvo = new Usuario("usuario1", "senha123", "avatar.png");
                when(userRepository.findByLogin("usuario1")).thenReturn(Optional.of(usuarioSalvo));

                // When
                for (boolean resultado : resultados) {
                        usuarioService.registrarSimulacao("usuario1", resultado);
                }

                // Then
                UsuarioDTO usuario = usuarioService.login(new LoginDTO("usuario1", "senha123"));

                assertThat(usuario.getPontuacao())
                                .as("Pontuação deve refletir simulações bem-sucedidas")
                                .isEqualTo(pontuacaoEsperada);

                assertThat(usuario.getTotalSimulacoes())
                                .as("Total de simulações deve ser incrementado")
                                .isEqualTo(resultados.length);

                assertThat(usuario.getTaxaSucesso())
                                .as("Taxa de sucesso deve ser calculada corretamente")
                                .isCloseTo(taxaEsperada, within(0.01));
        }

        static Stream<Arguments> simulacoesProvider() {
                return Stream.of(
                                of(new boolean[] { true }, 1, 100.0), // 1 sucesso de 1
                                of(new boolean[] { false }, 0, 0.0), // 0 sucessos de 1
                                of(new boolean[] { true, true }, 2, 100.0), // 2 sucessos de 2
                                of(new boolean[] { true, false }, 1, 50.0), // 1 sucesso de 2
                                of(new boolean[] { false, false }, 0, 0.0), // 0 sucessos de 2
                                of(new boolean[] { true, false, true }, 2, 66.67) // 2 sucessos de 3
                );
        }

        /**
         * Testa a falha ao registrar simulação para um usuário inexistente.
         *
         * @pre Login de usuário inexistente fornecido
         * @post IllegalArgumentException lançada com a mensagem apropriada
         */
        @Test
        void testRegistroSimulacaoUsuarioInexistente() {
                // Given
                // O mock no setUp() já garante que findByLogin retornará Optional.empty()
                // para qualquer string que não foi explicitamente mockada.
                String loginInexistente = "fantasma";

                // When/Then
                assertThatThrownBy(() -> usuarioService.registrarSimulacao(loginInexistente, true))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("Usuário não encontrado");
        }

        /**
         * Testa exclusão de usuário.
         * 
         * @pre Usuário criado e existente
         * @post Usuário removido, não encontrado após exclusão
         */
        @Test
        void testExclusaoUsuario() {
                // Given
                CriarUsuarioDTO dto = new CriarUsuarioDTO("usuario1", "senha123", "avatar.png");
                usuarioService.criarUsuario(dto);

                // Configurar mock para encontrar usuário e depois simular exclusão
                Usuario usuarioSalvo = new Usuario("usuario1", "senha123", "avatar.png");
                when(userRepository.findByLogin("usuario1"))
                                .thenReturn(Optional.of(usuarioSalvo))
                                .thenReturn(Optional.empty()); // Após exclusão

                // When
                usuarioService.excluirUsuario("usuario1");

                // Then
                assertThatThrownBy(() -> usuarioService.login(new LoginDTO("usuario1", "senha123")))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("Usuário não encontrado");
        }

        /**
         * Testa exclusão de usuário inexistente.
         * 
         * @pre Usuário inexistente no sistema
         * @post IllegalArgumentException lançada
         */
        @Test
        void testExclusaoUsuarioInexistente() {
                // When/Then
                assertThatThrownBy(() -> usuarioService.excluirUsuario("inexistente"))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("Usuário não encontrado");
        }

        /**
         * Testa cálculo de estatísticas do sistema.
         * 
         * @pre Múltiplos usuários com diferentes desempenhos
         * @post Estatísticas calculadas corretamente (totais e médias)
         */
        @Test
        void testEstatisticasSistema() {
                // Given - Criar múltiplos usuários com diferentes desempenhos
                criarUsuarioComSimulacoes("usuario1", new boolean[] { true, true, false }); // 2/3 = 66.67%
                criarUsuarioComSimulacoes("usuario2", new boolean[] { true, false }); // 1/2 = 50%
                criarUsuarioComSimulacoes("usuario3", new boolean[] { false, false }); // 0/2 = 0%

                // Configurar mock para listar todos os usuários
                List<Usuario> todosUsuarios = List.of(
                                new Usuario("usuario1", "senha123", "avatar.png"),
                                new Usuario("usuario2", "senha123", "avatar.png"),
                                new Usuario("usuario3", "senha123", "avatar.png"));
                // Simular pontuações e simulações dos usuários
                todosUsuarios.get(0).setPontuacao(2);
                todosUsuarios.get(0).setTotalSimulacoes(3);
                todosUsuarios.get(1).setPontuacao(1);
                todosUsuarios.get(1).setTotalSimulacoes(2);
                todosUsuarios.get(2).setPontuacao(0);
                todosUsuarios.get(2).setTotalSimulacoes(2);

                when(userRepository.findAll()).thenReturn(todosUsuarios);

                // When
                EstatisticasDTO stats = usuarioService.obterEstatisticas();

                // Then
                assertThat(stats.getTotalUsuarios())
                                .as("Total de usuários deve ser 3")
                                .isEqualTo(3);

                assertThat(stats.getQuantidadeTotalSimulacoes())
                                .as("Total de simulações deve ser 7")
                                .isEqualTo(7);

                assertThat(stats.getMediaSimulacoesSucessoUsuario())
                                .as("Média de sucessos por usuário: (66.67 + 50 + 0) / 3 = 38.89%")
                                .isCloseTo(38.89, within(0.1));

                assertThat(stats.getMediaTotalSimulacoesSucesso())
                                .as("Média total de sucessos: 3/7 = 42.86%")
                                .isCloseTo(42.86, within(0.1));
        }

        /**
         * Helper para criar usuário com simulações específicas.
         */
        private void criarUsuarioComSimulacoes(String login, boolean[] resultados) {
                CriarUsuarioDTO dto = new CriarUsuarioDTO(login, "senha123", "avatar.png");
                usuarioService.criarUsuario(dto);

                // Configurar mock para encontrar o usuário criado
                Usuario usuarioSalvo = new Usuario(login, "senha123", "avatar.png");
                when(userRepository.findByLogin(login)).thenReturn(Optional.of(usuarioSalvo));

                for (boolean resultado : resultados) {
                        usuarioService.registrarSimulacao(login, resultado);
                }
        }

        /**
         * Testa listagem de usuários.
         * 
         * @pre Múltiplos usuários criados
         * @post Todos os usuários retornados na listagem
         */
        @Test
        void testListagemUsuarios() {
                // Given
                usuarioService.criarUsuario(new CriarUsuarioDTO("user1", "pass1", "av1.png"));
                usuarioService.criarUsuario(new CriarUsuarioDTO("user2", "pass2", "av2.png"));
                usuarioService.criarUsuario(new CriarUsuarioDTO("user3", "pass3", "av3.png"));

                // Configurar mock para retornar todos os usuários
                List<Usuario> todosUsuarios = List.of(
                                new Usuario("user1", "pass1", "av1.png"),
                                new Usuario("user2", "pass2", "av2.png"),
                                new Usuario("user3", "pass3", "av3.png"));
                when(userRepository.findAll()).thenReturn(todosUsuarios);

                // When
                List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();

                // Then
                assertThat(usuarios)
                                .as("Deve retornar todos os usuários")
                                .hasSize(3);

                assertThat(usuarios)
                                .extracting(UsuarioDTO::getLogin)
                                .containsExactlyInAnyOrder("user1", "user2", "user3");
        }

        @Test
        void testVerificarSenhaComSenhaArmazenadaNula() {
                // Arrange
                Usuario usuarioComSenhaNula = new Usuario(); // Usa o construtor sem argumentos
                usuarioComSenhaNula.setSenha(null);

                // Act & Assert
                assertThat(usuarioComSenhaNula.verificarSenha("qualquerSenha")).isFalse();
                assertThat(usuarioComSenhaNula.verificarSenha(null)).isFalse();
        }

}
