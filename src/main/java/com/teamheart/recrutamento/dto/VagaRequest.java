package com.teamheart.recrutamento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VagaRequest(
    @NotBlank(message = "O título é obrigatório")
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres")
    String titulo,

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    String descricao,

    @NotBlank(message = "O departamento é obrigatório")
    String departamento,

    @NotBlank(message = "O nível é obrigatório")
    String nivel,

    @NotNull(message = "A meta de diversidade é obrigatória")
    @Min(value = 0, message = "A meta de diversidade não pode ser negativa")
    Integer metaDiversidade
) {}
