package io.github.bboy22dxdavid.sang_backEnd.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    private UUID id;
    private UUID roleId;
    private String username;

    //construtor da class
    public UserDTO() {}

    public UserDTO(UUID id, UUID roleId, String username) {
        this.id = id;
        this.roleId = roleId;
        this.username = username;
    }

    // Getters e Setters

}
