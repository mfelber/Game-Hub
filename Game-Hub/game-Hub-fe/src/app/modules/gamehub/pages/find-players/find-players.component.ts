import {Component, OnInit} from '@angular/core';
import {ReportRequest} from '../../../../services/models/report-request';
import {DatePipe, NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ReportUserModalComponent} from '../../components/report-user-modal/report-user-modal.component';
import {SearchBar} from '../../components/search-bar/search-bar';
import {Dropdown, initFlowbite} from 'flowbite';
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
import {LoadingComponent} from '../../components/loading/loading.component';
import {CountryControllerService} from '../../../../services/services/country-controller.service';

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
    PaginationComponent,
    LoadingComponent
  ],
  templateUrl: './find-players.component.html',
  styleUrl: './find-players.component.scss',
})
export class FindPlayersComponent implements OnInit {


  public page = 0;
  public size = 10;
  searchQuery = '';

  userHasProfilePicture = true;
  isLoading = false;
  isReportUserModalOpen = false;

  constructor(
    private communityService: CommunityControllerService,
    private reportService: ReportControllerService,
    private countryService: CountryControllerService,
    private router: Router,
    private route: ActivatedRoute,
    private refreshService: RefreshService
  ) {
  }

  filters = {
    country: '',
    lookingForTeammate: false,
    voiceChat: false
  }

  errorMessage: string = '';
  successMessage: string | null = null;
  toastVisible = false;

  selectedUserToReport: UserCommunityResponse | null = null;
  reportRequest: ReportRequest = {reason: null!, message: ''};

  allCommunityGuidelines: { id: number; reason: string }[] = [];
  allCountries: string[] = [];


  userCommunityResponse: PageResponseUserCommunityResponse = {};
  filteredUsers: UserCommunityResponse[] = [];

  ngOnInit(): void {
    initFlowbite();
    this.route.queryParams.subscribe(params => {
      this.page = Number(params['page'] ?? 1) - 1;

      this.filters.country = params['country'] ?? '';
      this.filters.lookingForTeammate = params['lookingForTeammate'] === 'true';
      this.filters.voiceChat = params['voiceChat'] === 'true';

      this.loadAllUsers();
    });

    this.getCountries();

  }

  private loadAllUsers(query: string = "") {
    this.isLoading = true;
    this.communityService.findAllUsers({
      page: this.page,
      size: this.size,
      query: query
    }).subscribe({
        next: (users) => {
          this.userCommunityResponse = users;
          this.filteredUsers = [...(users.content || [])];
          this.isLoading = false;

        }, error: error => {
          console.log(error);
          this.isLoading = true;
        }
      }
    )

  }

  getCountries() {
    this.countryService.getAllCountries().subscribe({
      next: (countries) => {
        this.allCountries = countries.map(c => c.name!)
      }
    })
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
    this.filters = {
      country: '',
      lookingForTeammate: false,
      voiceChat: false,
    };
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        country: null,
        lookingForTeammate: false,
        voiceChat: false
      },
      queryParamsHandling: 'merge'
    })
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

  changeFilter() {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        country: this.filters.country || null,
        lookingForTeammate: this.filters.lookingForTeammate || null,
        voiceChat: this.filters.voiceChat || null,
      }
    })
  }

  closeDropdown() {
    const dropdownCountryElement = document.getElementById('countryDropdown');
    const buttonElement = document.getElementById('countryDropdownButton');

    if (dropdownCountryElement && buttonElement) {
      const dropdown = new Dropdown(dropdownCountryElement, buttonElement);
      dropdown.hide();
    }
  }
}
