package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;




import java.util.UUID;

@Data
@Entity
@Table(name = "efetivo")
public class Efetivo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String saram;

    @Column(name = "nome_guerra", nullable = false, length = 100)
    private String nomeGuerra;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(nullable = false, length = 50)
    private String secao;

    @Column(name = "cod_posto", nullable = false, length = 20)
    private String codPosto;

    @Column(nullable = false, length = 50)
    private String posto;

    @Column(nullable = false, length = 30)
    private String quadro;

    @Column(nullable = false, length = 100)
    private String funcao;

    @Column(nullable = false)
    private Integer antiguidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vinculo", nullable = false, length = 20)
    private TipoVinculo tipoVinculo;

    @Column(nullable = false)
    private Boolean ativo = true;
}
