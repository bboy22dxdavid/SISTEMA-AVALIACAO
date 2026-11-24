import { Component, inject } from '@angular/core';
import { MaterialModule } from '../material-module/material-module';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth';

@Component({
  imports: [CommonModule, RouterLink, RouterLinkActive, MaterialModule],
  selector: 'app-header',
  styleUrl: './header.scss',
  templateUrl: './header.html',
})
export class Header {
   authService = inject(AuthService);

   get labelPerfil(): string {
    const role = this.authService.getUserRole();
    switch (role) {
      case 'ROLE_EVALUATOR': return 'Avaliador';
      case 'ROLE_REVIEWER': return 'Revisor (Comando)';
      case 'ROLE_COORDINATOR': return 'Administrador';
      default: return 'Usuário';
    }
  }

  sair(): void {
    this.authService.logout();
  }
}
