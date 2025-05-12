package com.example.saltitantes.controller;

import com.example.saltitantes.model.dto.CriaturasDTO;
import com.example.saltitantes.model.dto.ParametrosDTO;
import com.example.saltitantes.model.service.Simulador;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class SimuladorController {

    private final Simulador simuladorService;

    public SimuladorController(Simulador simuladorService) {
        this.simuladorService = simuladorService;
    }

    @PostMapping("/start")
    public String inicializar(@RequestBody ParametrosDTO parametros) {

        simuladorService.inicializar(parametros.getQuantidade());
        simuladorService.simular(parametros.getIteracoes());
        return "Simulação iniciada com " + parametros.getQuantidade() + " criaturas " + "com um total de " + parametros.getIteracoes() + " iterações";
    }


    @GetMapping("/estado")
    public List<List<CriaturasDTO>> getEstado() {
        return simuladorService.getHistoricoSimulacoes();
    }
}
