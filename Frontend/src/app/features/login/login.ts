import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Auth } from '../../core/auth/auth';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {

  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(Auth);
  private readonly router = inject(Router);

  protected readonly cargando = signal(false);
  protected readonly error = signal<string | null>(null);

  protected readonly formulario = this.fb.nonNullable.group({
    username: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(4)]],
  });

  protected enviar(): void {

    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.cargando.set(true);
    this.error.set(null);

    this.auth.login(this.formulario.getRawValue()).subscribe({
      next: () => this.router.navigate(['/inicio']),
      error: (err: HttpErrorResponse) => {
        this.cargando.set(false);
        this.error.set(this.mensajeDeError(err));
      },
    });
  }

  private mensajeDeError(err: HttpErrorResponse): string {

    if (err.status === 0) {
      return 'No se pudo conectar con el servidor. ¿Está corriendo el backend?';
    }

    // El backend responde ProblemDetail: el texto viene en "detail"
    return err.error?.detail ?? 'Ocurrió un error inesperado';
  }
}