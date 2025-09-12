import { Component } from '@angular/core';
import {RegistrationRequest} from '../../services/models/registration-request';
import {NgForOf, NgIf} from '@angular/common';
import {FormBuilder, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthenticationService} from '../../services/services/authentication.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  registerRequest: RegistrationRequest = {
    email: '', firstName: '', lastName: '', password: '', username: ''
  }
  errorMessage: Array<string> = [];

  constructor(private router: Router,
              private authenticationService: AuthenticationService) {}

  register() {
    this.errorMessage = [];
    this.authenticationService.register(
      {
        body: this.registerRequest
      }
    ).subscribe({
      next: () => {
        this.router.navigate(['library']);
    },
    error: (err) => {
        this.errorMessage = err.error.validationErrors;
    }
    })
  }

  login() {
    this.router.navigate(['login']);
  }
}
