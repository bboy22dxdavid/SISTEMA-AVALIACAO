package io.github.bboy22dxdavid.sang_backEnd.dto;

import java.util.UUID;

public class UserDTO {

    private UUID id;
    private String username;
    private Integer roleId;

    //construtor da class
    public UserDTO() {}

    public UserDTO(UUID id, Integer roleId, String username) {
        this.id = id;
        this.roleId = roleId;
        this.username = username;
    }

    // Getters e Setters
    public UUID getId() {return id;}
    public void setId(UUID id) {this.id = id;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
