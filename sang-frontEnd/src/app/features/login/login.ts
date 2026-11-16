import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MaterialModule } from '../../shared/components/material-module/material-module';



@Component({
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MaterialModule],
  selector: 'app-login',
  styleUrl: './login.scss',
  templateUrl: './login.html',
})
export class Login {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  carregando = false;
  esconderSenha = true;

  loginForm = this.fb.nonNullable.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });

  entrar(): void {
    if (this.loginForm.invalid) return;

    this.carregando = true;
    this.authService.login(this.loginForm.getRawValue()).subscribe({
      next: () => {
        this.carregando = false;
        this.router.navigate(['/ranking']);
      },
      error: (err) => {
        this.carregando = false;
        const msg = err.error?.mensagem || 'Usuário ou senha inválidos.';
        this.snackBar.open(msg, 'Fechar', { duration: 4000 });
      }
    });
  }
}
