package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;


import java.util.UUID;

@Data
@Entity
@Table(
        name = "designacoes_setor",
        uniqueConstraints = @UniqueConstraint(name = "uq_processo_secao", columnNames = {"processo_id", "secao"})
)
public class DesignacaoSetor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "processo_id", nullable = false)
    private Processo processo;

    @Column(nullable = false, length = 50)
    private String secao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avaliador_id", nullable = false)
    private User avaliador;


}
