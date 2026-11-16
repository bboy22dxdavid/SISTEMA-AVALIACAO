import { Component, inject } from '@angular/core';
import { MaterialModule } from '../../shared/components/material-module/material-module';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../../core/services/UserService';
import { AuthService } from '../../core/services/auth';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';
import { AvaliadoService } from '../../core/services/avaliado';

@Component({
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MaterialModule],
  selector: 'app-usuario-cadastro',
  styleUrl: './usuario-cadastro.scss',
  templateUrl: './usuario-cadastro.html',
})
export class UsuarioCadastro {
private fb = inject(FormBuilder);
  private userService = inject(UserService);
  private avaliadoService = inject(AvaliadoService);
  private authService = inject(AuthService);
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
    codposto: ['', [Validators.required]],
    posto: ['', [Validators.required]],
    nguerra: ['', [Validators.required]],
    saram: ['', [Validators.required]],
    secao: ['', [Validators.required]],
    quadro: ['', [Validators.required]],
    funcao: ['', [Validators.required]],
    antiguidade: [1, [Validators.required, Validators.min(1)]],
    ativo: [true],
    excecaoGraduado: [false],
    username: [''],
    password: ['']
  });

  get isGraduadoSelecionado(): boolean {
    const cod = this.cadastroForm.get('codposto')?.value;
    return ['SO', '1SGT', '2SGT', '3SGT'].includes(cod || '');
  }

  get precisaDeLogin(): boolean {
    return !this.isGraduadoSelecionado || this.cadastroForm.get('excecaoGraduado')?.value === true;
  }

  aoSelecionarPosto(cod: string): void {
    const encontrado = this.postos.find(p => p.cod === cod);
    if (encontrado) {
      this.cadastroForm.patchValue({ posto: encontrado.nome });
      if (encontrado.tipo !== 'GRADUADO') {
        this.cadastroForm.patchValue({ excecaoGraduado: false });
      }
      this.atualizarValidadoresLogin();
    }
  }

  aoAlterarExcecao(): void {
    this.atualizarValidadoresLogin();
  }

  private atualizarValidadoresLogin(): void {
    const usernameCtrl = this.cadastroForm.get('username');
    const passwordCtrl = this.cadastroForm.get('password');

    if (this.precisaDeLogin) {
      usernameCtrl?.setValidators([Validators.required]);
      passwordCtrl?.setValidators([Validators.required, Validators.minLength(3)]);
    } else {
      usernameCtrl?.clearValidators();
      passwordCtrl?.clearValidators();
      usernameCtrl?.setValue('');
      passwordCtrl?.setValue('');
    }

    usernameCtrl?.updateValueAndValidity();
    passwordCtrl?.updateValueAndValidity();
  }

  salvar(): void {
    if (this.cadastroForm.invalid) return;

    this.carregando = true;
    const formVal = this.cadastroForm.getRawValue();

    const dadosMilitares = {
      saram: formVal.saram,
      nguerra: formVal.nguerra,
      secao: formVal.secao,
      codposto: formVal.codposto,
      posto: formVal.posto,
      quadro: formVal.quadro,
      funcao: formVal.funcao,
      antiguidade: formVal.antiguidade,
      ativo: formVal.ativo
    };

    // Caso 1: Graduado comum -> Salva apenas na tabela de Avaliados
    if (this.isGraduadoSelecionado && !formVal.excecaoGraduado) {
      this.avaliadoService.salvar(dadosMilitares).subscribe({
        next: () => this.finalizarComSucesso('Graduado cadastrado para avaliação com sucesso!'),
        error: (err) => this.tratarErro(err)
      });
      return;
    }

    // Caso 2: Graduado com exceção -> Salva como Avaliado (para ser pontuado) E como Usuário Avaliador (para pontuar)
    if (this.isGraduadoSelecionado && formVal.excecaoGraduado) {
      const dadosUsuario = {
        ...dadosMilitares,
        username: formVal.username,
        password: formVal.password
      };

      forkJoin([
        this.avaliadoService.salvar(dadosMilitares),
        this.userService.cadastrar(dadosUsuario, true)
      ]).subscribe({
        next: () => this.finalizarComSucesso('Graduado cadastrado como Avaliado e Avaliador (Exceção)!'),
        error: (err) => this.tratarErro(err)
      });
      return;
    }

    // Caso 3: Oficial -> Salva como Usuário (Avaliador ou Revisor)
    const dadosOficial = {
      ...dadosMilitares,
      username: formVal.username,
      password: formVal.password
    };

    this.userService.cadastrar(dadosOficial, false).subscribe({
      next: () => this.finalizarComSucesso('Oficial cadastrado no sistema com sucesso!'),
      error: (err) => this.tratarErro(err)
    });
  }

  private finalizarComSucesso(mensagem: string): void {
    this.carregando = false;
    this.snackBar.open(mensagem, 'OK', { duration: 4000 });
    const rotaDestino = this.authService.isAuthenticated() ? '/ranking' : '/login';
    this.router.navigate([rotaDestino]);
  }

  private tratarErro(err: any): void {
    this.carregando = false;
    const msg = err.error?.mensagem || 'Erro ao salvar cadastro. Verifique se SARAM ou usuário já existem.';
    this.snackBar.open(msg, 'Fechar', { duration: 5000 });
  }

}
