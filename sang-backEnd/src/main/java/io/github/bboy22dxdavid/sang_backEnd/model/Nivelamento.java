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
@Table(name = "nivelamentos")
public class Nivelamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "avaliacao_id", nullable = false, unique = true)
    private Avaliacao avaliacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "revisor_id", nullable = false)
    private User revisor;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, Integer> criterios;

    @Column(name = "nota_total_revisada", nullable = false)
    private Integer notaTotalRevisada;

    @Column(name = "justificativa_revisao", columnDefinition = "TEXT")
    private String justificativaRevisao;

    @Column(name = "conceito_moral", nullable = false, columnDefinition = "TEXT")
    private String conceitoMoral;

    @Column(name = "conceito_profissional", nullable = false, columnDefinition = "TEXT")
    private String conceitoProfissional;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
