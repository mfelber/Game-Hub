import {AfterViewInit, Component, OnInit} from '@angular/core';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {initFlowbite} from 'flowbite';
import {Router} from '@angular/router';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import { DatePipe, NgClass, NgStyle } from '@angular/common';
import {GameResponse} from '../../../../services/models/game-response';
import {CountryControllerService, StoreControllerService} from '../../../../services/services';
import {FormsModule} from '@angular/forms';
import {UserUpdateRequest} from '../../../../services/models/user-update-request';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../../../../services/services/authentication.service';
import {AuthenticationRequest} from '../../../../services/models/authentication-request';
import {CardColorControllerService} from '../../../../services/services/card-color-controller.service';
import {CardPreviewComponent} from '../../components/card-preview/card-preview.component';
import {RefreshService} from '../../../../services/fn/refresh-service/refresh-service';
import {MatSlideToggle} from '@angular/material/slide-toggle';
import {FlagsControllerService} from '../../../../services/services/flags-controller.service';
import {ProfileInfoComponent} from '../../components/profile-info/profile-info.component';
import {EditProfileInfoComponent} from '../../components/edit-profile-info/edit-profile-info.component';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {UserLibraryResponse} from '../../../../services/models/user-library-response';
import {RecentGamesResponse} from '../../../../services/models/recent-games-response';
import {GameResponseShort} from '../../../../services/models/game-response-short';
import {LoadingComponent} from '../../components/loading/loading.component';
import {forkJoin} from 'rxjs';
import {
  HlmDialog, HlmDialogClose,
  HlmDialogContent, HlmDialogDescription,
  HlmDialogFooter,
  HlmDialogHeader,
  HlmDialogTitle,
  HlmDialogTrigger,
  HlmDialogPortal
} from '@spartan/dialog';
import {HlmButton} from '@spartan/button';
import {HlmTextarea} from '@spartan/textarea';

@Component({
  selector: 'app-user-profile',
  imports: [
    NgClass,
    FormsModule,
    NgStyle,
    ProfileInfoComponent,
    EditProfileInfoComponent,
    UserActionsComponent,
    DatePipe,
    LoadingComponent,
    HlmDialog,
    HlmDialogContent,
    HlmDialogHeader,
    HlmDialogFooter,
    HlmDialogTrigger,
    HlmDialogTitle,
    HlmDialogDescription,
    HlmDialogClose,
    HlmDialogPortal,
    HlmTextarea
  ],
  templateUrl: './user-private-profile.component.html',
  styleUrl: './user-private-profile.component.scss'
})
export class UserPrivateProfileComponent implements OnInit{

  constructor(
    private router: Router,
    private userService: UserProfileControllerService,
    private gameService: StoreControllerService,
    private locationService: CountryControllerService,
    private http: HttpClient,
    private authenticationService: AuthenticationService,
    private cardColorService: CardColorControllerService,
    private refreshService: RefreshService,
    private storeFlagsService: FlagsControllerService
  ) {
  }

  ngOnInit(): void {
    this.loadUserPrivateProfile();
    this.getColorsForCard()
  }

  activeTab: 'basic' | 'profile' | 'gaming' = 'basic';

  profilePicture: File | null = null;
  previewImage: string | undefined;
  previewBanner: string | undefined;
  profileBanner: File | null = null;
  selectedBannerId: number | null = null;

  isEditProfileModalOpen = false;
  isProfileModalOpen = false;
  isLoading = false;
  isLocationDropdownOpen = false;
  toastVisible = false;

  showPredefinedBanners = false;
  isPreviewImageInserted = false;
  isPreviewBannerInserted = false;
  userHasProfilePicture = true

  allLocations: { name: string; iconPath: string, countryName: string }[] = [];
  selectedGenres: Set<number> = new Set<number>()
  favoriteGenreIds: number[] = [];
  allStoreFlags: { flagName: string; description: string }[] = [];
  successMessage: string | null = null;
  genreResponse: any[] = [];
  cardColorsResponse: any [] = [];
  selectedColorCode: string = '';
  selectedColorId: number | null = null;

  showPreviewColors = false
  previewSelectedColor = '';
  previewImageInserted = false;

  friendRequestOptions: string[] = [];
  sendMessageOptions: string[] = [];
  profileVisibilityOptions: string[] = [];
  groupInvitesOptions: string[] = [];
  playTogetherInvitesOptions: string[] = [];

  recentGamesResponse: RecentGamesResponse[] = [];

  userResponse: UserPrivateResponse = {
    bio: '',
    badges: [],
    favoriteGenres: [],
    userProfilePicture: '',
    recommendedGames: [],
    bannerImage: '',
    wishlistCount: 0,
    libraryCount: 0,
    profileColor: '',

  };
  userRequest: UserUpdateRequest = {
    email: this.userResponse.email,
    cardColorId: this.selectedColorId!
  };

  bioUpdateRequest: UserUpdateRequest = {
    bio: ''
  }

  authenticationRequest: AuthenticationRequest = {
    email: '',
    password: ''
  }

