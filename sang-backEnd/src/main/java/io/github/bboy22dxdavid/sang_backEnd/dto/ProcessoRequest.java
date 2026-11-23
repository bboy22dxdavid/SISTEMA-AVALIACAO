package io.github.bboy22dxdavid.sang_backEnd.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProcessoRequest(
        @NotNull(message = "O ano do processo é obrigatório.")
        @Min(value = 2000, message = "Informe um ano válido.")
        Integer ano
) { }
