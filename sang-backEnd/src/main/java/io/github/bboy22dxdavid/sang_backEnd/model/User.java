package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

import javax.management.relation.Role;

@Data
@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // Relacionamento Muitos para Um com a tabela de Perfis
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    //construtor vazio
    public User() {}

    public User(Long id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters e Setters manual se preciso
}
