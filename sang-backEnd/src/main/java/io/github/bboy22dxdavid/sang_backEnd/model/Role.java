package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "tb_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(nullable = false, unique = true)
    private String role;

    public Role() {}

    public Role(UUID id, String role) {
        this.id = id;
        this.role = role;
    }

    // Getters e Setters manual se preciso
}
