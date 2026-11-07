package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "tb_avaliados")
public class Avaliado {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "saram", nullable = false, unique = true)
    private String saram;

    @Column(name = "nguerra", nullable = false)
    private String nguerra;

    @Column(name = "secao", nullable = false)
    private String secao;

    @Column(name = "cod_posto", nullable = false)
    private String codposto;

    @Column(name = "posto", nullable = false)
    private String posto;

    @Column(name = "quadro", nullable = false)
    private String quadro;

    @Column(name = "funcao", nullable = false)
    private String funcao;

    @Column(name = "antiguidade", nullable = false)
    private Integer antiguidade;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    //construtor
    public Avaliado() {}

}
