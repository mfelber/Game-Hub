import {Component, OnInit} from '@angular/core';
import {NgIf} from "@angular/common";
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserNotificationsResponse} from '../../../../services/models/user-notifications-response';
import {RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  selector: 'app-user-actions',
  imports: [
    NgIf,
    RouterLink
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
