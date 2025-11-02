import {Component} from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {RouterOutlet} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/services/authentication.service';
import {TokenService} from '../../services/token/token.service';
import {UserProfileControllerService} from '../../services/services/user-profile-controller.service';

@Component({
  selector: 'app-login',
  imports: [
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
  errorMessage: string = '';

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService,
    private tokenService: TokenService,
    private userService: UserProfileControllerService
  ) {
  }

  login() {

    if (this.errorMessage.length) {
      return;
    }
    if (!this.validateLogin()) {
      return;
    }

    this.errorMessage = '';
    this.authenticationService.authenticate({
      body: this.authenticationRequest
    }).subscribe({
      next: (res) => {
        this.tokenService.token = res.token as string;
        this.router.navigate(['gamehub']);
        this.setUserToOnline()
      },
      error: (err) => {
        if (err.status === 403) {
          this.errorMessage = 'Incorrect email or password';
        } else {
          this.errorMessage = 'An error occurred. Please try again later';
        }
      }
    })
  }

  setUserToOnline(){
    this.userService.setStatusToOnline().subscribe({
      next: () => {
        console.log("nastaveny online")
      }
    });
  }

  register() {
    this.router.navigate(['register']);
  }

  resetPassword() {
    this.router.navigate(['forgot-password']);
  }

  validateLogin(): boolean {
    const {email, password} = this.authenticationRequest;

    if (!email?.trim() && !password?.trim()) {
      this.errorMessage = 'Please fill in all required fields';
      return false;
    }

    // valid email regex
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(email)) {
      this.errorMessage = 'Please enter a valid email address';
      return false;
    }

    if (!password) {
      this.errorMessage = 'Please enter your password';
      return false;
    }

    this.errorMessage = '';
    return true;
  }
}
