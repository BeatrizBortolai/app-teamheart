package com.teamheart.feedback.controller;

import com.teamheart.feedback.dto.FeedbackRequestDTO;
import com.teamheart.feedback.dto.FeedbackResponseDTO;
import com.teamheart.feedback.service.FeedbackService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/diversidade/feedbacks")
@Tag(name = "Feedbacks", description = "Gerencia os feedbacks sobre clima de inclusão e bem-estar dos funcionários")
public class FeedbackController {

    private final FeedbackService service;

    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar feedback",
        description = "Cria um novo registro de feedback de funcionário (sigiloso).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Feedback criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Erro de validação ou ID de funcionário inválido/não existente.")
    })
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> criar(@Valid @RequestBody FeedbackRequestDTO requestDTO) {
        FeedbackResponseDTO novo = service.salvar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @Operation(summary = "Listar feedbacks",
        description = "Retorna todos os feedbacks registrados (apenas dados sigilosos).")
    @ApiResponse(responseCode = "200", description = "Lista de feedbacks retornada com sucesso.")
    @GetMapping
    public ResponseEntity<List<FeedbackResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Buscar feedback por ID",
        description = "Retorna um feedback específico pelo seu ID (sigiloso).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feedback encontrado."),
        @ApiResponse(responseCode = "404", description = "Feedback não encontrado."),
        @ApiResponse(responseCode = "400", description = "ID fornecido está em um formato UUID inválido.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> buscarPorId(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        FeedbackResponseDTO feedback = service.buscarPorId(uuid);
        return ResponseEntity.ok(feedback);
    }

    @Operation(summary = "Atualizar feedback",
        description = "Atualiza um feedback existente (apenas campos permitidos).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feedback atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Feedback não encontrado."),
        @ApiResponse(responseCode = "400",
            description = "ID fornecido está em um formato UUID inválido ou dados da requisição são inválidos.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> atualizar(@PathVariable String id,
                                                         @Valid @RequestBody FeedbackRequestDTO dados) {
        UUID uuid = UUID.fromString(id);
        FeedbackResponseDTO atualizado = service.atualizar(uuid, dados);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Excluir feedback", description = "Remove um feedback do sistema pelo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Feedback excluído com sucesso."),
        @ApiResponse(responseCode = "404", description = "Feedback não encontrado."),
        @ApiResponse(responseCode = "400", description = "ID fornecido está em um formato UUID inválido.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        UUID uuid = UUID.fromString(id);
        service.deletar(uuid);
        return ResponseEntity.noContent().build();
    }
}
