import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthenticationService, UserProfileControllerService} from '../services';
import {authGuard} from './auth.guard';
import {map} from 'rxjs/operators';

export const userProfileGuard: CanActivateFn = (route, state) => {
  const userService = inject(UserProfileControllerService)
  const router = inject(Router)

  const userId = Number(route.paramMap.get('id'));
  return userService.getUserPrivate().pipe(map(user => {
    if (user.userId === userId) {
      return router.parseUrl('/gamehub/user/me')
    }
    return true;
  }))
};
