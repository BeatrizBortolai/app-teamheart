package com.teamheart.feedback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record FeedbackRequestDTO(
    @NotBlank(message = "O sentimento é obrigatório")
    @Size(max = 20, message = "O sentimento deve ter no máximo 20 caracteres")
    String sentimento,

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres")
    String comentario,

    @NotNull(message = "O id do funcionário é obrigatório")
    UUID idFuncionario
) { }
