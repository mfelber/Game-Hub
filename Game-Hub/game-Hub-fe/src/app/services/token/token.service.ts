import { Injectable } from '@angular/core';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string) {
    localStorage.setItem('token', token);
  }

  set role(role: string) {
    localStorage.setItem('role', role);
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  get role() {
    return localStorage.getItem('role') as string;
  }

  isTokenNotValid() {
    return !this.isTokenValid()
  }

  private isTokenValid() {
    const token = this.token
    if (!token) {
      return false;
    }

    const jwtHelper = new JwtHelperService();
    // check expiry date
    const isTokenExpired = jwtHelper.isTokenExpired(token)
    if (isTokenExpired) {
      localStorage.clear();
      return false;
    }

    return true;
  }
}
