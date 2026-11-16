import { HttpClient } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Avaliado } from '../../shared/models/avaliado';

@Injectable({
  providedIn: 'root'
})
export class AvaliadoService  {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/avaliados';

   salvar(avaliado: Avaliado): Observable<Avaliado> {
    return this.http.post<Avaliado>(this.apiUrl, avaliado);
  }

   listarTodos(): Observable<Avaliado[]> {
    return this.http.get<Avaliado[]>(this.apiUrl);
  }

   listarAtivos(): Observable<Avaliado[]> {
    return this.http.get<Avaliado[]>(`${this.apiUrl}/ativos`);
  }

   buscarPorId(id: string): Observable<Avaliado> {
    return this.http.get<Avaliado>(`${this.apiUrl}/${id}`);
  }

  buscarPorSaram(saram: string): Observable<Avaliado> {
    return this.http.get<Avaliado>(`${this.apiUrl}/saram/${saram}`);
  }
}
