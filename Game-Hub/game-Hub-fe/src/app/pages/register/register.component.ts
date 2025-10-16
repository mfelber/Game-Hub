import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser, CommonModule } from '@angular/common';
import { RegistrationRequest } from '../../services/models/registration-request';
import { NgForOf, NgIf, NgOptimizedImage } from '@angular/common';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthenticationService } from '../../services/services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    FormsModule,
    NgOptimizedImage,
    CommonModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent implements OnInit {

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  async ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const flowbite = await import('flowbite');
      flowbite.initFlowbite();
    }
  }

  registerRequest: RegistrationRequest = {
    email: '', firstName: '', lastName: '', password: '', username: ''
  };
  errorMessage: Array<string> = [];
  successMessage = '';

  register() {
    this.errorMessage = [];
    this.successMessage = '';
    this.authenticationService.register({
      body: this.registerRequest
    }).subscribe({
      next: () => {
        this.successMessage = 'Registration successful! Redirecting to login...';
        this.registerRequest.firstName = ''
        this.registerRequest.lastName = ''
        this.registerRequest.username = ''
        this.registerRequest.email = ''
        this.registerRequest.password = ''

        setTimeout(() => {
          this.router.navigate(['login']);
        },3000);
      },
      error: (err) => {
        this.errorMessage = err.error.validationErrors;
      }
    });
  }

  login() {
    this.router.navigate(['login']);
  }
}
