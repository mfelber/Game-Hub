import {Component, OnInit} from '@angular/core';
import {ReportRequest} from '../../../../services/models/report-request';
import {DatePipe, NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ReportUserModalComponent} from '../../components/report-user-modal/report-user-modal.component';
import {SearchBar} from '../../components/search-bar/search-bar';
import {initFlowbite} from 'flowbite';
import {CommunityControllerService} from '../../../../services/services/community-controller.service';
import {ReportControllerService} from '../../../../services/services/report-controller.service';
import {ActivatedRoute, Router} from '@angular/router';
import {RefreshService} from '../../../../services/fn/refresh-service/refresh-service';
import {UserCommunityResponse} from '../../../../services/models/user-community-response';
import {PageResponseUserCommunityResponse} from '../../../../services/models/page-response-user-community-response';
import {firstValueFrom} from 'rxjs';
import {EmptyStateComponent} from '../../components/empty-state/empty-state.component';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {PaginationComponent} from '../../components/pagination/pagination.component';

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
    SearchBar,
    EmptyStateComponent,
    UserActionsComponent,
    PaginationComponent
  ],
  templateUrl: './find-players.component.html',
  styleUrl: './find-players.component.scss',
})
export class FindPlayersComponent implements OnInit {

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.page = Number(params['page'] ?? 1) - 1;

      this.loadAllUsers();
    });
    initFlowbite();
  }

  public page = 0;
  public size = 10;
  searchQuery = '';

  userHasProfilePicture = true;
  isLoading = true;
  isReportUserModalOpen = false;

  constructor(
    private communityService: CommunityControllerService,
    private reportService: ReportControllerService,
    private router: Router,
    private route: ActivatedRoute,
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

  filteredUsers: UserCommunityResponse[] = [];

  private loadAllUsers(query: string = "") {
    this.communityService.findAllUsers({
      page: this.page,
      size: this.size,
      query: query}).subscribe({
        next: (users) => {
          this.userCommunityResponse = users;
          this.filteredUsers = [...(users.content || [])];
          this.isLoading = false;

        }, error: error => {
          console.log(error);
          this.isLoading = false;
      }
      }
    )

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
        this.isLoading = false;
        this.loadAllUsers(this.searchQuery);
      }
    })
  }

  cancelFriendRequest(userId: number) {
    this.communityService.cancelFriendRequest({userId}).subscribe({
      next: () => {
        this.isLoading = false;
        this.loadAllUsers(this.searchQuery);
      }
    })
  }

  navigateToUser(userId: number | undefined) {
    this.router.navigate(['gamehub/user', userId]);
  }

  searchByUsername(value: string) {
    this.page = 0;
    this.searchQuery = value;
    this.userCommunityResponse = {}
    this.loadAllUsers(value);
  }

  acceptFriendRequest(userId: number) {
    this.communityService.acceptFriendRequest({userId}).subscribe({
      next: () => {
        this.refreshService.triggerRefresh();
        this.loadAllUsers(this.searchQuery);
      }
    });
  }

  rejectFriendRequest(userId: number) {
    this.communityService.rejectFriendRequest({userId}).subscribe({
      next: () => {
        this.refreshService.triggerRefresh();
        this.loadAllUsers(this.searchQuery);
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

  changePage(page: number) {
    this.page = page;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: page + 1
      },
      queryParamsHandling: 'merge'
    });
  }
}
