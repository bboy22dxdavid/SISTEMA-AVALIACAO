package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Data
@Entity
@Table(
        name = "avaliacoes",
        uniqueConstraints = @UniqueConstraint(name = "uq_avaliacao_processo_militar", columnNames = {"processo_id", "avaliado_id"})
)
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "processo_id", nullable = false)
    private Processo processo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avaliado_id", nullable = false)
    private Efetivo avaliado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avaliador_id", nullable = false)
    private User avaliador;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Integer> criterios;

    @Column(name = "nota_total", nullable = false)
    private Integer notaTotal;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String justificativa;

    @Column(name = "conceito_moral", nullable = false, columnDefinition = "TEXT")
    private String conceitoMoral;

    @Column(name = "conceito_profissional", nullable = false, columnDefinition = "TEXT")
    private String conceitoProfissional;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
