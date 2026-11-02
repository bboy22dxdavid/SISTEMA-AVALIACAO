package io.github.bboy22dxdavid.sang_backEnd.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_nivelamentos")
public class Nivelamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento 1:1 estrito com a Avaliação Original
    @OneToOne
    @JoinColumn(name = "avaliacao_id", nullable = false, unique = true)
    private Avaliacao avaliacao;

    // Usuário do Comando que realizou a reavaliação
    @ManyToOne
    @JoinColumn(name = "comando_id", nullable = false)
    private User comando;

    // --- Os 15 Critérios Técnicos Reavaliados pelo Comando (Notas 0 a 10) ---
    @Column(name = "qualidade_trabalho", nullable = false)
    private Integer qualidadeTrabalho;

    @Column(name = "produtividade_trabalho", nullable = false)
    private Integer productivityTrabalho; // Mapeado conforme parâmetro

    @Column(name = "conhecimento_profissional", nullable = false)
    private Integer conhecimentoProfissional;

    @Column(name = "eficiencia_trabalho", nullable = false)
    private Integer eficienciaTrabalho;

    @Column(name = "meios_materiais", nullable = false)
    private Integer meiosMateriais;

    @Column(nullable = false)
    private Integer planejamento;

    @Column(nullable = false)
    private Integer julgamento;

    @Column(nullable = false)
    private Integer responsabilidade;

    @Column(nullable = false)
    private Integer disciplina;

    @Column(name = "apresentacao_pessoal", nullable = false)
    private Integer apresentacaoPessoal;

    @Column(nullable = false)
    private Integer lideranca;

    @Column(nullable = false)
    private Integer iniciativa;

    @Column(nullable = false)
    private Integer adaptabilidade;

    @Column(name = "comunicacao_oral_escrita", nullable = false)
    private Integer comunicacaoOralEscrita;

    @Column(name = "relacionamento_ambiente_trabalho", nullable = false)
    private Integer relacionamentoAmbienteTrabalho;

    // --- Justificativa OBRIGATÓRIA para alteração de nota ---
    @Column(nullable = false, columnDefinition = "TEXT")
    private String justificativa;

    @Column(name = "conceito_moral", columnDefinition = "TEXT")
    private String conceitoMoral;

    @Column(name = "conceito_profissional", columnDefinition = "TEXT")
    private String conceitoProfissional;

    // Construtor Padrão (Obrigatório para o Hibernate)
    public Nivelamento() {}


}
