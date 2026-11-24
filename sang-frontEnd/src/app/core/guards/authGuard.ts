import { inject } from '@angular/core/primitives/di';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth';
import { UserRole } from '../models/auth';

export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;
  }

  void router.navigate(['/login']);
  return false;
};
export const roleGuard = (allowedRoles: UserRole[]): CanActivateFn => {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (!authService.isAuthenticated()) {
      void router.navigate(['/login']);
      return false;
    }

    if (authService.hasAnyRole(allowedRoles)) {
      return true;
    }

    void router.navigate(['/ranking']);
    return false;
  };
};
