import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterLinkActive} from '@angular/router';
import {initFlowbite} from 'flowbite';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {UserProfileControllerService} from '../../../../services/services';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {FormsModule} from '@angular/forms';
import {StatusResponse} from '../../../../services/models/status-response';

@Component({
  selector: 'app-menu',
  imports: [
    RouterLink,
    RouterLinkActive,
    NgIf,
    FormsModule,
    NgForOf,
    NgClass,
    NgStyle
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {
  @ViewChild('dropdownAvatarName') dropdown!: ElementRef<HTMLDivElement>;

  closeDropdown() {
    this.dropdown.nativeElement.classList.add('hidden');
  }

  ngOnInit(): void {
    initFlowbite();
    this.loadUserName();
    this.getStatus();
  }

  isStoreActive = false;
  isLibraryActive = false;
  isCommunityActive = false;
  _isGameDetailsActive = false;
  userResponse: UserPrivateResponse = {};
  statusResponse: StatusResponse = {};
  statuses = ['ONLINE', 'OFFLINE', 'AWAY'];
  userHasProfilePicture = true;

  constructor(
    private router: Router,
    protected userService: UserProfileControllerService
  ) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const url = this.router.url;
        this.updateActiveTabs(url);
        this.isGameDetailsActive(url)
      }
    });
  }

  private loadUserName() {
    this.userService.getUserPrivateShort().subscribe({
      next: (user) => {
        this.userResponse = user;
        console.log(user)
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

  updateActiveTabs(url: string) {
    this.isStoreActive = url === '/gamehub' || url.startsWith('/gamehub/game/');
    this.isLibraryActive = url === '/gamehub/library' || url.startsWith("/gamehub/library/game/");
    this.isCommunityActive = url === '/gamehub/community';
  }

  isGameDetailsActive(url: string) {
    this._isGameDetailsActive =
      url.startsWith("/gamehub/library/game/")
      || url.startsWith("/gamehub/game/")
      || url.startsWith("/gamehub/user/")
      || url.startsWith("/gamehub/community");
  }

  goToCart() {
    console.log("Cart clicked!");
  }

  showNotifications() {
    console.log("Notifications clicked!");
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
        window.location.href = '/logout';
        localStorage.clear();
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

  onStatusChange($event: Event) {
    const newStatus = (event?.target as HTMLSelectElement).value;

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

  getAvailableStatuses(): string[] {
    return this.statuses.filter(s => s !== this.userResponse.status);
  }

}
