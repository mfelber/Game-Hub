import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {initFlowbite} from 'flowbite';
import {NgForOf, NgIf} from '@angular/common';
import {UserProfileControllerService} from '../../../../services/services';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';

@Component({
  selector: 'app-menu',
  imports: [
    RouterLink,
    RouterLinkActive,
    NgIf
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {
  ngOnInit(): void {
    initFlowbite();
    this.loadUserName();
  }

  isStoreActive = false;
  isLibraryActive = false;
  _isGameDetailsActive = false;
  userResponse: UserPrivateResponse = {};

  constructor(
    private router: Router,
    private userService: UserProfileControllerService
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
    this.userService.getUserPrivate().subscribe({
      next:  (user) => {
        this.userResponse = user;
        console.log(user)
        // this.getProfilePicture(user)
      }
    });

  }

  updateActiveTabs(url: string) {
    this.isStoreActive = url === '/gamehub' || url.startsWith('/gamehub/game/');
    this.isLibraryActive = url === '/gamehub/library' || url.startsWith("/gamehub/library/game/");
  }

  isGameDetailsActive(url: string){
    this._isGameDetailsActive = url.startsWith("/gamehub/library/game/") || url.startsWith("/gamehub/game/") || url.startsWith("/gamehub/user/");
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
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  setUserToOffline() {
    this.userService.setStatusToOffline().subscribe({
      next: () => {
        window.location.href = '/logout';
      },
      error: (err) => console.error(err)
    })
  }
}
