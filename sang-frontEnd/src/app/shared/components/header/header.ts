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

    get roleLabel(): string {
    switch (this.authService.getUserRole()) {
      case 'ROLE_COORDINATOR':
        return 'Coordenador';
      case 'ROLE_EVALUATOR':
        return 'Avaliador';
      case 'ROLE_REVIEWER':
        return 'Revisor';
      default:
        return '';
    }
  }

  sair(): void {
    this.authService.logout();
  }

  get currentUser() {
    return this.authService.getCurrentUser();
  }

  get isCoordinator(): boolean {
    return this.authService.getUserRole() === 'ROLE_COORDINATOR';
  }

}
