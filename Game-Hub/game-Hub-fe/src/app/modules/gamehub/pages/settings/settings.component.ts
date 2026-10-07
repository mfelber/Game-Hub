import {Component, OnInit, ViewChild} from '@angular/core';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {HlmTabs, HlmTabsContent, HlmTabsList, HlmTabsTrigger} from '@spartan/tabs';
import {PrivacySettingsResponse} from '../../../../services/models/privacy-settings-response';
import {StoreSettingsResponse} from '../../../../services/models/store-settings-response';
import {CommunitySettingsResponse} from '../../../../services/models/community-settings-response';
import {HlmDropdownMenu, HlmDropdownMenuItem, HlmDropdownMenuTrigger} from '@spartan/dropdown-menu';
import {HlmSwitch} from '@spartan/switch';
import {FormsModule} from '@angular/forms';
import {HlmTooltip} from '@spartan/tooltip';
import {HlmInputGroup, HlmInputGroupButton, HlmInputGroupInput} from '@spartan/input-group';
import {BrnTabs} from '@spartan-ng/brain/tabs';
import {SettingsControllerService} from '../../../../services/services/settings-controller.service';
import {UserSettingsUpdateRequests} from '../../../../services/models/user-settings-update-requests';
import {
  HlmDialog,
  HlmDialogClose,
  HlmDialogContent,
  HlmDialogDescription, HlmDialogFooter, HlmDialogHeader,
  HlmDialogPortal, HlmDialogTitle
} from '@spartan/dialog';
import {NgClass, NgStyle} from '@angular/common';
import {UserSettingsResponse} from '../../../../services/models/user-settings-response';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {HlmTextarea} from '@spartan/textarea';
import {UserUpdateRequest} from '../../../../services/models/user-update-request';
import {CountryControllerService} from '../../../../services/services/country-controller.service';
import {Router} from '@angular/router';
import {ToastService} from '../../../../services/ToastService/toast.service';
import {CardColorResponse} from '../../../../services/models/card-color-response';
import {CardColorControllerService} from '../../../../services/services/card-color-controller.service';
import {concatMap, Observable, of} from 'rxjs';
import {RefreshService} from '../../../../services/fn/refresh-service/refresh-service';
import {LoadingComponent} from '../../components/loading/loading.component';
import {StoreControllerService} from '../../../../services/services/store-controller.service';
import {SearchBar} from '../../components/search-bar/search-bar';
import {UserLibraryResponse} from '../../../../services/models/user-library-response';

