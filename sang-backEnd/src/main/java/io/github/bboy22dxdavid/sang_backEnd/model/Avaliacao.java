package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "tb_avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "avaliado_id", referencedColumnName = "id", nullable = false)
    private Avaliado avaliado;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "avaliador_id", referencedColumnName = "id", nullable = false)
    private User avaliador;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "processo_id", referencedColumnName = "id", nullable = false)
    private Processo processo;

    // --- 15 Critérios Técnicos (Notas de 0 a 10) ---
    @Column(name = "qualidade_trabalho", nullable = false)
    private Integer qualidadetrabalho;

    @Column(name = "produtividade_trabalho", nullable = false)
    private Integer produtividadetrabalho;

    @Column(name = "conhecimento_profissional", nullable = false)
    private Integer conhecimentoprofissional;

    @Column(name = "eficiencia_trabalho", nullable = false)
    private Integer eficienciatrabalho;

    @Column(name = "meios_materiais", nullable = false)
    private Integer meiosmateriais;

    @Column(name = "planejamento", nullable = false)
    private Integer planejamento;

    @Column(name = "julgamento", nullable = false)
    private Integer julgamento;

    @Column(name = "responsabilidade", nullable = false)
    private Integer responsabilidade;

    @Column(name = "disciplina", nullable = false)
    private Integer disciplina;

    @Column(name = "apresentacao_pessoal", nullable = false)
    private Integer apresentacaopessoal;

    @Column(name = "lideranca", nullable = false)
    private Integer lideranca;

    @Column(name = "iniciativa", nullable = false)
    private Integer iniciativa;

    @Column(name = "adaptabilidade", nullable = false)
    private Integer adaptabilidade;

    @Column(name = "comunicacao_oral_escrita", nullable = false)
    private Integer comunicacaooralescrita;

    @Column(name = "relacionamento_ambiente_trabalho", nullable = false)
    private Integer relacionamentoambientetrabalho;

    // --- Pareceres Descritivos ---
    @Column(name = "justificativa", columnDefinition = "TEXT")
    private String justificativa;

    @Column(name = "conceito_moral", columnDefinition = "TEXT")
    private String conceitomoral;

    @Column(name = "conceito_profissional", columnDefinition = "TEXT")
    private String conceitoprofissional;

    public Avaliacao() {}


}
