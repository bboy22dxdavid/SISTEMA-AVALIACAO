import { HttpClient } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { AvaliacaoPayload, AvaliacaoResponse } from '../../shared/models/avaliacao-payload';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class AvaliacaoService {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/avaliacao';

    criar(payload: AvaliacaoPayload): Observable<AvaliacaoResponse> {
    return this.http.post<AvaliacaoResponse>(this.apiUrl, payload);
  }

  buscarPorId(id: string): Observable<AvaliacaoResponse> {
    return this.http.get<AvaliacaoResponse>(`${this.apiUrl}/${id}`);
  }
}
