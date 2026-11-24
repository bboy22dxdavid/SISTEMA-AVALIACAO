import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import {  finalize,  Subscription } from 'rxjs';
import { MaterialModule } from '../../shared/components/material-module/material-module';
import { ProcessoService } from '../../core/services/processo-service';
import { RankingService } from '../../core/services/ranking-service';
import { AvaliadoService } from '../../core/services/avaliado';
import { AuthService } from '../../core/services/auth';
import { Processo } from '../../shared/models/processo';
import { RankingResponse } from '../../shared/models/ranking-response';
import { BreakpointObserver } from '@angular/cdk/layout';

@Component({
  imports: [CommonModule, FormsModule, MaterialModule,  ],
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

  // Dentro da classe RankingComponent:
private readonly breakpointObserver = inject(BreakpointObserver);

  private subProcessos?: Subscription;
  private paginatorRef?: MatPaginator;

  @ViewChild(MatPaginator) set paginator(pager: MatPaginator) {
    if (pager) {
      this.paginatorRef = pager;
      this.dataSource.paginator = pager;
    }
  }

  processos: Processo[] = [];
  processoSelecionadoId: string = '';

  dataSource = new MatTableDataSource<RankingResponse>([]);
  opcoesPagina = [5, 10, 25];

  exibirNovoProcesso = false;
  novoAnoProcesso = new Date().getFullYear();


  termoBusca: string = '';
  carregando = false;

  colunasDesktop: string[] = ['posicao', 'foto', 'militar', 'secao', 'pontuacao', 'status', 'acao'];
  colunasMobile: string[] = ['posicao', 'militar', 'pontuacao', 'status', 'acao'];

  colunasExibidas: string[] = this.colunasDesktop;
  isMobile = false;

   get isCoordinator(): boolean {
    return this.authService.getUserRole() === 'ROLE_COORDINATOR';
  }

  get isAvaliador(): boolean {
    return this.authService.getUserRole() === 'ROLE_EVALUATOR';
  }

  get isRevisor(): boolean {
    return this.authService.getUserRole() === 'ROLE_REVIEWER';
  }

  ngOnInit(): void {
    this.configurarFiltroDataSource();

    this.breakpointObserver.observe(['(max-width: 768px)']).subscribe(result => {
      this.isMobile = result.matches;
      this.colunasExibidas = this.isMobile ? this.colunasMobile : this.colunasDesktop;
      this.cdr.detectChanges();
    });

    this.subProcessos = this.processoService.processos$.subscribe(lista => {
      this.processos = lista;
      if (lista.length > 0 && !this.processoSelecionadoId) {
        this.processoSelecionadoId = lista.at(0)!.id;
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
      this.snackBar.open('Informe um ano válido.', 'Fechar', { duration: 3500 });
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
      return data.nomeGuerra.toLowerCase().includes(termo) ||
        data.saram.toLowerCase().includes(termo) ||
        data.secao.toLowerCase().includes(termo);
    };
  }

  carregarDadosRanking(): void {
    if (!this.processoSelecionadoId) return;

    this.carregando = true;
    this.rankingService.obterRankingPorProcesso(this.processoSelecionadoId)
      .pipe(finalize(() => {
        this.carregando = false;
        this.cdr.detectChanges();
      }))
      .subscribe({
        next: (lista) => {
          this.dataSource = new MatTableDataSource<RankingResponse>(lista);
          this.configurarFiltroDataSource();
          if (this.paginatorRef) {
            this.dataSource.paginator = this.paginatorRef;
          }
          this.aplicarFiltro();
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Erro ao carregar ranking:', err);
          this.snackBar.open('Erro ao carregar classificação oficial.', 'Fechar', { duration: 4000 });
        }
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
