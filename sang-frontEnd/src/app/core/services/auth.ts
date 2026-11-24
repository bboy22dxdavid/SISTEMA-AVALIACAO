import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { LoginRequest, LoginResponse } from '../../shared/models/auth';
import { Observable } from 'rxjs/internal/Observable';
import { tap } from 'rxjs/internal/operators/tap';
import { UserRole } from '../models/auth';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly apiUrl = 'http://localhost:8081/api/auth';
  private readonly storageKey = 'sang_session';

    private  currentUserSubject = new BehaviorSubject<LoginResponse | null>(this.carregarSessaoLocal());
  currentUser$ = this.currentUserSubject.asObservable();

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem(this.storageKey, JSON.stringify(response));
        this.currentUserSubject.next(response);
      })
    );
  }

   getAuthData(): LoginResponse | null {
    return this.getCurrentUser();
  }

  logout(): void {
    localStorage.removeItem(this.storageKey);
    this.currentUserSubject.next(null);
    void this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this.currentUserSubject.value?.token ?? null;
  }

  getCurrentUser(): LoginResponse | null {
    return this.currentUserSubject.value;
  }

  getUserRole(): UserRole | null {
    return this.currentUserSubject.value?.role ?? null;
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expirado = payload.exp * 1000 < Date.now();
      if (expirado) {
        this.logout();
        return false;
      }
      return true;
    } catch {
      return false;
    }
  }

  hasAnyRole(roles: UserRole[]): boolean {
    const currentRole = this.getUserRole();
    return currentRole !== null && roles.includes(currentRole);
  }

  private carregarSessaoLocal(): LoginResponse | null {
    const raw = localStorage.getItem(this.storageKey);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as LoginResponse;
    } catch {
      return null;
    }
  }
}
