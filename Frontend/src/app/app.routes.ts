import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { Login } from './features/login/login';
import { Inicio } from './features/inicio/inicio';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'inicio', component: Inicio, canActivate: [authGuard] },
  { path: '', pathMatch: 'full', redirectTo: 'inicio' },
  { path: '**', redirectTo: 'inicio' },
];
