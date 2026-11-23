package io.github.bboy22dxdavid.sang_backEnd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record NivelamentoRequest(
        @NotNull(message = "O ID da avaliação original é obrigatório.")
        UUID avaliacaoId,

        @NotEmpty(message = "Os critérios revisados são obrigatórios.")
        Map<String, Integer> criterios,

        String justificativaRevisao,

        @NotBlank(message = "O conceito moral é obrigatório.")
        String conceitoMoral,

        @NotBlank(message = "O conceito profissional é obrigatório.")
        String conceitoProfissional
) { }
