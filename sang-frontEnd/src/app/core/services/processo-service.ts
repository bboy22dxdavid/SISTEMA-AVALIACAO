import { HttpClient } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { Processo } from '../../shared/models/processo';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class ProcessoService {
   private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/processos';

  listarTodos(): Observable<Processo[]> {
    return this.http.get<Processo[]>(this.apiUrl);
  }

  criar(ano: number): Observable<Processo> {
    return this.http.post<Processo>(this.apiUrl, { ano });
  }
}
