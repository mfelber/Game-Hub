import {Component, OnInit} from '@angular/core';

import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserNotificationsResponse} from '../../../../services/models/user-notifications-response';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {
  HlmDialog,
  HlmDialogContent,
  HlmDialogDescription,
  HlmDialogHeader, HlmDialogPortal,
  HlmDialogTitle,
  HlmDialogTrigger
} from '@spartan/dialog';
import {HlmButton} from '@spartan/button';

@Component({
  selector: 'app-user-actions',
  imports: [
    RouterLink,
    HlmDialog,
    HlmDialogContent,
    HlmDialogTrigger,
    HlmDialogHeader,
    HlmDialogTitle,
    HlmDialogDescription,
    HlmDialogPortal
  ],
  templateUrl: './user-actions.component.html',
  styleUrl: './user-actions.component.scss',
})
export class UserActionsComponent implements OnInit {

  userNotificationsResponse: UserNotificationsResponse = {}

  constructor(
    private userService: UserProfileControllerService
  ) {
  }

  ngOnInit() {
    this.loadNotifications()
  }

  loadNotifications() {
    this.userService.getUserNotifications().subscribe({
      next: data => {
        this.userNotificationsResponse = data
        console.log(data);
      }
    })
  }

}
