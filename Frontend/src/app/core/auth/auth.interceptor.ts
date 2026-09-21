import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Auth } from './auth';

export const authInterceptor: HttpInterceptorFn = (peticion, siguiente) => {

  const auth = inject(Auth);
  const token = auth.accessToken;

  // Las rutas de /auth no llevan token
  const peticionConToken =
    token && !peticion.url.includes('/auth/')
      ? peticion.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
      : peticion;

  return siguiente(peticionConToken).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !peticion.url.includes('/auth/login')) {
        auth.logout();
      }
      return throwError(() => error);
    })
  );
};