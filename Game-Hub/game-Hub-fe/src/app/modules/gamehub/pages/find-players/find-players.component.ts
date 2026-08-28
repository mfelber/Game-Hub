import {Component, OnInit} from '@angular/core';
import {ReportRequest} from '../../../../services/models/report-request';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ReportUserModalComponent} from '../../components/report-user-modal/report-user-modal.component';
import {SearchBar} from '../../components/search-bar/search-bar';
import {initFlowbite} from 'flowbite';
import {CommunityControllerService} from '../../../../services/services/community-controller.service';
import {ReportControllerService} from '../../../../services/services/report-controller.service';
import {Router} from '@angular/router';
import {RefreshService} from '../../../../services/fn/refresh-service/refresh-service';
import {UserCommunityResponse} from '../../../../services/models/user-community-response';
import {PageResponseUserCommunityResponse} from '../../../../services/models/page-response-user-community-response';
import {firstValueFrom} from 'rxjs';

@Component({
  selector: 'app-find-players',
  imports: [
    NgForOf,
    NgIf,
    NgStyle,
    NgClass,
    ReactiveFormsModule,
    FormsModule,
    ReportUserModalComponent,
    SearchBar
  ],
  templateUrl: './find-players.component.html',
  styleUrl: './find-players.component.scss',
})
export class FindPlayersComponent implements OnInit {

  ngOnInit(): void {
    initFlowbite();
    this.loadAllUsers();
  }

  public page = 0;
  public size = 10;

  userHasProfilePicture = true;
  loadUsers = false;
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

  allCommunityGuidelines: { id: number; reason: string }[] = [];
  friendRequestMapFromSender: { [key: number]: boolean } = {};
  friendRequestMapForReceiver: { [key: number]: boolean } = {};
  friendsMap: { [key: number]: boolean } = {};

  private loadAllUsers(query: string = "") {

    this.communityService.findAllUsers({query: query}).subscribe({
      next: (users) => {
        console.log(users.content?.length!)

        console.log(this.friendRequestMapFromSender)
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

        this.isLoaded = true;

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

  searchByUsername(value: string) {
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
    this.loadCommunityGuidelines();
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

  private loadCommunityGuidelines() {
    this.reportService.getAllCommunityGuidelines().subscribe({
      next: (communityGuidelines) => {
        this.allCommunityGuidelines = communityGuidelines.map(r => ({
          id: r.id!,
          reason: r.communityGuideline!
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

  handleReport(request: ReportRequest) {
    this.showSuccess('User has been reported successfully');
  }

  resetFilters() {

  }
}
