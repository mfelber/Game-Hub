import {Component, OnInit} from '@angular/core';
import {ReportRequest} from '../../../../services/models/report-request';
import {DatePipe, NgClass, NgStyle} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ReportUserModalComponent} from '../../components/report-user-modal/report-user-modal.component';
import {SearchBar} from '../../components/search-bar/search-bar';
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
import {HlmDropdownMenu, HlmDropdownMenuItem, HlmDropdownMenuTrigger} from '@spartan/dropdown-menu';
import {HlmButton} from '@spartan/button';
import {RegionResponse} from '../../../../services/models/region-response';
import {Platform} from '@angular/cdk/platform';
import {PlatformResponse} from '../../../../services/models/platform-response';
import {ToastService} from '../../../../services/ToastService/toast.service';
import {CountryResponse} from '../../../../services/models/country-response';

@Component({
  selector: 'app-find-players',
  imports: [
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

  filterOpen = false;

  filters = {
    country: '',
    lookingForTeammate: false,
    voiceChat: false,
    onlyOnline: false,
  }

  errorMessage: string = '';

  selectedUserToReport: UserCommunityResponse | null = null;
  reportRequest: ReportRequest = {reason: null!, message: ''};

  allCommunityGuidelines: { id: number; reason: string }[] = [];
  allCountries: string[] = [];


  userCommunityResponse: PageResponseUserCommunityResponse = {};
  filteredUsers: UserCommunityResponse[] = [];

  regions: RegionResponse[] = [];
  gamingPlatforms: PlatformResponse[] = [];

  pinnedCountries = ['Slovakia', 'Czechia', 'Poland', 'Germany', 'United States'];
  countryList: CountryResponse[] = [];
  extraCountries: string[] = [];
  selectedCountries: string[] = [];
  countryOpen = false;
  countrySearchQuery = '';

  constructor(
    private communityService: CommunityControllerService,
    private reportService: ReportControllerService,
    private countryService: CountryControllerService,
    private router: Router,
    private route: ActivatedRoute,
    private refreshService: RefreshService,
    private toastService: ToastService,
  ) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.page = Number(params['page'] ?? 1) - 1;

      this.filters.country = params['country'] ?? '';
      this.filters.lookingForTeammate = params['lookingForTeammate'] === 'true';
      this.filters.voiceChat = params['voiceChat'] === 'true';
      this.filters.onlyOnline = params['onlyOnline'] === 'true';

      this.loadAllUsers();
    });

    this.getCountries();
    this.getRegions();
    this.getGamingPlatforms();
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
          console.log(this.userCommunityResponse);

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
        this.allCountries = countries.map(c => c.countryName!)
        this.countryList = countries;
      }
    })
  }

  getRegions() {
    this.communityService.getAllRegions().subscribe({
      next: (regions) => {
        this.regions = regions;
      },
      error: error => {
        console.log(error);
        this.toastService.error('Error getting regions.');
      }
    })
  }

  getGamingPlatforms() {
    this.communityService.getAllGamingPlatforms().subscribe({
      next: (gamingPlatforms) => {
        this.gamingPlatforms = gamingPlatforms;
      },
      error: error => {
        console.log(error);
        this.toastService.error('Error getting gaming platforms.');
      }
    })
  }

  getProfilePicture(user: UserCommunityResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return this.userHasProfilePicture;
  }

  get visibleCountries() {
    return [...this.pinnedCountries, ...this.extraCountries];
  }

  getFlag(name: string) {
    return this.countryList.find(country => country.countryName === name)?.iconPath;
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

  get searchCountry(): CountryResponse[] {
    const query = this.countrySearchQuery.trim().toLowerCase();
    return this.countryList.filter(country =>
      country.countryName !== 'Not Selected' && !this.visibleCountries.includes(country.countryName!) &&
      country.countryName!.toLowerCase().includes(query));
  }

  toggleCountry(countryName: string) {
    if (this.selectedCountries.includes(countryName)) {
      this.selectedCountries = this.selectedCountries.filter(country => country !== countryName);
      this.extraCountries = this.extraCountries.filter(country => country !== countryName)
    } else {
      this.selectedCountries = [...this.selectedCountries, countryName];
    }
  }

  addCountry(name: string) {
    this.extraCountries = [...this.extraCountries, name];
    this.selectedCountries = [...this.selectedCountries, name];
    this.countrySearchQuery = '';
    this.countryOpen = false;
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

  handleReport(request: ReportRequest) {
    this.toastService.success('User has been reported successfully')
  }

  resetFilters() {
    this.filters = {
      country: '',
      lookingForTeammate: false,
      voiceChat: false,
      onlyOnline: false,
    };
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        country: null,
        lookingForTeammate: false,
        voiceChat: false,
        onlyOnline: false,
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
        onlyOnline: this.filters.onlyOnline || null,
      }
    })
  }
}
