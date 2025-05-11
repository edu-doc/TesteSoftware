package com.example.saltitantes.controller;

import com.example.saltitantes.model.dto.CriaturasDTO;
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
    public String inicializar(@RequestParam int quantidade, @RequestParam int iteracoes) {
        simuladorService.inicializar(quantidade);
        simuladorService.simular(iteracoes);
        return "Simulação iniciada com " + quantidade + " criaturas " + "com um total de " + iteracoes + "iterações";
    }


    @GetMapping("/estado")
    public List<CriaturasDTO> getEstado() {
        return simuladorService.getEstadoAtual();
    }
}
