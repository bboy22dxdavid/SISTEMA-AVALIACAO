package io.github.bboy22dxdavid.sang_backEnd.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class LoginResponseDTO {
    private String token;
    private UUID userId;
    private String username;
    private String role;

    public LoginResponseDTO(String token, UUID userId,String username, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}
