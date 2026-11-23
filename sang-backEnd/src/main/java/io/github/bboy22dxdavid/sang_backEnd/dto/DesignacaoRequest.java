package io.github.bboy22dxdavid.sang_backEnd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DesignacaoRequest(
        @NotNull(message = "O ID do processo é obrigatório.")
        UUID processoId,

        @NotBlank(message = "A seção é obrigatória.")
        String secao,

        @NotNull(message = "O ID do avaliador é obrigatório.")
        UUID avaliadorId
) { }
