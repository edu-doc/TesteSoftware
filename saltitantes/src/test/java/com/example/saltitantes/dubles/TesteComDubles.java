package com.example.saltitantes.dubles;

import com.example.saltitantes.model.entity.Usuario;
import com.example.saltitantes.model.dto.UsuarioDTO;
import com.example.saltitantes.model.dto.EstatisticasDTO;
import com.example.saltitantes.model.dto.LoginDTO;
import com.example.saltitantes.model.dto.CriarUsuarioDTO;
import com.example.saltitantes.repository.UserRepository;
import com.example.saltitantes.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        private Usuario usuarioMock;

        @BeforeEach
        void setUp() {
                usuarioMock = new Usuario();
                usuarioMock.setId(1L);
                usuarioMock.setLogin("user1");
                usuarioMock.setSenha("pass1");
                usuarioMock.setAvatar("avatar1.png");
                usuarioMock.setPontuacao(5);
                usuarioMock.setTotalSimulacoes(10);
        }

        /**
         * Teste com Spy: Verifica se a lógica de registro de simulação
         * corretamente invoca a busca e o salvamento do usuário.
         *
         * @pre Spy do service, repository mockado
         * @post Métodos de busca e salvamento chamados corretamente
         */
        @Test
        void testSpyRegistroSimulacao() {
                // Arrange
                UsuarioService spyUsuarioService = spy(usuarioService);
                when(usuarioRepository.findByLogin("user1")).thenReturn(Optional.of(usuarioMock));
                when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

                // Act
                spyUsuarioService.registrarSimulacao("user1", true);

                // Verify
                verify(spyUsuarioService).registrarSimulacao("user1", true);
                verify(usuarioRepository).findByLogin("user1");
                verify(usuarioRepository).save(any(Usuario.class));
        }

        /**
         * Teste com Mock: Simula um erro na camada de persistência para garantir
         * que a exceção seja propagada corretamente pelo serviço.
         *
         * @pre Repository configurado para lançar exceção
         * @post RuntimeException propagada corretamente
         */
        @Test
        void testMockErroRepositorioAoListar() {
                // Arrange
                when(usuarioRepository.findAll()).thenThrow(new RuntimeException("Erro de conexão com o banco"));

                // Act & Assert
                assertThatThrownBy(() -> usuarioService.obterEstatisticas())
                                .isInstanceOf(RuntimeException.class)
                                .hasMessageContaining("Erro de conexão com o banco");
        }

        /**
         * Teste com Stub: Garante que o cálculo de estatísticas não falhe
         * (ex: divisão por zero) quando não há usuários no sistema.
         *
         * @pre Repository retorna lista vazia
         * @post Estatísticas zeradas retornadas corretamente
         */
        @Test
        void testStubEstatisticasComBancoVazio() {
                // Arrange
                when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

                // Act
                EstatisticasDTO estatisticas = usuarioService.obterEstatisticas();

                // Assert
                assertThat(estatisticas.getTotalUsuarios()).isZero();
                assertThat(estatisticas.getQuantidadeTotalSimulacoes()).isZero();
                assertThat(estatisticas.getMediaSimulacoesSucessoUsuario()).isZero();
                assertThat(estatisticas.getMediaTotalSimulacoesSucesso()).isZero();
        }
}
