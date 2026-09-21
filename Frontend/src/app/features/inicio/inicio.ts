import { Component, inject } from '@angular/core';
import { Auth } from '../../core/auth/auth';

@Component({
  selector: 'app-inicio',
  imports: [],
  templateUrl: './inicio.html',
  styleUrl: './inicio.scss',
})
export class Inicio {

  protected readonly auth = inject(Auth);
}