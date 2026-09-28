import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { NgOptimizedImage } from "@angular/common";
import {AuthenticationRequest} from '../../../../services/models/authentication-request';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../../../services/services/authentication.service';
import {HlmInputGroup, HlmInputGroupAddon, HlmInputGroupInput} from '@spartan/input-group';

@Component({
  selector: 'app-forgot-password',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    HlmInputGroup,
    HlmInputGroupAddon,
    HlmInputGroupInput
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss'
})
export class ForgotPasswordComponent {

  authenticationRequest: AuthenticationRequest = {
    email: '',
    password: ''
  }
  errorMessage: Array<string>[] = [];
  successMessage: string = '';

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
  ) {
  }

  resetPassword() {
    this.authenticationService.processForgotPasswordRequest({
      body: this.authenticationRequest
    }).subscribe({
      next: () => {
        this.authenticationRequest.email = ''
        this.successMessage = 'A password reset link has been sent to your email address.'
        setTimeout(() => {
          this.router.navigate(['login']);
        },3000);

      },
      error: (err) => {
        console.error(err);
      }
    })
  }

  login() {
    this.router.navigate(['login']);
  }

}
