import { Routes } from '@angular/router';

export const routes: Routes = [
    // Rota padrão redireciona para a tela inicial (Ranking) ou Login
    {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
   // Tela de Autenticação
    {
    path: 'login',
    loadComponent: () =>
      import('./features/login/login').then(m => m.Login),
    title: 'SANG - Login'
  },

   // Tela Inicial: Ranking de Militares por Processo
  {
    path: 'ranking',
    loadComponent: () =>
      import('./features/ranking/ranking').then(m => m.Ranking),
    title: 'SANG - Ranking de Avaliação'
    // canActivate: [authGuard] // 👈 Descomentar após criar o authGuard
  },

  // Tela de Cadastro de Usuários (Avaliadores e Revisores)
  {
    path: 'usuarios/novo',
    loadComponent: () =>
      import('./features/usuario-cadastro/usuario-cadastro').then(m => m.UsuarioCadastro),
    title: 'SANG - Cadastro de Usuários'
    // canActivate: [authGuard]
  },

  // Tela de Cadastro de Graduados Avaliados
  {
    path: 'avaliados/novo',
    loadComponent: () =>
      import('./features/avaliado-cadastro/avaliado-cadastro').then(m => m.AvaliadoCadastro),
    title: 'SANG - Cadastro de Avaliados'
    // canActivate: [authGuard]
  },

  // Tela de Lançamento de Avaliação (15 Critérios)
  {
    path: 'avaliacao/:avaliadoId',
    loadComponent: () =>
      import('./features/avaliacao-nivelamento/avaliacao-nivelamento').then(m => m.AvaliacaoNivelamento),
    data: { modo: 'AVALIACAO' },
    title: 'SANG - Avaliação de Graduado'
    // canActivate: [authGuard]
  },

  // Tela de Nivelamento / Revisão pelo Comando (Modo 1:1)
  {
    path: 'nivelamento/:avaliacaoId',
    loadComponent: () =>
      import('./features/avaliacao-comando/avaliacao-comando').then(m => m.AvaliacaoComando),
    data: { modo: 'NIVELAMENTO' },
    title: 'SANG - Nivelamento do Comando'
    // canActivate: [authGuard]
  },

  // Rota Coringa: redireciona qualquer URL inexistente para o Login
  {
    path: '**',
    redirectTo: 'login'
  }
];
