package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String role;

    //construtor vazio
    public Role() {}

    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    // Getters e Setters manual se preciso
}
