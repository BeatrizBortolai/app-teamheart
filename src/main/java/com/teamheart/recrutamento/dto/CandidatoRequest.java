package com.teamheart.recrutamento.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CandidatoRequest(
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    String nome,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    String email,

    @NotBlank(message = "O gênero é obrigatório")
    String genero,

    String etnia,

    @NotBlank(message = "A localização é obrigatória")
    String localizacao,

    @NotNull(message = "A experiência é obrigatória")
    @Min(value = 0, message = "A experiência não pode ser negativa")
    Integer experienciaAnos
) {}
