import { HttpClient } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { RankingResponse } from '../../shared/models/ranking-response';

@Injectable({
  providedIn: 'root'
})
export class RankingService {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/ranking';

  obterRankingPorProcesso(processoId: string): Observable<RankingResponse[]> {
    return this.http.get<RankingResponse[]>(`${this.apiUrl}/processo/${processoId}`);
  }

}
