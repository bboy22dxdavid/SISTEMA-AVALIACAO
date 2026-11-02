package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_avaliados")
public class Avaliado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String saram;

    @Column(name = "nguerra", nullable = false)
    private String nGuerra;

    @Column(nullable = false)
    private String secao;

    @Column(name = "cod_posto", nullable = false)
    private String codPosto;

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

    // Construtores
    public Avaliado() {}

    public Avaliado(Long id, Boolean ativo, Integer antiguidade, String quadro, String funcao, String posto,
                    String codPosto, String secao, String nGuerra, String saram) {
        this.id = id;
        this.ativo = ativo;
        this.antiguidade = antiguidade;
        this.quadro = quadro;
        this.funcao = funcao;
        this.posto = posto;
        this.codPosto = codPosto;
        this.secao = secao;
        this.nGuerra = nGuerra;
        this.saram = saram;
    }
}
