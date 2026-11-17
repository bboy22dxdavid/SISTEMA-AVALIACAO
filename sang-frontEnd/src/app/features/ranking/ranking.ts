import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { MaterialModule } from '../../shared/components/material-module/material-module';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProcessoService } from '../../core/services/processo-service';
import { RankingService } from '../../core/services/ranking-service';
import { AvaliadoService } from '../../core/services/avaliado';
import { AuthService } from '../../core/services/auth';
import { Router } from '@angular/router';
import { Processo } from '../../shared/models/processo';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';
import { RankingResponse } from '../../shared/models/ranking-response';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  imports: [CommonModule, FormsModule, MaterialModule],
  selector: 'app-ranking',
  styleUrl: './ranking.scss',
  templateUrl: './ranking.html',
})
export class Ranking implements OnInit{
  private processoService = inject(ProcessoService);
  private rankingService = inject(RankingService);
  private avaliadoService = inject(AvaliadoService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

    // Adicione estas propriedades no topo da classe RankingComponent:
  exibirNovoProcesso = false;
  novoAnoProcesso: number = new Date().getFullYear();

  processos: Processo[] = [];
  processoSelecionadoId: string = '';

  listaCompleta: RankingResponse[] = [];
  listaFiltrada: RankingResponse[] = [];
  termoBusca: string = '';
  carregando = false;

   colunasExibidas: string[] = ['posicao', 'foto', 'militar', 'secao', 'pontuacao', 'status', 'acao'];


   get isAvaliador(): boolean {
    return this.authService.getUserRole() === 'ROLE_AVALIADOR';
  }

  get isRevisor(): boolean {
    return this.authService.getUserRole() === 'ROLE_REVISOR';
  }


  ngOnInit(): void {
     this.carregarProcessos();
  }

  // Adicione este método na classe RankingComponent:
  criarNovoProcesso(): void {
    if (!this.novoAnoProcesso || this.novoAnoProcesso < 2000) {
      this.snackBar.open('Informe um ano válido (ex: 2026).', 'Fechar', { duration: 3500 });
      return;
    }

    this.carregando = true;
    this.processoService.criar(this.novoAnoProcesso).subscribe({
      next: (processoCriado) => {
        this.exibirNovoProcesso = false;
        this.snackBar.open(`Processo ${processoCriado.ano} criado com sucesso!`, 'OK', { duration: 3500 });
        this.carregarProcessos(); // Recarrega a lista e já seleciona o novo ano
      },
      error: (err) => {
        this.carregando = false;
        const msg = err.error?.mensagem || 'Erro ao criar processo. Verifique se o ano já existe.';
        this.snackBar.open(msg, 'Fechar', { duration: 4000 });
      }
    });
  }

   carregarProcessos(): void {
    this.carregando = true;
    this.processoService.listarTodos().subscribe({
      next: (dados) => {
        // Ordena do ano mais recente para o mais antigo
        this.processos = dados.sort((a, b) => b.ano - a.ano);
        if (this.processos.length > 0) {
          this.processoSelecionadoId = this.processos[0].id;
          this.carregarDadosRanking();
        } else {
          this.carregando = false;
        }
      },
      error: () => {
        this.carregando = false;
        this.snackBar.open('Erro ao carregar processos anuais.', 'Fechar', { duration: 4000 });
      }
    });
  }

  carregarDadosRanking(): void {
    if (!this.processoSelecionadoId) return;

    this.carregando = true;
    forkJoin({
      avaliados: this.avaliadoService.listarAtivos(),
      pontuados: this.rankingService.obterRankingPorProcesso(this.processoSelecionadoId)
    }).subscribe({
      next: ({ avaliados, pontuados }) => {
        // Mescla os avaliados já pontuados com os que ainda aguardam avaliação
        const idsPontuados = new Set(pontuados.map(p => p.avaliadoId));

        const pendentes: RankingResponse[] = avaliados
          .filter(av => av.id && !idsPontuados.has(av.id))
          .map(av => ({
            avaliadoId: av.id!,
            avaliacaoId: null,
            saram: av.saram,
            posto: av.posto,
            nguerra: av.nguerra,
            secao: av.secao,
            antiguidade: av.antiguidade,
            pontuacaoTotal: 0,
            nivelado: false
          }));

        this.listaCompleta = [...pontuados, ...pendentes];
        this.aplicarFiltro();
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
        this.snackBar.open('Erro ao carregar dados do ranking.', 'Fechar', { duration: 4000 });
      }
    });
  }

  aplicarFiltro(): void {
    const termo = this.termoBusca.toLowerCase().trim();
    if (!termo) {
      this.listaFiltrada = [...this.listaCompleta];
      return;
    }

    this.listaFiltrada = this.listaCompleta.filter(item =>
      item.nguerra.toLowerCase().includes(termo) ||
      item.saram.toLowerCase().includes(termo) ||
      item.secao.toLowerCase().includes(termo)
    );
  }

  irParaAvaliacao(avaliadoId: string): void {
    this.router.navigate(['/avaliacao', avaliadoId], {
      queryParams: { processoId: this.processoSelecionadoId }
    });
  }

  irParaNivelamento(avaliacaoId: string): void {
    this.router.navigate(['/nivelamento', avaliacaoId]);
  }
}
