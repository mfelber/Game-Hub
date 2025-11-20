import {Component, OnInit} from '@angular/core';
import {initFlowbite} from 'flowbite';
import {CommunityControllerService} from '../../../../services/services/community-controller.service';
import {UserCommunityResponse} from '../../../../services/models/user-community-response';
import {PageResponseUserCommunityResponse} from '../../../../services/models/page-response-user-community-response';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {Router} from '@angular/router';
import {firstValueFrom} from 'rxjs';
import {RefreshService} from '../../../../services/fn/refresh-service/refresh-service';
import {ReportControllerService} from '../../../../services/services/report-controller.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ReportRequest} from '../../../../services/models/report-request';

@Component({
  selector: 'app-community',
  imports: [
    NgForOf,
    NgIf,
    NgStyle,
    NgClass,
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './community.component.html',
  styleUrl: './community.component.scss'
})
export class CommunityComponent implements OnInit {


  ngOnInit(): void {
    initFlowbite();
    this.loadAllUsers();
  }

  public page = 0;
  public size = 10;

  userHasProfilePicture = true;
  loadUsers = false;
  noUsersFound = false;
  isLoaded = false;
  isReportUserModalOpen = false;

  constructor(
    private communityService: CommunityControllerService,
    private reportService: ReportControllerService,
    private router: Router,
    private refreshService: RefreshService
  ) {
  }

  errorMessage: string = '';
  successMessage: string | null = null;
  toastVisible = false;

  selectedUserToReport: UserCommunityResponse | null = null;
  userCommunityResponse: PageResponseUserCommunityResponse = {};
  reportRequest: ReportRequest = {reason: null!, message: ''};

  allReportReasons: { id: number; reason: string }[] = [];
  friendRequestMapFromSender: { [key: number]: boolean } = {};
  friendRequestMapForReceiver: { [key: number]: boolean } = {};
  friendsMap: { [key: number]: boolean } = {};

  private loadAllUsers(query: string = "") {

    this.communityService.findAllUsers({query: query}).subscribe({
      next: (users) => {
        if (users.content?.length! === 0) {
          this.noUsersFound = true;
          return
        }
        console.log(this.friendRequestMapFromSender)
        this.noUsersFound = false;
        this.userCommunityResponse = users;

        this.userCommunityResponse.content?.forEach(user => {
          this.friendRequestExistsForSender(user.userId)
        })

        this.userCommunityResponse.content?.forEach(user => {
          this.friendRequestExistsForReceiver(user.userId!)
        })

        this.userCommunityResponse.content?.forEach(user => {
          this.areFriends(user.userId!)
        })

        this.loadUsers = true;
      }
    })

  }

  private async areFriends(userId: number) {
    const exists = await firstValueFrom(this.communityService.friendExistsForUser({userId}))
    this.friendsMap[userId] = exists;
    this.isLoaded = true;
    return exists;
  }

  private async friendRequestExistsForSender(userId: any) {
    const exists = await firstValueFrom(this.communityService.friendRequestExistsFromSender({userId}));
    this.friendRequestMapFromSender[userId] = exists;
    console.log(this.friendRequestMapFromSender);
    return exists;
  }

  private async friendRequestExistsForReceiver(userId: number) {
    const exists = await firstValueFrom(this.communityService.friendRequestExistsForReceiver({userId}))
    this.friendRequestMapForReceiver[userId] = exists;
    return exists;
  }

  getProfilePicture(user: UserCommunityResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return this.userHasProfilePicture;
  }

  sendFriendRequest(userId: number) {
    this.communityService.sendFriendRequest({userId}).subscribe({
      next: () => {
        console.log('friend request send')
        this.friendRequestMapFromSender[userId] = true;
      }
    })
  }

  cancelFriendRequest(userId: number) {
    this.communityService.cancelFriendRequest({userId}).subscribe({
      next: () => {
        this.friendRequestMapFromSender[userId] = false;
      }
    })
  }

  navigateToUser(userId: number | undefined) {
    this.router.navigate(['gamehub/user', userId]);
  }

  reportUser(userId: number) {
    this.errorMessage = '';
    if (this.reportRequest.reason === 6 && this.reportRequest.message === '') {
      this.errorMessage = 'Please write a reason for reporting';
      return;
    }
    if (this.reportRequest.reason !== null) {
      this.reportService.reportUser({userId, body: this.reportRequest}).subscribe({
        next: () => {
          this.isReportUserModalOpen = false;
          this.reportRequest = {
            reason: undefined,
            message: ''
          };
          this.showSuccess('User has been reported successfully');
        }
      })
    }
    else {
      this.errorMessage = 'Please select a reason before submitting';
    }

  }

  searchByUsername(value: string) {
    console.log(value);
    this.page = 0;
    this.userCommunityResponse = {}
    this.loadAllUsers(value);
  }

  acceptFriendRequest(userId: number) {
    this.communityService.acceptFriendRequest({userId}).subscribe({
      next: () => {
        this.friendsMap[userId!] = true;
        this.friendRequestMapForReceiver[userId!] = false;
        this.refreshService.triggerRefresh();
      }
    });
  }

  rejectFriendRequest(userId: number) {
    this.communityService.rejectFriendRequest({userId}).subscribe({
      next: () => {
        this.friendsMap[userId!] = false;
        this.friendRequestMapForReceiver[userId!] = false;
        this.refreshService.triggerRefresh();
      }
    })

  }

  openReportUserModal(user: UserCommunityResponse) {
    this.selectedUserToReport = user;
    this.isReportUserModalOpen = true;
    this.loadReportReasons();
  }

  closeReportModal() {
    this.selectedUserToReport = null;
    this.isReportUserModalOpen = false;
    this.errorMessage = '';
    this.reportRequest = {
      reason: undefined,
      message: ''
    };
  }

  private loadReportReasons() {
    this.reportService.getAllReportReasons().subscribe({
      next: (reasons) => {
        this.allReportReasons = reasons.map(r => ({
          id: r.id!,
          reason: r.reason!
        }));
      }
    })
  }

  showSuccess(message: string) {
    this.successMessage = message;

    setTimeout(() => this.toastVisible = true, 10);

    setTimeout(() => this.hideToast(), 3000);
  }

  hideToast() {
    this.toastVisible = false;

    setTimeout(() => this.successMessage = null, 500);
  }
}
