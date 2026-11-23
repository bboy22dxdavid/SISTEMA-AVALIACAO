package io.github.bboy22dxdavid.sang_backEnd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record AvaliacaoRequest(
        @NotNull(message = "O ID do processo é obrigatório.")
        UUID processoId,

        @NotNull(message = "O ID do militar avaliado é obrigatório.")
        UUID avaliadoId,

        @NotEmpty(message = "Os critérios de avaliação são obrigatórios.")
        Map<String, Integer> criterios,

        @NotBlank(message = "A justificativa técnica é obrigatória.")
        String justificativa,

        @NotBlank(message = "O conceito moral é obrigatório.")
        String conceitoMoral,

        @NotBlank(message = "O conceito profissional é obrigatório.")
        String conceitoProfissional
) { }
