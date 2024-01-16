import {inject} from '@angular/core';
import { Router } from '@angular/router';
import {LoginService} from "./login.service";


export const LoginGuard = () => {
  const loginService = inject(LoginService);
  const router = inject(Router);

  if (!!loginService.user.value) {
    return true;
  }

  // Redirect to the login page
  console.log("loginGuard false");
  return router.createUrlTree(['/login']);
};
