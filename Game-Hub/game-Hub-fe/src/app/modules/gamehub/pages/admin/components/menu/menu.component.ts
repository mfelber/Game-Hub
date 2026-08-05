import { Component } from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {UserProfileControllerService} from '../../../../../../services/services/user-profile-controller.service';

@Component({
  selector: 'app-menu-admin',
  imports: [
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss',
})
export class AdminMenuComponent {

  constructor(
    private router: Router,
    protected userService: UserProfileControllerService
  ) {
  }

  async logoutUser() {

    this.userService.setStatusToOffline().subscribe({
      next: () => {
        localStorage.clear();
        this.router.navigate(['login']);
      },
      error: (err) => console.error(err)
    })
  }

}
