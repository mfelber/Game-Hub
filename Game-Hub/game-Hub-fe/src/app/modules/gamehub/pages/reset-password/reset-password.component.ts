import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';
import {AuthenticationService} from '../../../../services/services/authentication.service';
@Component({
  selector: 'app-reset-password',
  imports: [
    FormsModule,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.scss'
})
export class ResetPasswordComponent implements OnInit{

  token: any
  password: string = '';
  confirmPassword: string = '';
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
  }

  ngOnInit() {
    this.token = this.route.snapshot.queryParamMap.get('token');

    if (!this.token){
      this.router.navigate(['login']);
    }

  }

  resetPassword() {

    if (!this.password || !this.confirmPassword) {
      this.errorMessage = 'Please fill in all fields';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    if (this.password.length < 6 && this.confirmPassword.length < 6) {
      this.errorMessage = 'Password should be at least 6 characters';
      return;
    }

    this.authenticationService.getResetTokenInfo({ token: this.token }).subscribe({
      next: (res) => {
        if (res.expired) {
          this.errorMessage = 'Token has expired request for password change again';
        }
      },
      error: () => {
        this.errorMessage = 'Invalid token';
      }
    });

    const params = {
      token: this.token,
      newPassword: this.password
    };

    this.authenticationService.resetPassword(params).subscribe({
      next: () => {

        this.password = '';
        this.confirmPassword = '';
        this.errorMessage = '';
        this.successMessage = 'Password reset successfully!';
        setTimeout(() => {
          this.router.navigate(['login']);
        },3000);
      },
      error: (err) => {
        this.successMessage = '';
      }
    });
  }

  forgotPassword() {
    this.router.navigate(['forgot-password']);
  }
}
