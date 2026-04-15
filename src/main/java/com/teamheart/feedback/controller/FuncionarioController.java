package com.teamheart.feedback.controller;

import com.teamheart.feedback.entity.Funcionario;
import com.teamheart.feedback.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/diversidade/funcionarios")
@Tag(name = "Funcionários", description = "Gerencia o cadastro e dados de funcionários.")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar funcionário", description = "Cria um novo funcionário no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Erro ao cadastrar funcionário.")
    })
    @PostMapping
    public ResponseEntity<Funcionario> criar(@Valid @RequestBody Funcionario funcionario) {
        Funcionario novo = service.salvar(funcionario);
        return ResponseEntity.status(201).body(novo);
    }

    @Operation(summary = "Listar funcionários", description = "Retorna a lista de todos os funcionários cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso.")
    @GetMapping
    public ResponseEntity<List<Funcionario>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Buscar funcionário por ID", description = "Retorna os dados de um funcionário específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Funcionário encontrado."),
        @ApiResponse(responseCode = "404", description = "Funcionário não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable UUID id) {
        Optional<Funcionario> funcionario = service.buscarPorId(id);
        return funcionario.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar funcionário", description = "Atualiza as informações de um funcionário existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Funcionário não encontrado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable UUID id, @Valid @RequestBody Funcionario dados) {
        Funcionario atualizado = service.atualizar(id, dados);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Excluir funcionário", description = "Remove um funcionário do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Funcionário excluído com sucesso."),
        @ApiResponse(responseCode = "404", description = "Funcionário não encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
