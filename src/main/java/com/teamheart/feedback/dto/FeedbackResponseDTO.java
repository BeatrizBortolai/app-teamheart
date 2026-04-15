package com.teamheart.feedback.dto;

import java.time.LocalDate;
import java.util.UUID;

public record FeedbackResponseDTO(
    UUID id,
    String sentimento,
    String comentario,
    LocalDate data,
    Integer idadeFuncionario,
    String cargoFuncionario,
    String departamentoFuncionario
) { }
