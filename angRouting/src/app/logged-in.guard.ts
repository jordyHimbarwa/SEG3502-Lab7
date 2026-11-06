import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthenticationService } from './authentication.service';

export const loggedInGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthenticationService);
  const router = inject(Router);

  if (authService.isLoggedIn()) { return true; }
  authService.redirectUrl = state.url;
  return router.createUrlTree(['/login']);
};
