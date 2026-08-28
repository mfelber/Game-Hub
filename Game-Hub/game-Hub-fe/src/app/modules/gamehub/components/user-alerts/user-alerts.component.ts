import {Component, OnInit} from '@angular/core';
import {NgIf} from "@angular/common";
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserNotificationsResponse} from '../../../../services/models/user-notifications-response';

@Component({
  selector: 'app-user-alerts',
  imports: [
    NgIf
  ],
  templateUrl: './user-alerts.component.html',
  styleUrl: './user-alerts.component.scss',
})
export class UserAlertsComponent implements OnInit {

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
