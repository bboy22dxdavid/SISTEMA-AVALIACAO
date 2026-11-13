import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core/primitives/di';
import { AuthService } from '../services/auth';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

   // Não injeta token nas rotas públicas de login e cadastro de usuário
  const isPublicRoute = req.url.includes('/api/auth/login') ||
                        (req.url.endsWith('/api/users') && req.method === 'POST');

  if (token && !isPublicRoute) {
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedReq);
  }

  return next(req);
};
