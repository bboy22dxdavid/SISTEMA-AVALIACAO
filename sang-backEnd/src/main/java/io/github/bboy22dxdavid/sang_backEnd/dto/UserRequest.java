package io.github.bboy22dxdavid.sang_backEnd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotBlank(message = "O username (login LDAP) é obrigatório.")
        String username,

        @NotBlank(message = "O SARAM é obrigatório.")
        String saram,

        @NotBlank(message = "O posto é obrigatório.")
        String posto,

        @NotBlank(message = "O código do posto é obrigatório.")
        String codPosto,

        @NotNull(message = "A antiguidade é obrigatória.")
        Integer antiguidade,

        @NotBlank(message = "O perfil de acesso (role) é obrigatório.")
        String roleName,

        Boolean ativo
) { }
