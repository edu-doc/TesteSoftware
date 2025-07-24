package com.example.saltitantes.estrutural; // Ou o pacote correto do seu teste

import com.example.saltitantes.model.dto.UsuarioDTO;
import com.example.saltitantes.model.entity.Usuario;
import com.example.saltitantes.model.service.UsuarioService;
import com.example.saltitantes.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    /**
     * TESTE DO BRANCH 1: Usuário não encontrado
     */
    @Test
    void obterEstatisticasUsuario_deveLancarExcecao_quandoUsuarioNaoExiste() {
        // Arrange (Preparação)
        String loginInexistente = "naoexiste";
        // Configura o mock para retornar um Optional vazio
        when(userRepository.findByLogin(loginInexistente)).thenReturn(Optional.empty());

        // Act & Assert (Ação e Verificação)
        assertThatThrownBy(() -> usuarioService.obterEstatisticasUsuario(loginInexistente))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário não encontrado.");
    }

    /**
     * TESTE DO BRANCH 2: Usuário encontrado
     */
    @Test
    void obterEstatisticasUsuario_deveRetornarDTO_quandoUsuarioExiste() {
        // Arrange (Preparação)
        String loginExistente = "admin";
        // Cria um usuário falso para ser retornado pelo mock
        // Supondo um construtor: new Usuario(login, senha, totalSimulacoes, sucessoSimulacoes)
        Usuario usuarioDeTeste = new Usuario(1L, loginExistente, "senha123", "avatar.png", 5, 10);

        // Configura o mock para encontrar este usuário
        when(userRepository.findByLogin(loginExistente)).thenReturn(Optional.of(usuarioDeTeste));

        // Act (Ação)
        UsuarioDTO resultadoDTO = usuarioService.obterEstatisticasUsuario(loginExistente);

        // Assert (Verificação)
        assertThat(resultadoDTO).isNotNull();
        assertThat(resultadoDTO.getLogin()).isEqualTo(loginExistente);
        // Assumindo que o DTO tem esses getters
        assertThat(resultadoDTO.getTotalSimulacoes()).isEqualTo(10);
        assertThat(resultadoDTO.getTaxaSucesso()).isEqualTo(50);
    }
}