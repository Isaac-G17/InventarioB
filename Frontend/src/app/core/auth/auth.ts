import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { API_URL } from '../api';
import { AuthResponse, LoginRequest } from './auth.models';

const ACCESS_TOKEN = 'accessToken';
const REFRESH_TOKEN = 'refreshToken';
const USERNAME = 'username';

@Injectable({ providedIn: 'root' })
export class Auth {

  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  readonly usuario = signal<string | null>(localStorage.getItem(USERNAME));

  login(credenciales: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${API_URL}/auth/login`, credenciales)
      .pipe(tap((respuesta) => this.guardarSesion(respuesta, credenciales.username)));
  }

  logout(): void {
    localStorage.removeItem(ACCESS_TOKEN);
    localStorage.removeItem(REFRESH_TOKEN);
    localStorage.removeItem(USERNAME);
    this.usuario.set(null);
    this.router.navigate(['/login']);
  }

  get accessToken(): string | null {
    return localStorage.getItem(ACCESS_TOKEN);
  }

  estaAutenticado(): boolean {
    return this.accessToken !== null;
  }

  private guardarSesion(respuesta: AuthResponse, username: string): void {
    localStorage.setItem(ACCESS_TOKEN, respuesta.accessToken);
    localStorage.setItem(REFRESH_TOKEN, respuesta.refreshToken);
    localStorage.setItem(USERNAME, username);
    this.usuario.set(username);
  }
}