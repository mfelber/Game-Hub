import {Component, OnInit} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {initFlowbite} from 'flowbite';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {UserProfileControllerService} from '../../../../services/services';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {FormsModule} from '@angular/forms';
import {StatusResponse} from '../../../../services/models/status-response';
import {RefreshService} from '../../../../services/fn/refresh-service/refresh-service';

@Component({
  selector: 'app-menu',
  imports: [
    RouterLink,
    RouterLinkActive,
    NgIf,
    FormsModule,
    RouterLinkActive,
    NgForOf,
    NgClass,
    NgStyle
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {

  ngOnInit(): void {
    initFlowbite();
    this.loadUser();
    this.getStatus();
    this.refreshService.refresh$.subscribe(() => {
      this.loadUser();
    });
    this.refreshService.refresh$.subscribe(() => {
      this.loadUser();
    });
  }

  statusMenuOpen = false;
  userResponse: UserPrivateResponse = {};
  statusResponse: StatusResponse = {};
  statuses = ['ONLINE', 'OFFLINE', 'AWAY'];
  userHasProfilePicture = true;

  constructor(
    private router: Router,
    protected userService: UserProfileControllerService,
    private refreshService: RefreshService
  ) {

  }

  private loadUser() {
    this.userService.getUserPrivateShort().subscribe({
      next: (user) => {
        this.userResponse = user;
        console.log(this.userResponse);
        if (user.userProfilePicture) {
          this.userHasProfilePicture = true;
        } else {
          this.userHasProfilePicture = false;
        }
      }
    });

  }

  getStatus() {
    this.userService.getUserStatus().subscribe({
      next: (response) => {
        this.statusResponse = response
      },
      error: (err) => {
        console.error('error getting status:', err);
      }
    });
  }

  getProfilePicture(user: UserPrivateResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return this.userHasProfilePicture;
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

  setUserToOnline() {
    this.userService.setStatusToOnline().subscribe({
      next: () => {
        this.getStatus();

      }
    })
  }

  setUserToOffline() {
    this.userService.setStatusToOffline().subscribe({
      next: () => {
        this.getStatus();

      }
    })
  }

  setUserToAway() {
    this.userService.setStatusToAway().subscribe({
      next: () => {
        this.getStatus();
      }
    })
  }

  onStatusChange(newStatus: string) {

    switch (newStatus) {
      case 'ONLINE':
        this.setUserToOnline()
        break
      case 'OFFLINE':
        this.setUserToOffline()
        break
      case 'AWAY':
        this.setUserToAway()
        break
    }

  }

  toggleStatusMenu(event: MouseEvent) {
    event.stopPropagation();
    this.statusMenuOpen = !this.statusMenuOpen;
  }

  changeStatus(status: string) {
    this.statusMenuOpen = false;
    this.onStatusChange(status)
  }

}
