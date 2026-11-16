package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String saram;

    @Column(name = "nguerra", nullable = false)
    private String nguerra;

    @Column(nullable = false)
    private String secao;

    @Column(name = "cod_posto", nullable = false)
    private String codposto;

    @Column(nullable = false)
    private String posto;

    @Column(nullable = false)
    private String quadro;

    @Column(nullable = false)
    private String funcao;

    @Column(nullable = false)
    private Integer antiguidade;

    @Column(nullable = false)
    private Boolean ativo = true;

    // Relacionamento Manual: Salvamos apenas o ID numérico da Role física do banco
    // FetchType.EAGER explicitado e referencedColumnName garantindo o vínculo exato com o UUID de Role
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    // Construtores
    public User() {}



    // Construtores, Getters e Setters Manuais

}
