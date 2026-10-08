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
import {NewsControllerService} from '../../../../services/services/news-controller.service';
import {NewsResponse} from '../../../../services/models/news-response';
import {DatePipe} from '@angular/common';

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
    HlmDialogPortal,
    DatePipe
  ],
  templateUrl: './user-actions.component.html',
  styleUrl: './user-actions.component.scss',
})
export class UserActionsComponent implements OnInit {

  userNotificationsResponse: UserNotificationsResponse = {};

  newsResponse: NewsResponse[] = [];

  constructor(
    private userService: UserProfileControllerService,
    private newsService: NewsControllerService
  ) {
  }

  ngOnInit() {
    this.loadNotifications();
    this.loadNews();
  }

  loadNotifications() {
    this.userService.getUserNotifications().subscribe({
      next: data => {
        this.userNotificationsResponse = data
        console.log(data);
      }
    })
  }

  loadNews() {
    this.newsService.getNews().subscribe({
      next: data => {
        console.log(data);
        this.newsResponse = data;
      }
    })
  }

}
