package com.example.saltitantes.dubles;

import com.example.saltitantes.model.service.UsuarioService;
import com.example.saltitantes.model.entity.Usuario;
import com.example.saltitantes.model.dto.UsuarioDTO;
import com.example.saltitantes.model.dto.EstatisticasDTO;
import com.example.saltitantes.model.dto.LoginDTO;
import com.example.saltitantes.model.dto.CriarUsuarioDTO;
import com.example.saltitantes.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.data.Offset.offset;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

/**
 * Testes com Dublês de Teste (Mocks, Stubs, Spies)
 * 
 * Focam em:
 * - Isolamento de unidades de código
 * - Verificação de interações entre componentes
 * - Simulação de cenários específicos
 * - Controle de dependências externas
 */
@ExtendWith(MockitoExtension.class)
public class TesteComDubles {

        @Mock
        private UserRepository usuarioRepository;

        @InjectMocks
        private UsuarioService usuarioService;

        private Usuario usuarioMock1;
        private Usuario usuarioMock2;
        private Usuario usuarioMock3;

        @BeforeEach
        void setUp() {
                // Criar usuários fictícios para testes
                usuarioMock1 = new Usuario();
                usuarioMock1.setId(1L);
                usuarioMock1.setLogin("user1");
                usuarioMock1.setSenha("pass1");
                usuarioMock1.setAvatar("avatar1.png");
                usuarioMock1.setPontuacao(5);
                usuarioMock1.setTotalSimulacoes(10);

                usuarioMock2 = new Usuario();
                usuarioMock2.setId(2L);
                usuarioMock2.setLogin("user2");
                usuarioMock2.setSenha("pass2");
                usuarioMock2.setAvatar("avatar2.png");
                usuarioMock2.setPontuacao(3);
                usuarioMock2.setTotalSimulacoes(8);

                usuarioMock3 = new Usuario();
                usuarioMock3.setId(3L);
                usuarioMock3.setLogin("user3");
                usuarioMock3.setSenha("pass3");
                usuarioMock3.setAvatar("avatar3.png");
                usuarioMock3.setPontuacao(0);
                usuarioMock3.setTotalSimulacoes(2);
        }

        /**
         * Teste com Stub: Simular login de usuário
         * 
         * Utiliza stub para simular o comportamento do repositório
         * sem dependência de banco de dados.
         */
        @Test
        void testStubLoginUsuario() {
                // Arrange - Configurar stub
                when(usuarioRepository.findByLogin("user1"))
                                .thenReturn(Optional.of(usuarioMock1));
                when(usuarioRepository.findByLogin("inexistente"))
                                .thenReturn(Optional.empty());

                LoginDTO loginValido = new LoginDTO("user1", "pass1");
                LoginDTO loginInvalido = new LoginDTO("inexistente", "qualquer");

                // Act & Assert - Login válido
                var usuarioLogado = usuarioService.login(loginValido);
                assertThat(usuarioLogado)
                                .as("Deve fazer login com credenciais válidas")
                                .isNotNull()
                                .extracting(UsuarioDTO::getLogin)
                                .isEqualTo("user1");

                // Act & Assert - Login inválido
                assertThatThrownBy(() -> usuarioService.login(loginInvalido))
                                .as("Deve rejeitar login com credenciais inválidas")
                                .isInstanceOf(RuntimeException.class);

                // Verify - Verificar interações
                verify(usuarioRepository, times(1)).findByLogin("user1");
                verify(usuarioRepository, times(1)).findByLogin("inexistente");
        }

