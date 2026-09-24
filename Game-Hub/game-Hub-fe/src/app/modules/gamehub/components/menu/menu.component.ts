import {Component, OnInit} from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {initFlowbite} from 'flowbite';
import { NgClass, NgStyle } from '@angular/common';
import {UserProfileControllerService} from '../../../../services/services';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {FormsModule} from '@angular/forms';
import {StatusResponse} from '../../../../services/models/status-response';
import {RefreshService} from '../../../../services/fn/refresh-service/refresh-service';
import {
  HlmSidebar,
  HlmSidebarContent,
  HlmSidebarFooter,
  HlmSidebarHeader,
  HlmSidebarMenu,
  HlmSidebarMenuButton,
  HlmSidebarMenuItem,
  HlmSidebarTrigger, HlmSidebarWrapper,
} from '@spartan/sidebar';
import {HlmDropdownMenu, HlmDropdownMenuItem, HlmDropdownMenuTrigger} from '@spartan/dropdown-menu';

@Component({
  selector: 'app-menu',
  imports: [
    RouterLink,
    RouterLinkActive,
    FormsModule,
    RouterLinkActive,
    NgClass,
    NgStyle,
    HlmSidebar,
    HlmSidebarContent,
    HlmSidebarFooter,
    HlmSidebarHeader,
    HlmSidebarMenu,
    HlmSidebarMenuButton,
    HlmSidebarMenuItem,
    HlmSidebarTrigger,
    HlmSidebarWrapper,
    HlmDropdownMenuTrigger,
    HlmDropdownMenu,
    HlmDropdownMenuItem,
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
  }

  userResponse: UserPrivateResponse = {};
  statusResponse: StatusResponse = {};
  statuses = ['Online', 'Offline', 'Away'];
  userHasProfilePicture = true;
  isLoading = false;

  constructor(
    private router: Router,
    protected userService: UserProfileControllerService,
    private refreshService: RefreshService
  ) {

  }


  friendReqCount: any = 0
  private loadUser() {
    this.isLoading = true;
    this.userService.getUserPrivateShort().subscribe({
      next: (user) => {
        this.userResponse = user;
        this.friendReqCount = this.userResponse.friendReqCount;
        if (user.userProfilePicture) {
          this.userHasProfilePicture = true;
        } else {
          this.userHasProfilePicture = false;
        }
        this.isLoading = false;
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
      case 'Online':
        this.setUserToOnline()
        break
      case 'Offline':
        this.setUserToOffline()
        break
      case 'Away':
        this.setUserToAway()
        break
    }

  }

  changeStatus(status: string) {
    this.onStatusChange(status)
  }

}
