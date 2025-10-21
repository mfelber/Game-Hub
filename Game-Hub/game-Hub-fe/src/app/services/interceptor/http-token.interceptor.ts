import {HttpHeaders, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {TokenService} from '../token/token.service';

export const httpTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService)
  const token = tokenService.token

  if (req.url.includes('/auth')) {
    return next(req);
  }

  if (token) {
    const authRequest = req.clone({
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + token
      })
    })
    return next(authRequest);
  }
  return next(req);
};
