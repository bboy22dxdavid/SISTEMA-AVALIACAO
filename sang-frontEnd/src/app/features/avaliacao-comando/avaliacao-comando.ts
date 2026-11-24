import { Component, inject, OnInit } from '@angular/core';
import { MaterialModule } from '../../shared/components/material-module/material-module';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth';
import { NivelamentoService } from '../../core/services/nivelamento-service';
import { AvaliacaoService } from '../../core/services/avaliacao-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AvaliacaoResponse } from '../../shared/models/avaliacao-payload';
import { Avaliado } from '../../shared/models/avaliado';

@Component({
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MaterialModule],
  selector: 'app-avaliacao-comando',
  styleUrl: './avaliacao-comando.scss',
  templateUrl: './avaliacao-comando.html',
})
export class AvaliacaoComando implements OnInit {
 private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly avaliacaoService = inject(AvaliacaoService);
  private readonly nivelamentoService = inject(NivelamentoService);
  private readonly authService = inject(AuthService);

  avaliacaoId: string = '';
  avaliacaoOriginal: AvaliacaoResponse | null = null;
  militarAvaliado: Avaliado | null = null;
  carregando = false;

  criterios = [
    { campo: 'qualidadetrabalho', titulo: '1. Qualidade do Trabalho', descricao: 'Precisão, esmero e confiabilidade nas tarefas executadas.' },
    { campo: 'produtividadetrabalho', titulo: '2. Produtividade no Trabalho', descricao: 'Volume de trabalho produzido dentro dos prazos estabelecidos.' },
    { campo: 'conhecimentoprofissional', titulo: '3. Conhecimento Profissional', descricao: 'Domínio técnico e regulamentar da sua especialidade.' },
    { campo: 'eficienciatrabalho', titulo: '4. Eficiência no Trabalho', descricao: 'Otimização de recursos e tempo na resolução de problemas.' },
    { campo: 'meiosmateriais', titulo: '5. Zelo pelos Meios Materiais', descricao: 'Cuidado e manutenção de equipamentos e instalações.' },
    { campo: 'planejamento', titulo: '6. Planejamento', descricao: 'Capacidade de organizar etapas e prever necessidades.' },
    { campo: 'julgamento', titulo: '7. Julgamento', descricao: 'Sensatez e coerência na análise de situações e decisões.' },
    { campo: 'responsabilidade', titulo: '8. Responsabilidade', descricao: 'Comprometimento com deveres e consequências dos atos.' },
    { campo: 'disciplina', titulo: '9. Disciplina', descricao: 'Observância rigorosa às normas, regulamentos e hierarquia.' },
    { campo: 'apresentacaopessoal', titulo: '10. Apresentação Pessoal', descricao: 'Postura militar, uso correto do uniforme e higiene.' },
    { campo: 'lideranca', titulo: '11. Liderança', descricao: 'Capacidade de conduzir, motivar e dar exemplo aos pares/subordinados.' },
    { campo: 'iniciativa', titulo: '12. Iniciativa', descricao: 'Proatividade para agir e propor melhorias sem esperar ordens.' },
    { campo: 'adaptabilidade', titulo: '13. Adaptabilidade', descricao: 'Flexibilidade diante de mudanças de rotina ou pressão operacional.' },
    { campo: 'comunicacaooralescrita', titulo: '14. Comunicação Oral e Escrita', descricao: 'Clareza, objetividade e correção gramatical na expressão.' },
    { campo: 'relacionamentoambientetrabalho', titulo: '15. Relacionamento no Ambiente de Trabalho', descricao: 'Espírito de corpo, cooperação e respeito mútuo.' }
  ];

  form = this.fb.nonNullable.group({
    qualidadetrabalho: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    produtividadetrabalho: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    conhecimentoprofissional: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    eficienciatrabalho: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    meiosmateriais: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    planejamento: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    julgamento: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    responsabilidade: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    disciplina: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    apresentacaopessoal: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    lideranca: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    iniciativa: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    adaptabilidade: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    comunicacaooralescrita: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    relacionamentoambientetrabalho: [0, [Validators.required, Validators.min(1), Validators.max(5)]],
    justificativa: ['', [Validators.required, Validators.minLength(5)]],
    conceitomoral: ['', [Validators.required]],
    conceitoprofissional: ['', [Validators.required]]
  });

  get pontuacaoOriginal(): number {
    if (!this.avaliacaoOriginal) return 0;
    return this.criterios.reduce((acc, item) => {
      return acc + (Number((this.avaliacaoOriginal as any)[item.campo]) || 0);
    }, 0);
  }

  get pontuacaoRevisada(): number {
    const valores = this.form.getRawValue();
    return this.criterios.reduce((acc, item) => {
      return acc + (Number((valores as any)[item.campo]) || 0);
    }, 0);
  }

  ngOnInit(): void {
    this.avaliacaoId = this.route.snapshot.paramMap.get('avaliacaoId') || '';
    this.carregarAvaliacaoOriginal();
  }

  private carregarAvaliacaoOriginal(): void {
    if (!this.avaliacaoId) return;

    this.carregando = true;
    this.avaliacaoService.buscarPorId(this.avaliacaoId).subscribe({
      next: (av) => {
        this.avaliacaoOriginal = av;
        this.militarAvaliado = av.avaliado;
        this.form.patchValue({
          qualidadetrabalho: av.qualidadetrabalho,
          produtividadetrabalho: av.produtividadetrabalho,
          conhecimentoprofissional: av.conhecimentoprofissional,
          eficienciatrabalho: av.eficienciatrabalho,
          meiosmateriais: av.meiosmateriais,
          planejamento: av.planejamento,
          julgamento: av.julgamento,
          responsabilidade: av.responsabilidade,
          disciplina: av.disciplina,
          apresentacaopessoal: av.apresentacaopessoal,
          lideranca: av.lideranca,
          iniciativa: av.iniciativa,
          adaptabilidade: av.adaptabilidade,
          comunicacaooralescrita: av.comunicacaooralescrita,
          relacionamentoambientetrabalho: av.relacionamentoambientetrabalho,
          conceitomoral: av.conceitomoral,
          conceitoprofissional: av.conceitoprofissional,
          justificativa: ''
        });
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
        this.snackBar.open('Erro ao carregar dados da avaliação original.', 'Fechar', { duration: 4000 });
      }
    });
  }

  obterNotaOriginal(campo: string): number {
    return this.avaliacaoOriginal ? Number((this.avaliacaoOriginal as any)[campo]) : 0;
  }

  salvarNivelamento(): void {
    if (this.form.invalid) return;

    const revisorId = this.authService.getAuthData()?.userId;
    if (!revisorId) {
      this.snackBar.open('Sessão expirada. Faça login novamente.', 'Fechar', { duration: 4000 });
      return;
    }

    this.carregando = true;
    const dadosRevisados = this.form.getRawValue();

    const payload = {
      avaliacao: { id: this.avaliacaoId },
      revisor: { id: revisorId },
      ...dadosRevisados
    };

    this.nivelamentoService.criar(payload).subscribe({
      next: () => {
        this.carregando = false;
        this.snackBar.open('Nivelamento do Comando registrado com sucesso!', 'OK', { duration: 4000 });
        void this.router.navigate(['/ranking']);
      },
      error: (err) => {
        this.carregando = false;
        const msg = err.error?.mensagem || 'Erro ao registrar nivelamento.';
        this.snackBar.open(msg, 'Fechar', { duration: 5000 });
      }
    });
  }
}
