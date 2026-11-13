import { Component, inject } from '@angular/core';
import { MaterialModule } from '../../shared/components/material-module/material-module';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../../core/services/UserService';

@Component({
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MaterialModule],
  selector: 'app-usuario-cadastro',
  styleUrl: './usuario-cadastro.scss',
  templateUrl: './usuario-cadastro.html',
})
export class UsuarioCadastro {
  private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  carregando = false;

    postos = [
    { cod: 'CEL', nome: 'Coronel', tipo: 'REVISOR' },
    { cod: 'TCEL', nome: 'Tenente-Coronel', tipo: 'REVISOR' },
    { cod: 'MAJ', nome: 'Major', tipo: 'REVISOR' },
    { cod: 'CAP', nome: 'Capitão', tipo: 'AVALIADOR' },
    { cod: '1TEN', nome: 'Primeiro-Tenente', tipo: 'AVALIADOR' },
    { cod: '2TEN', nome: 'Segundo-Tenente', tipo: 'AVALIADOR' },
    { cod: 'SO', nome: 'Suboficial', tipo: 'GRADUADO' },
    { cod: '1SGT', nome: 'Primeiro-Sargento', tipo: 'GRADUADO' },
    { cod: '2SGT', nome: 'Segundo-Sargento', tipo: 'GRADUADO' },
    { cod: '3SGT', nome: 'Terceiro-Sargento', tipo: 'GRADUADO' }
  ];

  cadastroForm = this.fb.nonNullable.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required, Validators.minLength(3)]],
    saram: ['', [Validators.required]],
    nguerra: ['', [Validators.required]],
    secao: ['', [Validators.required]],
    codposto: ['', [Validators.required]],
    posto: ['', [Validators.required]],
    quadro: ['', [Validators.required]],
    funcao: ['', [Validators.required]],
    antiguidade: [1, [Validators.required, Validators.min(1)]],
    ativo: [true],
    excecaoGraduado: [false]
  });

  aoSelecionarPosto(cod: string): void {
    const encontrado = this.postos.find(p => p.cod === cod);
    if (encontrado) {
      this.cadastroForm.patchValue({ posto: encontrado.nome });
      if (encontrado.tipo !== 'GRADUADO') {
        this.cadastroForm.patchValue({ excecaoGraduado: false });
      }
    }
  }

    get isGraduadoSelecionado(): boolean {
    const cod = this.cadastroForm.get('codposto')?.value;
    return ['SO', '1SGT', '2SGT', '3SGT'].includes(cod || '');
  }

  salvar(): void {
    if (this.cadastroForm.invalid) return;

    this.carregando = true;
    const { excecaoGraduado, ...dadosUsuario } = this.cadastroForm.getRawValue();

    this.userService.cadastrar(dadosUsuario, excecaoGraduado).subscribe({
      next: () => {
        this.carregando = false;
        this.snackBar.open('Usuário cadastrado com sucesso!', 'OK', { duration: 3500 });
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.carregando = false;
        const msg = err.error?.mensagem || 'Erro ao cadastrar usuário. Verifique os dados.';
        this.snackBar.open(msg, 'Fechar', { duration: 5000 });
      }
    });
  }
}
