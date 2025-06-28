package com.example.saltitantes.controller;

import com.example.saltitantes.model.dto.CriarUsuarioDTO;
import com.example.saltitantes.model.dto.EstatisticasDTO;
import com.example.saltitantes.model.dto.LoginDTO;
import com.example.saltitantes.model.dto.LoginResponse;
import com.example.saltitantes.model.dto.UsuarioDTO;
import com.example.saltitantes.model.service.UsuarioService;

import java.util.List;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para operações relacionadas a usuários.
 */
@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Cria um novo usuário.
     * 
     * @param criarUsuarioDTO dados do usuário a ser criado
     * @return resposta com o usuário criado
     */
    @PostMapping
    public ResponseEntity<UsuarioDTO> criarUsuario(@RequestBody CriarUsuarioDTO criarUsuarioDTO) {
        try {
            UsuarioDTO usuario = usuarioService.criarUsuario(criarUsuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Realiza login do usuário.
     * 
     * @param loginDTO credenciais de login
     * @return resposta com informações do usuário
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDTO loginDTO) {
        try {
            UsuarioDTO usuario = usuarioService.login(loginDTO);
            return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso!", true, usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(e.getMessage(), false, null));
        }
    }

    /**
     * Lista todos os usuários cadastrados.
     * 
     * @return lista de usuários
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Exclui um usuário.
     * 
     * @param login login do usuário a ser excluído
     * @return resposta de confirmação
     */
    @DeleteMapping("/{login}")
    public ResponseEntity<?> excluirUsuario(@PathVariable String login) {
        try {
            usuarioService.excluirUsuario(login);
            return ResponseEntity.ok("Usuário excluído com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Obtém as estatísticas do sistema.
     * 
     * @return estatísticas da simulação
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<EstatisticasDTO> obterEstatisticas() {
        EstatisticasDTO estatisticas = usuarioService.obterEstatisticas();
        return ResponseEntity.ok(estatisticas);
    }
}
