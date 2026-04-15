package com.teamheart.recrutamento.dto;

import jakarta.validation.constraints.NotNull;

public record SelecaoRequest(
    @NotNull(message = "O id do candidato é obrigatório")
    Long idCandidato,

    @NotNull(message = "O id da vaga é obrigatório")
    Long idVaga
) { }
