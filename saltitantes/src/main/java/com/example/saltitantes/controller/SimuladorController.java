package com.example.saltitantes.controller;

import com.example.saltitantes.model.dto.ParametrosDTO;
import com.example.saltitantes.model.dto.SimularResponseDTO;
import com.example.saltitantes.model.service.SimuladorService;
import com.example.saltitantes.model.service.UsuarioService;
import lombok.AllArgsConstructor;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class SimuladorController {

    private final SimuladorService simuladorService;
    private final UsuarioService usuarioService;

    @PostMapping("/simular")
    public ResponseEntity<?> simular(@RequestBody ParametrosDTO parametros) {
        try {
            simuladorService.inicializar(parametros.getQuantidade());
            List<SimularResponseDTO> response = simuladorService.simular(parametros.getIteracoes());

            // Verificar se a simulação foi bem-sucedida
            boolean bemSucedida = response.stream()
                    .anyMatch(SimularResponseDTO::isSimulacaoBemSucedida);

            // Registrar simulação para o usuário se fornecido
            if (parametros.getLoginUsuario() != null && !parametros.getLoginUsuario().isEmpty()) {
                if (usuarioService.usuarioExiste(parametros.getLoginUsuario())) {
                    usuarioService.registrarSimulacao(parametros.getLoginUsuario(), bemSucedida);
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
        }
    }
}
