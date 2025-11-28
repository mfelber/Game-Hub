import {Component, OnInit, Inject, PLATFORM_ID} from '@angular/core';
import {isPlatformBrowser, CommonModule} from '@angular/common';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {FormBuilder, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthenticationService} from '../../services/services/authentication.service';
import {Router} from '@angular/router';
import {RegistrationRequest} from '../../services/models/registration-request';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
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
  ) {
  }

  async ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      const flowbite = await import('flowbite');
      flowbite.initFlowbite();
    }
  }

  registerRequest: RegistrationRequest = {
    email: '', firstName: '', lastName: '', password: '', username: '', childAccount: null!, parentEmail: ''
  };

  errorMessage: string = '';
  successMessage = '';
  childAccount = false;

  register() {
    this.errorMessage = '';
    this.successMessage = '';
    if (this.errorMessage.length) {
      return;
    }

    if (!this.validateRegister()) {
      return;
    }

    this.authenticationService.checkUserExists({email: this.registerRequest.email}).subscribe({
      next: (exists) => {
        if (exists) {
          this.errorMessage = 'Account with this Email already exists';
        }
        this.registerUser();
      },
      error: (err) => {
        console.error('Error', err);
      }
    });


  }

  private registerUser() {

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
        this.registerRequest.parentEmail = ''
        this.registerRequest.childAccount = null!

        setTimeout(() => {
          this.router.navigate(['login']);
        }, 2500);
      },
      error: (err) => {
        this.errorMessage = err.error.validationErrors;
      }
    });
  }

  login() {
    this.router.navigate(['login']);
  }

  validateRegister(): boolean {
    const {email, firstName, lastName, username, password, parentEmail} = this.registerRequest;

    if (this.childAccount && !parentEmail?.trim()) {
      this.errorMessage = 'Please enter parent email'
      return false;
    }

    if (!firstName?.trim() && !lastName?.trim() && !email?.trim() && !password?.trim() && !username?.trim()) {
      this.errorMessage = 'Please fill in all required fields';
      return false;
    }

    // valid email regex
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(email)) {
      this.errorMessage = 'Please enter a valid email address';
      return false;
    }

    if (!firstName?.trim()) {
      this.errorMessage = 'Please enter your first name';
      return false;
    }

    if (!lastName?.trim()) {
      this.errorMessage = 'Please enter your last name';
      return false;
    }

    if (!username?.trim()) {
      this.errorMessage = 'Please enter your username';
      return false;
    }

    if (!password) {
      this.errorMessage = 'Please enter your password';
      return false;
    }

    if (password.length < 6) {
      this.errorMessage = 'Password should have at least 6 characters';
      return false;
    }

    this.errorMessage = '';
    return true;
  }

  createChildAccount() {
    if (this.childAccount) {
      this.childAccount = false
    } else {
      this.childAccount = true;
    }
  }
}