        /**
         * Teste com Mock: Verificar criação de usuário
         * 
         * Utiliza mock para verificar se o método save do repositório
         * é chamado corretamente durante a criação.
         */
        @Test
        void testMockCriacaoUsuario() {
                // Arrange
                CriarUsuarioDTO novoUsuario = new CriarUsuarioDTO("newuser", "newpass", "newavatar.png");

                Usuario usuarioSalvo = new Usuario();
                usuarioSalvo.setId(4L);
                usuarioSalvo.setLogin("newuser");
                usuarioSalvo.setSenha("newpass");
                usuarioSalvo.setAvatar("newavatar.png");
                usuarioSalvo.setPontuacao(0);
                usuarioSalvo.setTotalSimulacoes(0);

                when(usuarioRepository.findByLogin("newuser")).thenReturn(Optional.empty());
                when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

                // Act
                var resultado = usuarioService.criarUsuario(novoUsuario);

                // Assert
                assertThat(resultado)
                                .as("Deve criar usuário com sucesso")
                                .isNotNull()
                                .extracting(UsuarioDTO::getLogin)
                                .isEqualTo("newuser");

                // Verify - Verificar que save foi chamado
                verify(usuarioRepository, times(1)).save(argThat(usuario -> "newuser".equals(usuario.getLogin()) &&
                                "newpass".equals(usuario.getSenha()) &&
                                "newavatar.png".equals(usuario.getAvatar())));
        }

        /**
         * Teste com Mock: Verificar falha na criação por usuário existente
         * 
         * Simula tentativa de criar usuário com login já existente.
         */
        @Test
        void testMockFalhaCriacaoUsuarioExistente() {
                // Arrange
                CriarUsuarioDTO usuarioExistente = new CriarUsuarioDTO("user1", "newpass", "newavatar.png");

                when(usuarioRepository.findByLogin("user1")).thenReturn(Optional.of(usuarioMock1));

                // Act & Assert
                assertThatThrownBy(() -> usuarioService.criarUsuario(usuarioExistente))
                                .as("Deve lançar exceção para usuário já existente")
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("já existe");

                // Verify - Verificar que save nunca foi chamado
                verify(usuarioRepository, never()).save(any(Usuario.class));
        }

        /**
         * Teste com Stub: Simular cálculo de estatísticas
         * 
         * Utiliza stub para fornecer dados específicos para
         * testar o cálculo das estatísticas.
         */
        @Test
        void testStubCalculoEstatisticas() {
                // Arrange
                List<Usuario> usuariosSimulados = Arrays.asList(usuarioMock1, usuarioMock2, usuarioMock3);
                when(usuarioRepository.findAll()).thenReturn(usuariosSimulados);

                // Act
                EstatisticasDTO estatisticas = usuarioService.obterEstatisticas();

                // Assert
                assertThat(estatisticas.getQuantidadeTotalSimulacoes())
                                .as("Total de simulações deve ser soma de todos os usuários")
                                .isEqualTo(20); // 10 + 8 + 2

                assertThat(estatisticas.getMediaSimulacoesSucessoUsuario())
                                .as("Média de sucessos por usuário deve ser calculada corretamente")
                                .isCloseTo((50.0 + 37.5 + 0.0) / 3.0, offset(0.1)); // Média das taxas de sucesso
                                                                                    // percentuais

                assertThat(estatisticas.getMediaTotalSimulacoesSucesso())
                                .as("Média total de sucessos deve ser calculada corretamente")
                                .isCloseTo(40.0, offset(0.1)); // 8 sucessos de 20 total = 40%

                // Verify
                verify(usuarioRepository, times(1)).findAll();
        }

        /**
         * Teste com Spy: Verificar atualização de estatísticas
         * 
         * Utiliza spy para verificar se métodos internos são chamados
         * durante o registro de simulação.
         */
        @Test
        void testSpyRegistroSimulacao() {
                // Arrange
                UsuarioService spyUsuarioService = spy(usuarioService);

                when(usuarioRepository.findByLogin("user1")).thenReturn(Optional.of(usuarioMock1));
                when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock1);

                // Act
                spyUsuarioService.registrarSimulacao("user1", true);

