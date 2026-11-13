import { HttpClient } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { Router } from '@angular/router';
import { LoginRequest, LoginResponse } from '../../shared/models/auth';
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs/internal/operators/tap';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly apiUrl = 'http://localhost:8081/api/auth';
  private readonly storageKey = 'sang_auth_data';

    login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem(this.storageKey, JSON.stringify(response));
      })
    );
  }

   logout(): void {
    localStorage.removeItem(this.storageKey);
    this.router.navigate(['/login']);
  }

    getAuthData(): LoginResponse | null {
    const data = localStorage.getItem(this.storageKey);
    return data ? (JSON.parse(data) as LoginResponse) : null;
  }

  getToken(): string | null {
    return this.getAuthData()?.token ?? null;
  }

  getUserRole(): string | null {
    return this.getAuthData()?.role ?? null;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
