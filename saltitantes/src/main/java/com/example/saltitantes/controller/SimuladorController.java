package com.example.saltitantes.controller;


import com.example.saltitantes.model.dto.ParametrosDTO;
import com.example.saltitantes.model.dto.SimularResponseDTO;
import com.example.saltitantes.model.service.SimuladorService;

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
    @PostMapping("/simular")
public ResponseEntity<List<SimularResponseDTO>> inicializar(@RequestBody ParametrosDTO parametros) {
    simuladorService.inicializar(parametros.getQuantidade());
    List<SimularResponseDTO> response = simuladorService.simular(parametros.getIteracoes());
    return ResponseEntity.status(HttpStatus.OK).body(response);
}
}
