import { HttpClient } from '@angular/common/http';
<<<<<<< HEAD
import { inject, Injectable } from '@angular/core';
import { Processo } from '../../shared/models/processo';
import { Observable } from 'rxjs/internal/Observable';
import { BehaviorSubject, tap } from 'rxjs';
=======
import { inject, Injectable, Service } from '@angular/core';
import { Processo } from '../../shared/models/processo';
import { Observable } from 'rxjs/internal/Observable';
>>>>>>> 4bb604f4dd8ae54da2598aff25def9bf1c353a1b

@Injectable({
  providedIn: 'root'
})
export class ProcessoService {
<<<<<<< HEAD
   private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/processos';

  // Estado reativo que notifica a interface sempre que houver alteração
  private readonly processosSubject = new BehaviorSubject<Processo[]>([]);
  processos$ = this.processosSubject.asObservable();

  listarTodos(): Observable<Processo[]> {
    return this.http.get<Processo[]>(this.apiUrl).pipe(
      tap(lista => {
        const ordenados = [...lista].sort((a, b) => b.ano - a.ano);
        this.processosSubject.next(ordenados);
      })
    );
  }

  criar(ano: number): Observable<Processo> {
    return this.http.post<Processo>(this.apiUrl, { ano }).pipe(
      tap(novoProcesso => {
        // Atualiza a lista reativa imediatamente sem precisar fazer outro GET
        const listaAtual = this.processosSubject.value;
        const atualizada = [...listaAtual, novoProcesso].sort((a, b) => b.ano - a.ano);
        this.processosSubject.next(atualizada);
      })
    );
=======
   private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/processos';

  listarTodos(): Observable<Processo[]> {
    return this.http.get<Processo[]>(this.apiUrl);
  }

  criar(ano: number): Observable<Processo> {
    return this.http.post<Processo>(this.apiUrl, { ano });
>>>>>>> 4bb604f4dd8ae54da2598aff25def9bf1c353a1b
  }
}