  loadUserPrivateProfile() {
    this.isLoading = true;

    forkJoin({
      user: this.userService.getUserPrivate(),
      recentGames: this.userService.getRecentlyPlayedGames(),
      genres: this.gameService.getAllGenres(),
    }).subscribe({
      next: ({user, recentGames, genres}) => {
        this.userResponse = user;
        console.log(this.userResponse);
        this.bioUpdateRequest.bio = user.bio || '';
        this.userHasProfilePicture = !!user.userProfilePicture;
        this.favoriteGenreIds = user.favoriteGenres?.map(g => g.id!) || [];
        this.userResponse.favoriteGenres = user.favoriteGenres?.sort((a, b) =>
          a.name!.localeCompare(b.name!)
        );
        this.userRequest = {
          email: user.email,
          firstName: user.firstName,
          lastName: user.lastName,
          username: user.username,
          country: this.userResponse.country?.name as undefined
        }
        this.authenticationRequest = {
          email: user.email!,
          password: ''
        }

        this.recentGamesResponse = recentGames;
        this.genreResponse = genres;
        this.isLoading = false;
        setTimeout(() => {
          console.log('MODAL:', document.getElementById('default-modal'));
          initFlowbite();
        });
      },
      error: (err) => {
        this.isLoading = true;
        console.log(err);
      }
    }
    );
  }

