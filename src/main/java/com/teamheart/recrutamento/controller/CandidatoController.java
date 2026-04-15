package com.teamheart.recrutamento.controller;

import com.teamheart.recrutamento.dto.CandidatoRequest;
import com.teamheart.recrutamento.dto.CandidatoResponse;
import com.teamheart.recrutamento.entity.Candidato;
import com.teamheart.recrutamento.service.CandidatoService;
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

@Tag(name = "Candidatos", description = "Gerencia informações de candidatos do processo seletivo.")
@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    private final CandidatoService service;

    public CandidatoController(CandidatoService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar novo candidato")
    @ApiResponse(responseCode = "201", description = "Candidato criado com sucesso")
    @PostMapping
    public ResponseEntity<CandidatoResponse> criar(@Valid @RequestBody CandidatoRequest dto) {
        Candidato c = service.salvar(dto);
        CandidatoResponse response = new CandidatoResponse(
            c.getId(), c.getNome(), c.getEmail(), c.getGenero(),
            c.getEtnia(), c.getLocalizacao(), c.getExperienciaAnos(), c.getDataRegistro()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos os candidatos")
    @GetMapping
    public List<CandidatoResponse> listar() {
        return service.listarTodos().stream()
            .map(c -> new CandidatoResponse(
                c.getId(), c.getNome(), c.getEmail(), c.getGenero(),
                c.getEtnia(), c.getLocalizacao(), c.getExperienciaAnos(), c.getDataRegistro()
            )).toList();
    }

    @Operation(summary = "Excluir candidato por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Candidato excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Candidato não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
