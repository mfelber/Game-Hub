import { Routes } from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {ForgotPasswordComponent} from './modules/gamehub/pages/forgot-password/forgot-password.component';
import {ResetPasswordComponent} from './modules/gamehub/pages/reset-password/reset-password.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, // default page
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'forgot-password',
    component: ForgotPasswordComponent
  },
  {
    path: 'reset-password',
    component: ResetPasswordComponent
  },
  {
    path: 'gamehub',
    loadChildren: () => import('./modules/gamehub/gamehub.module').then(m => m.GamehubModule)
  }
];
