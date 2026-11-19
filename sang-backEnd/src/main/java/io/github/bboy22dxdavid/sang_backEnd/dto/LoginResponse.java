package io.github.bboy22dxdavid.sang_backEnd.dto;

import java.util.UUID;


    public record LoginResponse(
            String token,
            UUID userId,
            String username,
            String saram,
            String posto,
            Integer antiguidade,
            String role

    ) {}