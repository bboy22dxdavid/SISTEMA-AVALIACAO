import { Component, inject, OnInit } from '@angular/core';
import { MaterialModule } from '../../shared/components/material-module/material-module';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AvaliadoService } from '../../core/services/avaliado';
import { AvaliacaoService } from '../../core/services/avaliacao-service';
import { NivelamentoService } from '../../core/services/nivelamento-service';
import { AuthService } from '../../core/services/auth';
import { Avaliado } from '../../shared/models/avaliado';

@Component({
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MaterialModule],
  selector: 'app-avaliacao-nivelamento',
  styleUrl: './avaliacao-nivelamento.scss',
  templateUrl: './avaliacao-nivelamento.html',
})
export class AvaliacaoNivelamento implements OnInit{
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly avaliadoService = inject(AvaliadoService);
  private readonly avaliacaoService = inject(AvaliacaoService);
  private readonly nivelamentoService = inject(NivelamentoService);
  private readonly authService = inject(AuthService);

  modo: 'AVALIACAO' | 'NIVELAMENTO' = 'AVALIACAO';
  militarAvaliado: Avaliado | null = null;
  avaliadoId: string = '';
  avaliacaoId: string = '';
  processoId: string = '';
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
    qualidadetrabalho: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    produtividadetrabalho: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    conhecimentoprofissional: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    eficienciatrabalho: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    meiosmateriais: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    planejamento: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    julgamento: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    responsabilidade: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    disciplina: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    apresentacaopessoal: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    lideranca: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    iniciativa: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    adaptabilidade: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    comunicacaooralescrita: [0, [Validators.required, Validators.min(0), Validators.max(10)]],
    relacionamentoambientetrabalho: [0, [Validators.required, Validators.min(0), Validators.max(10)]],

    justificativa: [''],
    conceitomoral: ['', [Validators.required]],
    conceitoprofissional: ['', [Validators.required]]
  });

   get pontuacaoCalculada(): number {
    const valores = this.form.getRawValue();
    return this.criterios.reduce((acc, item) => {
      const nota = Number((valores as any)[item.campo]) || 0;
      return acc + nota;
    }, 0);
  }


  ngOnInit(): void {
    this.modo = this.route.snapshot.data['modo'] || 'AVALIACAO';

    if (this.modo === 'AVALIACAO') {
      this.avaliadoId = this.route.snapshot.paramMap.get('avaliadoId') || '';
      this.processoId = this.route.snapshot.queryParamMap.get('processoId') || '';
      this.carregarDadosAvaliado();
    } else {
      this.avaliacaoId = this.route.snapshot.paramMap.get('avaliacaoId') || '';
      this.form.get('justificativa')?.setValidators([Validators.required]);
      this.form.get('justificativa')?.updateValueAndValidity();
      this.carregarDadosAvaliacaoOriginal();
    }
  }

  private carregarDadosAvaliado(): void {
    if (!this.avaliadoId) return;
    this.carregando = true;
    this.avaliadoService.buscarPorId(this.avaliadoId).subscribe({
      next: (dados) => {
        this.militarAvaliado = dados;
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
        this.snackBar.open('Erro ao carregar ficha do militar.', 'Fechar', { duration: 4000 });
      }
    });
  }

   private carregarDadosAvaliacaoOriginal(): void {
    if (!this.avaliacaoId) return;
    this.carregando = true;
    this.avaliacaoService.buscarPorId(this.avaliacaoId).subscribe({
      next: (av) => {
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
          justificativa: '' // Limpa para o Comando preencher a justificativa da revisão
        });
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
        this.snackBar.open('Erro ao carregar avaliação original.', 'Fechar', { duration: 4000 });
      }
    });
  }

  salvar(): void {
    if (this.form.invalid) return;

    const usuarioLogadoId = this.authService.getAuthData()?.userId;
    if (!usuarioLogadoId) {
      this.snackBar.open('Sessão expirada. Faça login novamente.', 'Fechar', { duration: 4000 });
      return;
    }

    this.carregando = true;
    const notasEConceitos = this.form.getRawValue();

    if (this.modo === 'AVALIACAO') {
      const payload = {
        avaliado: { id: this.avaliadoId },
        avaliador: { id: usuarioLogadoId },
        processo: { id: this.processoId },
        ...notasEConceitos
      };

      this.avaliacaoService.criar(payload).subscribe({
        next: () => this.finalizar('Avaliação registrada com sucesso!'),
        error: (err) => this.tratarErro(err)
      });
    } else {
      const payload = {
        avaliacao: { id: this.avaliacaoId },
        revisor: { id: usuarioLogadoId },
        ...notasEConceitos
      };

      this.nivelamentoService.criar(payload).subscribe({
        next: () => this.finalizar('Nivelamento do Comando registrado com sucesso!'),
        error: (err) => this.tratarErro(err)
      });
    }
  }

  private finalizar(msg: string): void {
    this.carregando = false;
    this.snackBar.open(msg, 'OK', { duration: 4000 });
    this.router.navigate(['/ranking']);
  }

  private tratarErro(err: any): void {
    this.carregando = false;
    const msg = err.error?.mensagem || 'Erro ao salvar ficha. Verifique os campos.';
    this.snackBar.open(msg, 'Fechar', { duration: 5000 });
  }
}