                // Assert & Verify
                verify(spyUsuarioService, times(1)).registrarSimulacao("user1", true);
                verify(usuarioRepository, times(1)).findByLogin("user1");
                verify(usuarioRepository, times(1)).save(any(Usuario.class));
        }

        /**
         * Teste com Mock: Simulação de erro do repositório
         * 
         * Simula erro na camada de persistência para testar
         * o tratamento de exceções.
         */
        @Test
        void testMockErroRepositorio() {
                // Arrange
                when(usuarioRepository.findAll()).thenThrow(new RuntimeException("Erro de conexão"));

                // Act & Assert
                assertThatThrownBy(() -> usuarioService.obterEstatisticas())
                                .as("Deve propagar erro do repositório")
                                .isInstanceOf(RuntimeException.class)
                                .hasMessageContaining("Erro de conexão");

                // Verify
                verify(usuarioRepository, times(1)).findAll();
        }

        /**
         * Teste com Stub: Cenário de banco vazio
         * 
         * Simula situação onde não há usuários cadastrados.
         */
        @Test
        void testStubBancoVazio() {
                // Arrange
                when(usuarioRepository.findAll()).thenReturn(Arrays.asList());

                // Act
                EstatisticasDTO estatisticas = usuarioService.obterEstatisticas();

                // Assert
                assertThat(estatisticas.getQuantidadeTotalSimulacoes())
                                .as("Sem usuários, total deve ser 0")
                                .isEqualTo(0);

                assertThat(estatisticas.getMediaSimulacoesSucessoUsuario())
                                .as("Sem usuários, média por usuário deve ser 0")
                                .isEqualTo(0.0);

                assertThat(estatisticas.getMediaTotalSimulacoesSucesso())
                                .as("Sem usuários, média total deve ser 0")
                                .isEqualTo(0.0);
        }

        /**
         * Teste com Mock: Verificar listagem de usuários
         * 
         * Testa o método que retorna todos os usuários cadastrados.
         */
        @Test
        void testMockListagemUsuarios() {
                // Arrange
                List<Usuario> usuarios = Arrays.asList(usuarioMock1, usuarioMock2);
                when(usuarioRepository.findAll()).thenReturn(usuarios);

                // Act
                var listaUsuarios = usuarioService.listarUsuarios();

                // Assert
                assertThat(listaUsuarios)
                                .as("Deve retornar lista de usuários")
                                .hasSize(2)
                                .extracting(UsuarioDTO::getLogin)
                                .containsExactly("user1", "user2");

                // Verify
                verify(usuarioRepository, times(1)).findAll();
        }

        /**
         * Teste com Mock: Verificar exclusão de usuário
         * 
         * Testa a funcionalidade de exclusão de usuário.
         */
        @Test
        void testMockExclusaoUsuario() {
                // Arrange
                when(usuarioRepository.findByLogin("user1")).thenReturn(Optional.of(usuarioMock1));
                doNothing().when(usuarioRepository).delete(usuarioMock1);

                // Act
                usuarioService.excluirUsuario("user1");

                // Verify
                verify(usuarioRepository, times(1)).findByLogin("user1");
                verify(usuarioRepository, times(1)).delete(usuarioMock1);
        }

        /**
         * Teste com Mock: Verificar se usuário existe
         * 
         * Testa o método de verificação de existência de usuário.
         */
        @Test
        void testMockVerificarExistenciaUsuario() {
                // Arrange
                when(usuarioRepository.findByLogin("user1")).thenReturn(Optional.of(usuarioMock1));
                when(usuarioRepository.findByLogin("inexistente")).thenReturn(Optional.empty());

                // Act & Assert
                assertThat(usuarioService.usuarioExiste("user1"))
                                .as("Deve confirmar que usuário existe")
                                .isTrue();

                assertThat(usuarioService.usuarioExiste("inexistente"))
                                .as("Deve confirmar que usuário não existe")
                                .isFalse();

                // Verify
                verify(usuarioRepository, times(1)).findByLogin("user1");
                verify(usuarioRepository, times(1)).findByLogin("inexistente");
        }
}
