import { inject, Injectable, Service } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { NivelamentoPayload } from '../../shared/models/nivelamento-payload';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NivelamentoService {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/nivelamento';

  criar(payload: NivelamentoPayload): Observable<any> {
    return this.http.post<any>(this.apiUrl, payload);
  }
}