  loadRecentGames() {
    this.userService.getRecentlyPlayedGames().subscribe({
      next: (recentGames) => {
        this.recentGamesResponse = recentGames;
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
  // TODO use this while retrieving game images for currently playing , wishlist,
  getGameImageCover(game: UserLibraryResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }


  getProfilePicture(user: UserPrivateResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return '';
  }

  getBanner(user: UserPrivateResponse) {
    if (user.bannerImage) {
      return 'data:image/jpeg;base64,' + user.bannerImage;
    }
    return user.predefinedBannerPath;
  }

  // TODO click event for use this while retrieving game images for currently playing , wishlist,
  goToGame(gameId: any) {
    if (!gameId) {
      return;
    }
    this.gameService.getGameById({gameId}).subscribe({
      next: (game) => {
        this.router.navigate(['gamehub/library/game', gameId]);
      },
      error: (err) => {
        console.error('Error with loading game:', err);
      }
    });
  }

  saveBio(ctx: any) {
    if (this.bioUpdateRequest.bio === this.userResponse.bio) {
      ctx.close();
      return;
    } else {
      this.userService.updateBio({
        body: this.bioUpdateRequest
      }).subscribe({
          next: () => {
            this.showSuccess('Bio updated successfully!')
            this.getUserBio()
            ctx.close();
          },
          error: (err) => {
            console.log(err)
          }
        }
      )
    }
  }

  getUserBio() {
    this.userService.getBio().subscribe({
      next: (response) => {
        this.userResponse.bio = response.bio
      }
    })
  }

  saveFavoriteGenres(ctx: any) {
    const allGenres = [...new Set([...this.favoriteGenreIds, ...this.selectedGenres])];

    this.userService.updateFavoriteGenres({
      body: allGenres
    }).subscribe({
      next: () => {
        this.showSuccess('Genres saved successfully!');
        this.loadUserPrivateProfile();
        ctx.close();
      },
      error: (err) => console.error(err)
    });
  }

  private getGenres() {
    this.gameService.getAllGenres().subscribe({
      next: (genres) => {
        this.genreResponse = genres;
        console.log(this.genreResponse);
      }
    })
  }

  private getColorsForCard() {
    this.cardColorService.getColors().subscribe({
      next: (colors) => {
        this.cardColorsResponse = colors;
      }
    })
  }

  selectedGenre(id: number) {
    if (this.selectedGenres.has(id)) {
      this.selectedGenres.delete(id)
    } else {
      this.selectedGenres.add(id)
    }
  }

  cancelFavoriteGenres() {
    this.selectedGenres.clear()
    this.favoriteGenreIds = this.userResponse.favoriteGenres?.map(g => g.id!) || [];
  }

  removeFavoriteGenre(id: any) {
    this.favoriteGenreIds = this.favoriteGenreIds.filter(g => g !== id);
    this.selectedGenres.delete(id);

  }

  async saveProfile(event:any) {

    try {

      if (event.favoriteGameId !== null) {
        await this.userService.pinGame({
          gameId: event.favoriteGameId,
        }).toPromise()
        this.showSuccess('You have successfully updated profile')
      }

      if (event.selectedBannerId !== null) {
        const bannerPath = "/assets/banners/banner_" + event.selectedBannerId + ".jpg";
        await this.userService.setPredefinedBanner({body: {bannerPath}}).toPromise();
        this.profileBanner = null;
        this.showSuccess('You have successfully updated profile')

      } else if (event.profileBanner) {
        const formData = new FormData();
        formData.append('file', event.profileBanner);
        await this.http.post('http://localhost:8088/api/v1/profile/custom/banner', formData).toPromise();
        this.showSuccess('You have successfully updated profile')
      }

      if (event.profilePicture) {
        const formData = new FormData();
        formData.append('file', event.profilePicture);

        try {
          await this.http.post('http://localhost:8088/api/v1/profile/image', formData).toPromise()
          this.userHasProfilePicture = true;
          this.showSuccess('You have successfully updated profile')
        } catch (err) {
          console.error(err)
        }

      }

      const changesExistProfileInfo = this.userRequest.username !== this.userResponse.username ||
        this.userRequest.firstName !== this.userResponse.firstName ||
        this.userRequest.lastName !== this.userResponse.lastName ||
        this.userRequest.email !== this.userResponse.email ||
        this.userRequest.country !== this.userResponse.country?.name || this.userRequest.cardColorId !== this.userResponse.cardColor?.id

      if (changesExistProfileInfo) {
        this.showSuccess('You have successfully updated profile')
        await this.userService.updateUserProfile({
          body: this.userRequest
        }).toPromise();

      }
      this.refreshService.triggerRefresh();
      this.loadUserPrivateProfile();
      this.closeModal();

    } catch (err) {
      console.error(err)
    }

  }

  closeModal() {
    this.isLocationDropdownOpen = false;
    this.selectedColorCode = '';
    this.selectedColorId = null;
    this.userRequest.cardColorId = this.userResponse.cardColor?.id;
    this.isProfileModalOpen = false;
    this.isEditProfileModalOpen = false;
    this.bioUpdateRequest.bio = this.userResponse.bio;
    this.userRequest.country = this.userResponse.country?.name as undefined;
    this.userRequest.username = this.userResponse.username;
    this.userRequest.firstName = this.userResponse.firstName;
    this.userRequest.lastName = this.userResponse.lastName;
    this.userRequest.email = this.userResponse.email;
    this.previewImage = this.userResponse.userProfilePicture
      ? 'data:image/jpeg;base64,' + this.userResponse.userProfilePicture
      : undefined;

    this.previewBanner = this.userResponse.bannerImage
      ? 'data:image/jpeg;base64,' + this.userResponse.bannerImage
      : undefined;
    this.activeTab = 'basic';
    this.isPreviewBannerInserted = false;
    this.isPreviewImageInserted = false;
    this.showPredefinedBanners = false;
    this.selectedBannerId = null;
    this.profilePicture = null;
    this.profileBanner = null;
  }

  editProfile() {
    this.closeModal();
    this.isEditProfileModalOpen = true;
    this.getCountries();
    this.getStoreFlags();
    this.getCommunityFlags()
  }

  getCountries() {
    this.locationService.getAllCountries().subscribe({
      next: (country) => {
        this.allLocations = country.map(country => ({
          name: country.name!,
          iconPath: country.iconPath!,
          countryName: country.countryName!,
        }));
      }
    })
  }

  getIconPath(locationName: string): string | undefined {
    const loc = this.allLocations.find(l => l.name === locationName);
    return loc?.iconPath;
  }

  // TODO use this method in settings page
  sendResetLink() {
    this.authenticationService.processForgotPasswordRequest({
      body: this.authenticationRequest
    }).subscribe({
      next: () => {
        this.showSuccess('reset link was send to you email')
      }
    })
  }


  showPreview(data:any) {
    this.previewSelectedColor = data.selectedColorCode;
    this.previewBanner = data.previewBanner;
    this.previewImage = data.previewImage;
    this.previewImageInserted = data.isPreviewImageInserted;

    this.showPreviewColors = true;
    this.isEditProfileModalOpen = false;
  }

  hidePreview() {
    this.showPreviewColors = false;
    this.activeTab = 'profile';
    this.isEditProfileModalOpen = true;
  }

  // TODO settings page
  getStoreFlags() {
    this.storeFlagsService.getAllStoreFlags().subscribe({
      next: (res) => {
        this.allStoreFlags = res.map(flag => ({
          flagName: flag.name!,
          description: flag.description!
        }))
      }
    })
  }

  // TODO settings page
  getCommunityFlags() {
    this.storeFlagsService.getAllCommunityFlags().subscribe(res => {
      res.forEach(flag => {
        if (flag.flagKey === 'FRIEND_REQUEST') {
          this.friendRequestOptions = flag.options!
        }
        if (flag.flagKey === 'SEND_MESSAGES') {
          this.sendMessageOptions = flag.options!
        }
        if (flag.flagKey === 'PROFILE_VISIBILITY') {
          this.profileVisibilityOptions = flag.options!
        }
        if (flag.flagKey === 'GROUP_INVITES') {
          this.groupInvitesOptions = flag.options!
        }
        if (flag.flagKey === 'PLAY_TOGETHER') {
          this.playTogetherInvitesOptions = flag.options!
        }
      })
      }
    )
  }

  goToWishList() {
    this.router.navigate(['gamehub/wishlist']);
  }

  goToUser(userId: number | undefined) {
    this.router.navigate(['gamehub/user', userId])
  }

  goToFriends() {

  }
}
