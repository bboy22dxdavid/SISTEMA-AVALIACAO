package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 20)
    private String saram;

    @Column(nullable = false, length = 50)
    private String posto;

    @Column(name = "cod_posto", nullable = false, length = 20)
    private String codPosto;

    @Column(nullable = false)
    private Integer antiguidade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean ativo = true;

    //GETTERS E SET

}
