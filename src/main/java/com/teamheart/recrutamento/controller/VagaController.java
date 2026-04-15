package com.teamheart.recrutamento.controller;

import com.teamheart.recrutamento.dto.VagaRequest;
import com.teamheart.recrutamento.dto.VagaResponse;
import com.teamheart.recrutamento.entity.Vaga;
import com.teamheart.recrutamento.service.VagaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Vagas", description = "Gerencia o cadastro de vagas priorizando diversidade.")
@RestController
@RequestMapping("/vagas")
public class VagaController {

    private final VagaService service;

    public VagaController(VagaService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar nova vaga")
    @ApiResponse(responseCode = "201", description = "Vaga criada com sucesso")
    @PostMapping
    public ResponseEntity<VagaResponse> criar(@Valid @RequestBody VagaRequest dto) {
        Vaga v = service.salvar(dto);
        VagaResponse response = new VagaResponse(
            v.getId(), v.getTitulo(), v.getDescricao(), v.getDepartamento(),
            v.getNivel(), v.getMetaDiversidade(), v.getDataAbertura()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todas as vagas")
    @GetMapping
    public List<VagaResponse> listar() {
        return service.listarTodas().stream()
            .map(v -> new VagaResponse(
                v.getId(), v.getTitulo(), v.getDescricao(), v.getDepartamento(),
                v.getNivel(), v.getMetaDiversidade(), v.getDataAbertura()
            )).toList();
    }

    @Operation(summary = "Excluir vaga por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Vaga excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Vaga não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
