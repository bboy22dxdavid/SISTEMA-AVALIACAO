package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "tb_nivelamentos")
public class Nivelamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "avaliacao_id", referencedColumnName = "id", nullable = false, unique = true)
    private Avaliacao avaliacao;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "revisor_id", referencedColumnName = "id", nullable = false)
    private User revisor;

    // --- 15 Critérios Técnicos Reavaliados (Notas de 0 a 10) ---
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

    // --- Pareceres Descritivos (Justificativa Obrigatória) ---
    @Column(name = "justificativa", nullable = false, columnDefinition = "TEXT")
    private String justificativa;

    @Column(name = "conceito_moral", columnDefinition = "TEXT")
    private String conceitomoral;

    @Column(name = "conceito_profissional", columnDefinition = "TEXT")
    private String conceitoprofissional;

    //construtor
    public Nivelamento() {}


}
