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
import {EmptyStateComponent} from '../../components/empty-state/empty-state.component';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {PaginationComponent} from '../../components/pagination/pagination.component';
import {LoadingComponent} from '../../components/loading/loading.component';
import {CountryControllerService} from '../../../../services/services/country-controller.service';
import {RegionResponse} from '../../../../services/models/region-response';
import {PlatformResponse} from '../../../../services/models/platform-response';
import {ToastService} from '../../../../services/ToastService/toast.service';
import {CountryResponse} from '../../../../services/models/country-response';
import {MicrophoneUsageResponse} from '../../../../services/models/microphone-usage-response';
import {HlmTabs, HlmTabsContent, HlmTabsList, HlmTabsTrigger} from '@spartan/tabs';
import {LfgPostsComponent} from '../community/lfg-posts/lfg-posts.component';

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
    LoadingComponent,
    DatePipe,
    HlmTabs,
    HlmTabsList,
    HlmTabsTrigger,
    HlmTabsContent,
    LfgPostsComponent
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
    onlyOnline: false,
  }

  errorMessage: string = '';

  selectedUserToReport: UserCommunityResponse | null = null;
  reportRequest: ReportRequest = {reason: null!, message: ''};

  allCommunityGuidelines: { id: number; reason: string }[] = [];
  allCountries: string[] = [];

  activeTab = 'find-players';

  userCommunityResponse: PageResponseUserCommunityResponse = {};
  filteredUsers: UserCommunityResponse[] = [];

  regions: RegionResponse[] = [];
  gamingPlatforms: PlatformResponse[] = [];
  voiceChatOptions: MicrophoneUsageResponse[] = [];

  pinnedCountries = ['SK', 'CZ', 'PL', 'DE', 'US'];
  countryList: CountryResponse[] = [];

  extraCountries: string[] = [];
  selectedCountries: string[] = [];
  selectedRegions: string[] = [];
  selectedPlatforms: string[] = [];
  selectedVoiceChatOptions: string[] = [];

  countryOpen = false;
  countrySearchQuery = '';

  private lastFiltersKey = '';

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
      this.activeTab = params['tab'] === 'lfg' ? 'lfg' : 'find-players';

      this.selectedCountries = params['countries']?.split(',') ?? [];
      this.selectedRegions = this.selectedCountries.length > 0 ? [] : params['region']?.split(',') ?? [];


      this.selectedPlatforms = params['platforms']?.split(',') ?? [];
      this.selectedVoiceChatOptions = params['voice']?.split(',') ?? [];
      this.filters.onlyOnline = params['onlyOnline'] === 'true';

      this.extraCountries = this.selectedCountries.filter(country => !this.pinnedCountries.includes(country));

      const filterKey = [params['page'], params['countries'], params['region'], params['platforms'], params['voice'], params['onlyOnline']].join('|');
      if (this.activeTab === 'find-players' && filterKey !== this.lastFiltersKey) {
        this.lastFiltersKey = filterKey;
        this.loadAllUsers();
      }
    });

    this.getCountries();
    this.getRegions();
    this.getGamingPlatforms();
    this.getMicrophoneUsages();

  }

  private loadAllUsers(query: string = "") {
    this.isLoading = true;
    this.communityService.findAllUsers({
      page: this.page,
      size: this.size,
      query: query,
      countries: this.selectedCountries as any,
      regions: this.selectedRegions as any,
      platforms: this.selectedPlatforms as any,
      voice: this.selectedVoiceChatOptions as any,
      onlyOnline: this.filters.onlyOnline,
    }).subscribe({
        next: (users) => {
          this.userCommunityResponse = users;
          this.filteredUsers = [...(users.content || [])];
          this.isLoading = false;

        }, error: error => {
          console.log(error);
          this.toastService.error("Error with loading players");
          this.isLoading = false;
        }
      }
    )

  }

  getCountries() {
    this.countryService.getAllCountries().subscribe({
      next: (countries) => {
        this.allCountries = countries.map(c => c.countryName!)
        this.countryList = countries;
      },
      error: error => {
        console.log(error);
        this.toastService.error('Error getting countries');
      }
    })
  }

  getMicrophoneUsages() {
    this.communityService.getAllMicrophoneUsages().subscribe({
      next: (microphoneUsage) => {
        this.voiceChatOptions = microphoneUsage;
      },
      error: error => {
        console.log(error);
        this.toastService.error('Error getting voice chat option');
      }
    })
  }

  getCountry(countryName: string) {
    return this.countryList.find(country => country.name === countryName);
  }

  getRegion(regionName: string) {
    return this.regions.find(region => region.enumName === regionName);
  }

  getPlatform(platformName: string) {
    return this.gamingPlatforms.find(platform => platform.enumName === platformName);
  }

  getVoiceChatOption(voiceChatOption: string) {
    return this.voiceChatOptions.find(voiceChat => voiceChat.enumName === voiceChatOption);
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

  get hasActiveFilters(): boolean {
    return this.selectedCountries.length > 0 || this.selectedRegions.length > 0 || this.selectedPlatforms.length > 0 || this.selectedVoiceChatOptions.length > 0;
  }

  get activeFiltersCount(): number {
    return this.activeFilters.length;
  }

  getFlag(name: string) {
    return this.countryList.find(country => country.name === name)?.iconPath;
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
      country.countryName !== 'Not Selected' && !this.visibleCountries.includes(country.name!) &&
      country.countryName!.toLowerCase().includes(query));
  }

  toggleCountry(countryName: string) {
    if (this.selectedCountries.includes(countryName)) {
      this.selectedCountries = this.selectedCountries.filter(country => country !== countryName);
      this.extraCountries = this.extraCountries.filter(country => country !== countryName)
    } else {
      this.selectedRegions = [];
      this.selectedCountries = [...this.selectedCountries, countryName];
    }
  }

  toggleRegion(regionName: string) {
    if (this.selectedRegions.includes(regionName)) {
      this.selectedRegions = this.selectedRegions.filter(region => region !== regionName);
    } else {
      this.selectedRegions = [regionName]
      this.extraCountries = [];
      this.selectedCountries = [];
      this.countryOpen = false;
      this.countrySearchQuery = '';
    }
  }

  togglePlatform(platformName: string) {
    if (this.selectedPlatforms.includes(platformName)) {
      this.selectedPlatforms = this.selectedPlatforms.filter(platform => platform !== platformName);
    } else {
      this.selectedPlatforms = [...this.selectedPlatforms, platformName]
    }
  }

  toggleVoiceChat(voiceChatOption: string) {
    if (this.selectedVoiceChatOptions.includes(voiceChatOption)) {
      this.selectedVoiceChatOptions = this.selectedVoiceChatOptions.filter(voice => voice !== voiceChatOption);
    } else {
      this.selectedVoiceChatOptions = [...this.selectedVoiceChatOptions, voiceChatOption];
    }
  }

  get regionDisabled(): boolean {
    return this.selectedCountries.length > 0;
  }

  get countryDisabled(): boolean {
    return this.selectedRegions.length > 0;
  }

  addCountry(name: string) {
    this.selectedRegions = [];
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

    this.filters.onlyOnline = false;
    this.selectedCountries = [];
    this.extraCountries = [];
    this.selectedRegions = [];
    this.selectedPlatforms = [];
    this.selectedVoiceChatOptions = [];
    this.countrySearchQuery = '';
    this.countryOpen = false;

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        countries: null,
        region: null,
        platforms: null,
        voice: null,
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
    document.getElementById('page-container')?.scrollTo({top: 0, behavior: 'smooth'});
  }

  changeFilter() {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        countries: this.selectedCountries.join(',') || null,
        region: this.selectedRegions.join(',') || null,
        platforms: this.selectedPlatforms.join(',') || null,
        voice: this.selectedVoiceChatOptions.join(',') || null,
        onlyOnline: this.filters.onlyOnline || false,
      }
    })
  }

  openFindPlayersFilter() {
    this.filterOpen = !this.filterOpen
    this.countrySearchQuery = '';
    this.countryOpen = false;
  }

  get activeFilters(): { label: string; remove: () => void }[] {
    return [
      ...this.selectedCountries.map(country => ({
        label: this.getCountry(country)?.countryName ?? country,
        remove: () => this.toggleCountry(country)
      })),
      ...this.selectedRegions.map(region => ({
        label: this.getRegion(region)?.name ?? region,
        remove: () => this.toggleRegion(region)
      })),
      ...this.selectedPlatforms.map(platform => ({
        label: this.getPlatform(platform)?.name ?? platform,
        remove: () => this.togglePlatform(platform)
      })),
      ...this.selectedVoiceChatOptions.map(voiceChat => ({
        label: this.getVoiceChatOption(voiceChat)?.name ?? voiceChat, remove: () => this.toggleVoiceChat(voiceChat)
      }))
    ];
  }

  applyFilters() {
    this.changeFilter();
  }

  removeFilter(filter: { label: string; remove: () => void }) {
    filter.remove();
    this.changeFilter();
  }

  onTabChange(tab: string | undefined) {
    if (!tab || tab === this.activeTab) {
      return;
    }
    this.searchQuery = '';
    this.lastFiltersKey = '';
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        tab: tab === 'lfg' ? 'lfg' : null,
        page: null,
        countries: null,
        region: null,
        platforms: null,
        voice: null,
        onlyOnline: false,
      },
      queryParamsHandling: 'merge'
    });
  }
}
