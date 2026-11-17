<<<<<<< HEAD
import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
=======
import { Component, inject, OnInit } from '@angular/core';
>>>>>>> 4bb604f4dd8ae54da2598aff25def9bf1c353a1b
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
<<<<<<< HEAD
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { catchError, finalize, of, Subscription } from 'rxjs';

@Component({
  imports: [CommonModule, FormsModule, MaterialModule ],
=======

@Component({
  imports: [CommonModule, FormsModule, MaterialModule],
>>>>>>> 4bb604f4dd8ae54da2598aff25def9bf1c353a1b
  selector: 'app-ranking',
  styleUrl: './ranking.scss',
  templateUrl: './ranking.html',
})
<<<<<<< HEAD
export class Ranking implements OnInit, OnDestroy {
  private readonly processoService = inject(ProcessoService);
  private readonly rankingService = inject(RankingService);
  private readonly avaliadoService = inject(AvaliadoService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly cdr = inject(ChangeDetectorRef);

  private  subProcessos?: Subscription;
  private  paginatorRef?: MatPaginator;

    @ViewChild(MatPaginator) set paginator(pager: MatPaginator) {
    if (pager) {
      this.paginatorRef = pager;
      this.dataSource.paginator = pager;
    }
  }

processos: Processo[] = [];
processoSelecionadoId: string = '';

    // Adicione estas propriedades no topo da classe RankingComponent:
  exibirNovoProcesso = false;
  novoAnoProcesso: number = new Date().getFullYear();

  dataSource = new MatTableDataSource<RankingResponse>([]);
  opcoesPagina: number[] = Array.of(5, 10, 25);
  termoBusca: string = '';

  carregando = false;

  colunasExibidas: string[] = ['posicao', 'foto', 'militar', 'secao', 'pontuacao', 'status', 'acao'];




  listaCompleta: RankingResponse[] = [];
  listaFiltrada: RankingResponse[] = [];



=======
export class Ranking implements OnInit{
   private processoService = inject(ProcessoService);
  private rankingService = inject(RankingService);
  private avaliadoService = inject(AvaliadoService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  processos: Processo[] = [];
  processoSelecionadoId: string = '';

  listaCompleta: RankingResponse[] = [];
  listaFiltrada: RankingResponse[] = [];
  termoBusca: string = '';
  carregando = false;

   colunasExibidas: string[] = ['posicao', 'foto', 'militar', 'secao', 'pontuacao', 'status', 'acao'];
>>>>>>> 4bb604f4dd8ae54da2598aff25def9bf1c353a1b


   get isAvaliador(): boolean {
    return this.authService.getUserRole() === 'ROLE_AVALIADOR';
  }

  get isRevisor(): boolean {
    return this.authService.getUserRole() === 'ROLE_REVISOR';
  }


  ngOnInit(): void {
<<<<<<< HEAD
    this.dataSource.filterPredicate = (data: RankingResponse, filter: string) => {
      const termo = filter.trim().toLowerCase();
      return data.nguerra.toLowerCase().includes(termo) ||
             data.saram.toLowerCase().includes(termo) ||
             data.secao.toLowerCase().includes(termo);
    };

    // 1. Registra o ouvinte reativo para a lista de processos
    this.subProcessos = this.processoService.processos$.subscribe(lista => {
      this.processos = lista;
      if (lista.length > 0 && !this.processoSelecionadoId) {
        this.processoSelecionadoId = lista.at(0)!.id;
        this.carregarDadosRanking();
      }
      this.cdr.detectChanges();
    });

    // 2. Dispara a carga inicial da API
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
          this.processoSelecionadoId = processoCriado.id; // Já seleciona o ano recém-criado
          this.snackBar.open(`Processo ${processoCriado.ano} criado com sucesso!`, 'OK', { duration: 3500 });
          this.carregarDadosRanking(); // Atualiza a tabela imediatamente para o novo ano
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
=======
     this.carregarProcessos();
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
>>>>>>> 4bb604f4dd8ae54da2598aff25def9bf1c353a1b
    if (!this.processoSelecionadoId) return;

    this.carregando = true;
    forkJoin({
<<<<<<< HEAD
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
        console.log('Dados recebidos -> Avaliados:', avaliados, 'Pontuados:', pontuados);

        const ativos = avaliados.filter(av => av.ativo !== false);
        const idsPontuados = new Set(pontuados.map(p => p.avaliadoId));

        const pendentes: RankingResponse[] = ativos
=======
      avaliados: this.avaliadoService.listarAtivos(),
      pontuados: this.rankingService.obterRankingPorProcesso(this.processoSelecionadoId)
    }).subscribe({
      next: ({ avaliados, pontuados }) => {
        // Mescla os avaliados já pontuados com os que ainda aguardam avaliação
        const idsPontuados = new Set(pontuados.map(p => p.avaliadoId));

        const pendentes: RankingResponse[] = avaliados
>>>>>>> 4bb604f4dd8ae54da2598aff25def9bf1c353a1b
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

<<<<<<< HEAD
        const listaFinal = [...pontuados, ...pendentes];

        this.dataSource = new MatTableDataSource<RankingResponse>(listaFinal);
        this.configurarFiltroDataSource();

        if (this.paginatorRef) {
          this.dataSource.paginator = this.paginatorRef;
        }

        this.aplicarFiltro();
        this.cdr.detectChanges();
      });
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
=======
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
>>>>>>> 4bb604f4dd8ae54da2598aff25def9bf1c353a1b
      queryParams: { processoId: this.processoSelecionadoId }
    });
  }

  irParaNivelamento(avaliacaoId: string): void {
<<<<<<< HEAD
    void this.router.navigate(['/nivelamento', avaliacaoId]);
=======
    this.router.navigate(['/nivelamento', avaliacaoId]);
>>>>>>> 4bb604f4dd8ae54da2598aff25def9bf1c353a1b
  }
}
