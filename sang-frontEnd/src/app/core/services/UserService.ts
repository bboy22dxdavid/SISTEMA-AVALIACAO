import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { UserDTO, UserRequest } from '../../shared/models/user';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/users';

   cadastrar(usuario: UserRequest, excecaoGraduado: boolean = false): Observable<UserDTO> {
    const params = new HttpParams().set('excecaoGraduado', excecaoGraduado.toString());
    return this.http.post<UserDTO>(this.apiUrl, usuario, { params });
  }


  listarTodos(): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(this.apiUrl);
  }

  buscarPorId(id: string): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/${id}`);
  }

  deletar(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