@Component({
  selector: 'app-settings',
  imports: [
    UserActionsComponent,
    HlmTabs,
    HlmTabsList,
    HlmTabsTrigger,
    HlmTabsContent,
    HlmDropdownMenuTrigger,
    HlmDropdownMenu,
    HlmDropdownMenuItem,
    HlmSwitch,
    FormsModule,
    HlmTooltip,
    HlmInputGroupButton,
    HlmDialog,
    HlmDialogContent,
    HlmDialogPortal,
    HlmDialogClose,
    HlmDialogDescription,
    HlmDialogFooter,
    HlmDialogHeader,
    HlmDialogTitle,
    NgClass,
    HlmInputGroup,
    HlmInputGroupInput,
    NgStyle,
    HlmTextarea,
    LoadingComponent,
    SearchBar
  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss',
})
export class SettingsComponent implements OnInit {

  @ViewChild(BrnTabs)
  tabs!: BrnTabs;

  @ViewChild('warningDialog')
  warningDialog!: HlmDialog;

  searchedQuery = '';

  originalPrivacySettingsResponse: PrivacySettingsResponse[] = [];
  privacySettingsResponse: PrivacySettingsResponse[] = [];

  originalStoreSettingsResponse: StoreSettingsResponse[] = [];
  storeSettingsResponse: StoreSettingsResponse[] = [];

  originalCommunitySettingsResponse: CommunitySettingsResponse[] = [];
  communitySettingsResponse: CommunitySettingsResponse[] = [];

  response: UserSettingsResponse = {};

  userRequest: UserUpdateRequest = {};

  activeTab: string = 'profile';

  allCountries: { name: string; iconPath: string, countryName: string }[] = [];
  selectedCountry = this.response.country;

  predefinedBanners = [1, 2, 3, 4];
  cardColorsResponse: CardColorResponse[] = [];

  genreResponse: any[] = [];

  newCustomColor = '';
  selectedColorCode: string = '';

  originalBio: string = '';

  originalFavoriteGame: UserLibraryResponse | null = null;
  selectedFavoriteGame: UserLibraryResponse | null = null;

  originalColorId: number | null = null;
  selectedColorId: number | null = null;

  profileBanner: File | null = null;
  profilePicture: File | null = null;

  previewBanner: string | undefined;
  previewProfilePic: string | undefined;

  originalBannerId: number | null = null;
  selectedBannerId: number | null = null;

  editBasicInfo = false;
  editAppearance = false
  editAboutMe = false;
  editGenres = false;
  editFavoriteGame = false;
  addCustomColor = false;

  selectedGenres: Set<number> = new Set<number>();
  originalFavoriteGenresIds: number[] = [];

  isSavingAppearance = false;
  isLoading = false;

  constructor(
    private toastService: ToastService,
    private refreshService: RefreshService,
    private userService: UserProfileControllerService,
    private settingsService: SettingsControllerService,
    private countryService: CountryControllerService,
    private cardService: CardColorControllerService,
    private gameService: StoreControllerService,
    private router: Router
  ) {
  }

  ngOnInit() {
    console.log('Initial tab:', this.activeTab);
    this.getCountries();
    this.loadProfile();
    this.loadColorsForCard();
    this.getGenres();
    this.userService.getUserSettings().subscribe({
        next: (userSettings) => {
          console.log(userSettings);
          this.response = userSettings;
          this.privacySettingsResponse = userSettings.privacySettingsResponse ?? []
          this.originalPrivacySettingsResponse = userSettings.privacySettingsResponse?.map(setting => ({
            ...setting,
          })) ?? [];

          this.storeSettingsResponse = userSettings.storeSettingsResponse ?? [];
          this.originalStoreSettingsResponse = userSettings.storeSettingsResponse?.map(setting => ({
            ...setting
          })) ?? [];


          this.communitySettingsResponse = userSettings.communitySettingsResponse ?? [];
          this.originalCommunitySettingsResponse = userSettings.communitySettingsResponse?.map(setting => ({
            ...setting,
          })) ?? [];
        }
      }
    )
  }

  loadProfile() {
    this.isLoading = true;
    this.userService.getUserSettings().subscribe({
      next: (profile) => {
        this.response = profile;
        this.originalColorId = profile.cardColor?.id!;
        this.selectedColorId = profile.cardColor?.id!;
        this.userRequest = {
          email: profile.email,
          firstName: profile.firstName,
          lastName: profile.lastName,
          username: profile.userName,
          country: profile.country?.name as undefined,
          cardColorId: profile.cardColor?.id!,
        }

        this.originalBannerId = this.getSelectedBannerId(profile.predefinedBannerPath);
        this.selectedBannerId = this.originalBannerId;

        this.originalBio = profile.bio ?? '';

        this.originalFavoriteGenresIds = this.response.favoriteGenres?.map(g => g.id) || [];
        this.selectedGenres = new Set(this.originalFavoriteGenresIds);

        this.originalFavoriteGame = this.response.favoriteGame ?? null;
        this.selectedFavoriteGame = this.originalFavoriteGame;

        this.isSavingAppearance = false;
        this.isLoading = false;
      },  error: () => {
        this.toastService.error('Error loading profile profile');
        this.isSavingAppearance = true;
        this.isLoading = true;
      }
    })
  }

  private getGenres() {
    this.gameService.getAllGenres().subscribe({
      next: (genres) => {
        this.genreResponse = genres;
        console.log(this.genreResponse);
      }
    })
  }

  loadColorsForCard() {
    this.cardService.getColors().subscribe({
      next: (colors) => {
        this.cardColorsResponse = colors;
      }
    })
  }

  savePrivacySettings() {
    const request: UserSettingsUpdateRequests = {
      communitySettingsIds: [],
      privacySettingsIds: this.privacySettingsResponse.map(setting => ({
        settingId: setting.settingId!,
        access: this.getAccessEnum(setting.access),
      })),
      storeSettingsIds: [],
    }

    this.settingsService.updateUserSettings({body: request}).subscribe({
      next: response => {
        this.originalPrivacySettingsResponse = this.privacySettingsResponse.map(setting => ({
          settingId: setting.settingId,
          access: setting.access
        }));

        this.warningDialog.close();

      },
      error: error => {
        console.log(error);
      }
    })
  }

  saveCommunitySettings() {
    const request: UserSettingsUpdateRequests = {
      communitySettingsIds: this.communitySettingsResponse.map(setting => ({
        settingId: setting.settingId!,
        access: this.getAccessEnum(setting.access),
      })),
      privacySettingsIds: [],
      storeSettingsIds: [],
    }

    this.settingsService.updateUserSettings({body: request}).subscribe({
      next: response => {
        this.originalCommunitySettingsResponse = this.communitySettingsResponse.map(setting => ({
          settingId: setting.settingId,
          access: setting.access
        }))

        this.warningDialog.close();
      },
      error: error => {
        console.log(error);
      }
    })
  }

  saveStoreSettings() {
    const request: UserSettingsUpdateRequests = {
      communitySettingsIds: [],
      privacySettingsIds: [],
      storeSettingsIds: this.storeSettingsResponse.map(setting => ({
        settingId: setting.settingId!,
        disabled: setting.disabled
      }))
    }
    console.log(request);
    this.settingsService.updateUserSettings({body: request}).subscribe({
      next: response => {
        this.originalStoreSettingsResponse = this.storeSettingsResponse.map(setting => ({
          ...setting,
        }))
        this.warningDialog.close();
      },
      error: error => {
        console.log(error);
      }
    })
  }

  hasProfileAppearanceChanges() {
    if (this.originalColorId !== this.selectedColorId) {
      return true;
    }

    if (this.previewProfilePic) {
      return true;
    }

    if (this.previewBanner) {
      return true;
    }

    if (this.originalBannerId !== this.selectedBannerId) {
      console.log(this.originalBannerId, this.selectedBannerId);
      return true;
    }

    return false;
  }

  isPrivacySettingsChanged(setting: PrivacySettingsResponse): boolean {
    const originalSettings = this.originalPrivacySettingsResponse.find(
      original => original.settingId === setting.settingId
    );

    return originalSettings?.access !== setting.access;
  }

  hasPrivacyChanges(): boolean {
    return this.privacySettingsResponse.some(setting =>
      this.isPrivacySettingsChanged(setting)
    );
  }

  isCommunitySettingsChanged(setting: CommunitySettingsResponse): boolean {
    const originalSettings = this.originalCommunitySettingsResponse.find(
      original => original.settingId === setting.settingId
    );

    return originalSettings?.access !== setting.access;
  }

  hasCommunityChanges() {
    return this.communitySettingsResponse.some(setting => this.isCommunitySettingsChanged(setting));
  }

  isStoreSettingsChanged(setting: StoreSettingsResponse): boolean {
    const originalSettings = this.originalStoreSettingsResponse.find(
      original => original.settingId === setting.settingId
    );

    return originalSettings?.disabled !== setting.disabled;
  }

  hasStoreChanges(): boolean {
    return this.storeSettingsResponse.some(setting => this.isStoreSettingsChanged(setting));
  }

  hasProfileInfoChanges(): boolean {
    const hasEmptyField =
      !this.userRequest.username?.trim() ||
      !this.userRequest.firstName?.trim() ||
      !this.userRequest.lastName?.trim() ||
      !this.userRequest.email?.trim() ||
      !this.userRequest.country?.trim();

    if (hasEmptyField) {
      return false;
    }

    return (
      this.userRequest.username !== this.response.userName ||
      this.userRequest.firstName !== this.response.firstName ||
      this.userRequest.lastName !== this.response.lastName ||
      this.userRequest.email !== this.response.email ||
      this.userRequest.country !== this.response.country?.name
    );
  }

  hasUnsavedChanges(): boolean {
    if (this.activeTab === 'privacy') {
      return this.hasPrivacyChanges();
    }

    if (this.activeTab === 'community') {
      return this.hasCommunityChanges();
    }

    if (this.activeTab === 'store') {
      return this.hasStoreChanges();
    }

    return false;
  }

  saveChanges() {
    if (this.activeTab === 'privacy') {
      this.savePrivacySettings();
    }

    if (this.activeTab === 'community') {
      this.saveCommunitySettings();
    }

    if (this.activeTab === 'store') {
      this.saveStoreSettings();
    }
  }

  discardChanges() {
    if (this.activeTab === 'privacy') {
      this.privacySettingsResponse = this.originalPrivacySettingsResponse.map(setting => ({
        ...setting
      }))
    }
    if (this.activeTab === 'community') {
      this.communitySettingsResponse = this.originalCommunitySettingsResponse.map(setting => ({
        ...setting
      }))
    }

    if (this.activeTab === 'store') {
      this.storeSettingsResponse = this.originalStoreSettingsResponse.map(setting => ({
        ...setting
      }))
    }
  }

  onTabChange(tab: string | undefined) {
    if (tab) {
      if (this.hasUnsavedChanges()) {
        this.tabs.setActiveTab(this.activeTab);
        setTimeout(() => {
          this.warningDialog.open();
        });
        return;
      }
      this.activeTab = tab;
      this.tabs.setActiveTab(tab);
      console.log(this.activeTab);
    }
  }

  private getAccessEnum(
    access: string | undefined
  ): 'EVERYONE' | 'FRIENDS' | 'NO_ONE' {
    if (access === 'Everyone') {
      return 'EVERYONE';
    }

    if (access === 'Friends') {
      return 'FRIENDS';
    }

    return 'NO_ONE';
  }

  getPegiColor(pegiRatingName: string | undefined): string {
    switch (pegiRatingName) {
      case 'PEGI 3':
        return 'text-[#a4c500cc]';
      case 'PEGI 7':
        return 'text-[#a4c500cc]';
      case 'PEGI 12':
        return 'text-[#f69f00cc]';
      case 'PEGI 16':
        return 'text-[#f69f00cc]';
      case 'PEGI 18':
        return 'text-[#e20217cc]';

      default:
        return 'text-white';
    }
  }

  get unsavedChangesTitle(): string {
    return `${this.activeTab}`;
  }

  getGameImageCover(game: UserSettingsResponse): string {
    if (game.favoriteGame) {
      return 'data:image/jpeg;base64,' + game.favoriteGame;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  getGameImageCoverLibrary(game: UserLibraryResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  getBanner(user: UserPrivateResponse) {
    if (user.bannerImage) {
      return 'data:image/jpeg;base64,' + user.bannerImage;
    }
    return user.predefinedBannerPath;
  }

  private getCountries() {
    this.countryService.getAllCountries().subscribe({
      next: (country) => {
        this.allCountries = country.map(country => ({
          name: country.name!,
          iconPath: country.iconPath!,
          countryName: country.countryName!,
        }));
      }
    })
  }

  selectCountry(country: any) {
    this.selectedCountry = country;
    this.userRequest.country = country.name;
  }

  getSelectedCountry() {
    return this.allCountries.find(
      country => country.name === this.userRequest.country
    );
  }

  getSelectedBannerId(path: string | undefined):number | null {
    if (!path) {
      return null;
    }

    const match = path.match(/banner_(\d+)\.jpg$/);
    return match ? Number(match[1]) : null;
    }

  saveBasicInfo() {
    const emailChanged = this.response.email !== this.userRequest.email;

    this.userService.updateUserProfile({
      body: this.userRequest,
    }).subscribe({
      next: (profile) => {
        if (emailChanged) {
          this.toastService.success('Profile updated successfully')
          localStorage.clear();
          this.router.navigate(['login']);
          return;
        }

        this.loadProfile();
        this.toastService.success('Profile updated successfully');
        this.editBasicInfo = false;

      },
      error: (err) => {
        console.error('updateUserProfile failed:', err);
        this.toastService.error('Failed to update profile');
      }
    })
  }


  cancelEditBasicInfo() {
    this.userRequest = {
      username: this.response.userName,
      firstName: this.response.firstName,
      lastName: this.response.lastName,
      email: this.response.email,
      country: this.response.country?.name as undefined
    };

    this.editBasicInfo = false;
  }

  cancelAppearance() {
    this.editAppearance = false;
    this.addCustomColor = false;

    this.previewBanner = undefined;
    this.profileBanner = null;
    this.selectedBannerId = this.originalBannerId;

    this.previewProfilePic = undefined;
    this.profilePicture = null;

    this.selectedColorId = this.originalColorId;

  }

  selectCustomColor() {
    console.log(this.newCustomColor);
  }

  hasSelectedFavoriteGame():boolean {
    return this.selectedFavoriteGame !== null;
}

  onBannerSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    this.profileBanner = input.files![0];

    if (this.profileBanner) {
      const reader = new FileReader();
      this.selectedBannerId = null;
      reader.onloadend = () => {
        this.previewBanner = reader.result as string;
      }
      reader.readAsDataURL(this.profileBanner);
    }
  }

  removeSelectedBanner() {
    this.previewBanner = undefined;
    this.selectedBannerId = this.originalBannerId;
    this.profileBanner = null;
  }

  selectedBanner(bannerId: number): void {
    this.selectedBannerId = bannerId;
    this.profileBanner = null;

    if (bannerId === this.originalBannerId) {
      this.previewBanner = undefined;
      return;
    }

    this.previewBanner = `assets/banners/banner_${bannerId}.jpg`;
  }

  getProfilePicture(user: UserPrivateResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return user.userProfilePicture;
  }

  onProfilePicSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    this.profilePicture = input.files![0]
    if (this.profilePicture) {
      const reader = new FileReader();
      reader.onload = () => {
        this.previewProfilePic = reader.result as string;
      }
      reader.readAsDataURL(this.profilePicture);
    }
  }

  removeSelectedProfilePicture() {
    this.previewProfilePic = undefined;
    this.profilePicture = null;
  }

  selectColor(id: number, colorCode: string) {
    console.log('Selected Color:', id, colorCode);
    this.selectedColorCode = colorCode;
    this.selectedColorId = id;
    this.userRequest.cardColorId = id;
  }

  resetColor() {
    this.selectedColorId = this.originalColorId;
  }

  saveAppearance() {
    console.log(this.userRequest);
    console.log(this.previewBanner);
    let request$: Observable<any> = of(null);

    if (this.originalColorId !== this.selectedColorId) {
      request$ = request$.pipe(
        concatMap(() => this.saveProfileColor())
      );
    }

    if (this.previewProfilePic) {
      request$ = request$.pipe(
        concatMap(() => this.saveProfilePicture())
      );
    }

    if (this.selectedBannerId !== null) {
      request$ = request$.pipe(
        concatMap(() => this.savePredefinedBanner())
      );
    } else if (this.previewBanner) {
      request$ = request$.pipe(
        concatMap(() => this.saveBanner())
      );
    }

    this.isSavingAppearance = true;
    request$.subscribe({
      next: () => {
        this.toastService.success('Profile appearance updated successfully');
        this.refreshService.triggerRefresh();
        this.editAppearance = false;
        this.previewBanner = undefined;
        this.previewProfilePic = undefined;
        this.profileBanner = null;
        this.profilePicture = null;
        this.loadProfile();
      },
      error: (err) => {
        console.error('updateUserProfile failed:', err);
        this.isSavingAppearance = false;
        this.toastService.error('Failed to update profile appearance');
      }
    })
  }


  private saveProfileColor(): Observable<any> {
    return this.userService.updateUserProfile({
      body: this.userRequest,
    })
  }

  private saveProfilePicture(): Observable<any> {
    return this.userService.uploadProfileImage({
      body: {
        file: this.profilePicture!
      }
    })
  }

  private saveBanner(): Observable<any> {
    return this.userService.uploadBannerImage({
      body : {
        file: this.profileBanner!
      }
    })
  }

  private savePredefinedBanner(): Observable<any> {
    const bannerPath = "/assets/banners/banner_" + this.selectedBannerId + ".jpg";
    return this.userService.setPredefinedBanner({
      body: {
        bannerPath
      }
    })
  }

  cancelEditAboutMe() {
    this.editAboutMe = false;
    this.response.bio = this.originalBio;
  }

  hasAboutMeChanges() {
    return this.response.bio !== this.originalBio;
  }

  saveAboutMe() {
    this.userService.updateBio({
      body: {
        bio: this.response.bio,
      }
    }).subscribe({
      next: () => {
        this.editAboutMe = false;
        this.toastService.success('About me has been updated');
      },
      error: (err) => {
        this.toastService.error('Failed to update About me');
      }
    })
  }

  cancelEditFavoriteGenres() {
    this.selectedGenres = new Set(this.originalFavoriteGenresIds);
    this.editGenres = false;
  }

  selectedGenre(id: number): void {
    const genres = new Set(this.selectedGenres);

    if (genres.has(id)) {
      genres.delete(id);
    } else {
      genres.add(id);
    }

    this.selectedGenres = genres;
  }

  hasGenreChanges() {
    const original = new Set(this.originalFavoriteGenresIds);

    if (original.size !== this.selectedGenres.size) {
      return true;
    }

    return [...original].some(id => !this.selectedGenres.has(id));
  }

  saveFavoriteGenres() {
    const selectedGenres= [...new Set([...this.selectedGenres])];

    this.userService.updateFavoriteGenres({
      body: selectedGenres
    }).subscribe({
      next: () => {
        this.toastService.success('Favorite genres has been updated');
        this.loadProfile();
        this.editGenres = false;
      },
      error: (err) => {
        this.toastService.error('Failed to update favorite genres');
      }
    })
  }

  cancelEditFavoriteGame() {
    this.selectedFavoriteGame = this.originalFavoriteGame;
    this.editFavoriteGame = false;
  }

  searchLibrary(query: string) {
    this.searchedQuery = query;
    this.getLibraryGames(this.searchedQuery);
  }
  userLibraryResponse: UserLibraryResponse[] = [];
  getLibraryGames(query: string) {
    this.userService.getLibraryGames({
      query: query
    }).subscribe({
      next: (result) => {
        this.userLibraryResponse = result;
      }
    })
  }



  selectFavoriteGame(game: UserLibraryResponse) {
    this.searchedQuery = '';
    this.selectedFavoriteGame = game;
    this.userLibraryResponse = [];
  }

  removeSelectedGame() {
    this.selectedFavoriteGame = null;
    this.searchedQuery = '';
  }

  hasFavoriteGameChanges() {

    return this.originalFavoriteGame?.gameId !== this.selectedFavoriteGame?.gameId;
  }

  saveFavoriteGame() {
    this.userService.pinGame({
      body: {
        gameId: this.selectedFavoriteGame?.gameId
      }
    }).subscribe({
      next: () => {
        this.toastService.success('Favorite game has been updated');
        this.editFavoriteGame = false;
        this.loadProfile();
      },
      error: (err) => {
        console.log(err);
        this.toastService.error('Failed to update favorite game');
      }
    })
  }

  // sendResetLink() {
  //   this.authenticationService.processForgotPasswordRequest({
  //     body: this.authenticationRequest
  //   }).subscribe({
  //     next: () => {
  //     }
  //   })
  // }

}
