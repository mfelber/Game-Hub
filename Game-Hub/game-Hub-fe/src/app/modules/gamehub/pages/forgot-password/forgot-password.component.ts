import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {AuthenticationRequest} from '../../../../services/models/authentication-request';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../../../services/services/authentication.service';

@Component({
  selector: 'app-forgot-password',
    imports: [
        FormsModule,
        NgForOf,
        NgIf,
        NgOptimizedImage,
        ReactiveFormsModule
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

        setTimeout(() => {
          this.router.navigate(['login']);
        },3000);

      },
      error: (err) => {
        console.error(err);
      }
    })
  }

}
