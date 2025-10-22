import { Component } from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {FormsModule} from '@angular/forms';
import { Router } from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';
import {TokenService} from '../../services/token/token.service';

@Component({
  selector: 'app-login',
    imports: [
        NgForOf,
        NgIf,
        FormsModule,
        NgOptimizedImage
    ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authenticationRequest: AuthenticationRequest = {
    email: '',
    password: ''
  }
  errorMessage: Array<string>[] = []

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private tokenService: TokenService
  ) {
  }

  login() {
    this.errorMessage = [];
    this.authenticationService.authenticate( {
      body: this.authenticationRequest
    }).subscribe({
      next: (res) => {
        this.tokenService.token = res.token as string;
        this.router.navigate(['gamehub']);
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  register() {
    this.router.navigate(['register']);
  }

  resetPassword() {
    this.router.navigate(['forgot-password']);
  }
}
