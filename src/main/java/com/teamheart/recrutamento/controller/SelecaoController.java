package com.teamheart.recrutamento.controller;

import com.teamheart.recrutamento.dto.SelecaoRequest;
import com.teamheart.recrutamento.dto.SelecaoResponse;
import com.teamheart.recrutamento.entity.Selecao;
import com.teamheart.recrutamento.service.SelecaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Seleção", description = "Gerencia o processo automatizado de seleção de candidatos priorizando diversidade.")
@RestController
@RequestMapping("/selecoes")
public class SelecaoController {

    private final SelecaoService service;

    public SelecaoController(SelecaoService service) {
        this.service = service;
    }

    @Operation(
        summary = "Registrar seleção",
        description = "Cria uma nova seleção entre candidato e vaga, aplicando critérios automáticos de diversidade."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Seleção criada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Candidato ou vaga não encontrados")
    })
    @PostMapping
    public ResponseEntity<SelecaoResponse> registrar(@Valid @RequestBody SelecaoRequest dto) {
        Selecao selecao = service.registrarSelecao(dto.idCandidato(), dto.idVaga());
        SelecaoResponse response = new SelecaoResponse(
            selecao.getId(),
            selecao.getCandidato().getNome(),
            selecao.getVaga().getTitulo(),
            selecao.getStatus(),
            selecao.getPriorizado(),
            selecao.getDataAvaliacao()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todas as seleções",
        description = "Retorna todas as seleções registradas no sistema.")
    @GetMapping
    public List<SelecaoResponse> listar() {
        return service.listarTodas().stream()
            .map(s -> new SelecaoResponse(
                s.getId(),
                s.getCandidato().getNome(),
                s.getVaga().getTitulo(),
                s.getStatus(),
                s.getPriorizado(),
                s.getDataAvaliacao()
            ))
            .toList();
    }
}
