import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { catchError, finalize, forkJoin, of, Subscription } from 'rxjs';
import { MaterialModule } from '../../shared/components/material-module/material-module';
import { ProcessoService } from '../../core/services/processo-service';
import { RankingService } from '../../core/services/ranking-service';
import { AvaliadoService } from '../../core/services/avaliado';
import { AuthService } from '../../core/services/auth';
import { Processo } from '../../shared/models/processo';
import { RankingResponse } from '../../shared/models/ranking-response';

@Component({
  imports: [CommonModule, FormsModule, MaterialModule],
  selector: 'app-ranking',
  styleUrl: './ranking.scss',
  templateUrl: './ranking.html',
})
export class Ranking implements OnInit, OnDestroy {
  private readonly processoService = inject(ProcessoService);
  private readonly rankingService = inject(RankingService);
  private readonly avaliadoService = inject(AvaliadoService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly cdr = inject(ChangeDetectorRef);

  private subProcessos?: Subscription;
  private paginatorRef?: MatPaginator;

  @ViewChild(MatPaginator) set paginator(pager: MatPaginator) {
    if (pager) {
      this.paginatorRef = pager;
      this.dataSource.paginator = pager;
    }
  }

  processos: Processo[] = [];
  processoSelecionadoId = '';
  exibirNovoProcesso = false;
  novoAnoProcesso = new Date().getFullYear();
  dataSource = new MatTableDataSource<RankingResponse>([]);
  opcoesPagina = [5, 10, 25];
  termoBusca = '';
  carregando = false;

  colunasExibidas = ['posicao', 'foto', 'militar', 'secao', 'pontuacao', 'status', 'acao'];

  get isAvaliador(): boolean {
    return this.authService.getUserRole() === 'ROLE_AVALIADOR';
  }

  get isRevisor(): boolean {
    return this.authService.getUserRole() === 'ROLE_REVISOR';
  }

  ngOnInit(): void {
    this.configurarFiltroDataSource();

    this.subProcessos = this.processoService.processos$.subscribe(lista => {
      this.processos = lista;
      if (lista.length > 0 && !this.processoSelecionadoId) {
        this.processoSelecionadoId = lista[0].id;
        this.carregarDadosRanking();
      }
      this.cdr.detectChanges();
    });

    this.processoService.listarTodos().subscribe({
      error: () => this.snackBar.open('Erro ao buscar processos anuais.', 'Fechar', { duration: 4000 })
    });
  }

  ngOnDestroy(): void {
    this.subProcessos?.unsubscribe();
  }

  criarNovoProcesso(): void {
    if (!this.novoAnoProcesso || this.novoAnoProcesso < 2000) {
      this.snackBar.open('Informe um ano válido (ex: 2026).', 'Fechar', { duration: 3500 });
      return;
    }

    this.carregando = true;
    this.processoService.criar(this.novoAnoProcesso)
      .pipe(finalize(() => {
        this.carregando = false;
        this.cdr.detectChanges();
      }))
      .subscribe({
        next: (processoCriado) => {
          this.exibirNovoProcesso = false;
          this.processoSelecionadoId = processoCriado.id;
          this.snackBar.open(`Processo ${processoCriado.ano} criado com sucesso!`, 'OK', { duration: 3500 });
          this.carregarDadosRanking();
        },
        error: (err) => {
          const msg = err.error?.mensagem || 'Erro ao criar processo. Verifique se o ano já existe.';
          this.snackBar.open(msg, 'Fechar', { duration: 4000 });
        }
      });
  }

  private configurarFiltroDataSource(): void {
    this.dataSource.filterPredicate = (data: RankingResponse, filter: string) => {
      const termo = filter.trim().toLowerCase();
      return data.nguerra.toLowerCase().includes(termo) ||
        data.saram.toLowerCase().includes(termo) ||
        data.secao.toLowerCase().includes(termo);
    };
  }

  carregarDadosRanking(): void {
    if (!this.processoSelecionadoId) return;

    this.carregando = true;
    forkJoin({
      avaliados: this.avaliadoService.listarTodos().pipe(
        catchError(err => {
          console.error('Erro ao buscar avaliados:', err);
          return of([]);
        })
      ),
      pontuados: this.rankingService.obterRankingPorProcesso(this.processoSelecionadoId).pipe(
        catchError(err => {
          console.error('Erro ao buscar ranking:', err);
          return of([]);
        })
      )
    })
      .pipe(finalize(() => {
        this.carregando = false;
        this.cdr.detectChanges();
      }))
      .subscribe(({ avaliados, pontuados }) => {
        const ativos = avaliados.filter(av => av.ativo !== false);
        const idsPontuados = new Set(pontuados.map(p => p.avaliadoId));

        const pendentes: RankingResponse[] = ativos
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

        this.dataSource.data = [...pontuados, ...pendentes];
        this.aplicarFiltro();
        this.cdr.detectChanges();
      });
  }

  aplicarFiltro(): void {
    this.dataSource.filter = this.termoBusca.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  calcularPosicaoReal(indexNaPagina: number): number {
    const pageIndex = this.dataSource.paginator?.pageIndex ?? 0;
    const pageSize = this.dataSource.paginator?.pageSize ?? 10;
    return (pageIndex * pageSize) + indexNaPagina + 1;
  }

  irParaAvaliacao(avaliadoId: string): void {
    void this.router.navigate(['/avaliacao', avaliadoId], {
      queryParams: { processoId: this.processoSelecionadoId }
    });
  }

  irParaNivelamento(avaliacaoId: string): void {
    void this.router.navigate(['/nivelamento', avaliacaoId]);
  }
}
