import {Component, OnInit} from '@angular/core';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {initFlowbite} from 'flowbite';
import {Router} from '@angular/router';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {GameResponse} from '../../../../services/models/game-response';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {FormsModule} from '@angular/forms';
import {UserUpdateRequest} from '../../../../services/models/user-update-request';
import {LocationControllerService} from '../../../../services/services/location-controller.service';
import {HttpClient} from '@angular/common/http';
import {AuthenticationService} from '../../../../services/services/authentication.service';
import {AuthenticationRequest} from '../../../../services/models/authentication-request';

@Component({
  selector: 'app-user-profile',
  imports: [
    NgIf,
    NgForOf,
    NgClass,
    FormsModule,
    NgStyle
  ],
  templateUrl: './user-private-profile.component.html',
  styleUrl: './user-private-profile.component.css'
})
export class UserPrivateProfileComponent implements OnInit {

  ngOnInit(): void {
    initFlowbite();
    this.loadUserPrivateProfile();
    this.getGenres();
  }

  constructor(
    private router: Router,
    private userService: UserProfileControllerService,
    private gameService: GameControllerService,
    private locationService: LocationControllerService,
    private http: HttpClient,
    private authenticationService: AuthenticationService,
  ) {
  }

  selectedGenres: Set<number> = new Set<number>()
  favoriteGenreIds: number[] = [];
  successMessage: string | null = null;
  toastVisible = false;
  genreResponse: any[] = [];
  userResponse: UserPrivateResponse = {
    bio: '',
    badges: [],
    favoriteGenres: [],
    userProfilePicture: [],
    playRecently: [],
    recommendedGames: [],
    favoriteGames: [],
    bannerImage: [],
    wishlistCount: 0,
    libraryCount: 0
  };
  userRequest: UserUpdateRequest = {
    email: this.userResponse.email
  };
  isEditBioModalOpen = false;
  isEditGenresModalOpen = false;
  isEditProfileModalOpen= false;
  isProfileModalOpen = false;
  bioUpdateRequest: UserUpdateRequest = {
    bio: ''
  }

  authenticationRequest: AuthenticationRequest = {
    email: '',
    password: ''
  }
  activeTab: 'basic' | 'profile' | 'security' = 'basic';
  allLocations: { name: string; iconPath: string }[] = [];

  profilePicture: File | null = null;
  previewImage: string | undefined;
  previewBanner: string | undefined;
  profileBanner: File | null = null;

  private loadUserPrivateProfile() {
    this.userService.getUserPrivate().subscribe({
      next: (user) => {
        this.userResponse = user;
        console.log(user)
        this.bioUpdateRequest.bio = user.bio || '';
        this.getProfilePicture(user)
        this.favoriteGenreIds = user.favoriteGenres?.map(g => g.id!) || [];
        this.userResponse.favoriteGenres = user.favoriteGenres?.sort((a, b) =>
          a.name!.localeCompare(b.name!)
        );
        this.userRequest = {
          email : user.email,
          firstName : user.firstName,
          lastName : user.lastName,
          username : user.username,
          location: this.userResponse.location?.name as undefined
        }
        this.authenticationRequest = {
          email: user.email!,
          password: ''
        }
      }
    });
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

  getGameImageCover(game: GameResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  getProfilePicture(user: UserPrivateResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  getBanner(user: UserPrivateResponse) {
    if (user.bannerImage) {
      return 'data:image/jpeg;base64,' + user.bannerImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  goToGame(gameId: any) {
    this.gameService.getGameById({gameId}).subscribe({
      next: (game) => {
        this.router.navigate(['gamehub/game', gameId]);
      },
      error: (err) => {
        console.error('Error with loading game:', err);
      }
    });
  }

  saveBio() {
    if (this.bioUpdateRequest.bio === this.userResponse.bio) {
      console.log('nothing change')
      this.isEditBioModalOpen = false
    } else {
      this.userService.updateBio({
        body: this.bioUpdateRequest
      }).subscribe({
          next: () => {
            this.showSuccess('Bio updated successfully!')
            this.getUserBio()
            this.isEditBioModalOpen = false
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

  saveFavoriteGenres() {
    const allGenres = [...new Set([...this.favoriteGenreIds, ...this.selectedGenres])];

    this.userService.updateFavoriteGenres({
      body: allGenres
    }).subscribe({
      next: () => {
        this.showSuccess('Genres saved successfully!');
        this.isEditGenresModalOpen = false
        this.loadUserPrivateProfile()
      },
      error: (err) => console.error(err)
    });
  }

  private getGenres() {
    this.gameService.getAllGenres().subscribe({
      next: (genres) => {
        this.genreResponse = genres;
      }
    })
  }

  selectedGenre(id: number) {
    if (this.selectedGenres.has(id)) {
      this.selectedGenres.delete(id)
    } else {
      this.selectedGenres.add(id)
    }
    console.log(this.selectedGenres)
  }

  cancelFavoriteGenres() {
    this.selectedGenres.clear()
    this.favoriteGenreIds = this.userResponse.favoriteGenres?.map(g => g.id!) || [];
    this.isEditGenresModalOpen = false
  }

  removeFavoriteGenre(id:any) {
    this.favoriteGenreIds = this.favoriteGenreIds.filter(g => g !== id);
    this.selectedGenres.delete(id);

  }

  saveProfile() {
    if (this.userRequest.username === this.userResponse.username &&
      this.userRequest.firstName === this.userResponse.firstName &&
      this.userRequest.lastName === this.userResponse.lastName &&
      this.userRequest.email === this.userResponse.email &&
      this.userRequest.location === this.userResponse.location?.name as undefined ) {
        this.closeModal()
    } else {
      this.userService.updateUserProfile({
        body: this.userRequest
      }).subscribe({
        next: () => {
          this.isEditProfileModalOpen = false;
          this.showSuccess('Profile was successfully updated')
          this.loadUserPrivateProfile()
        }
      })
    }

    if (this.profilePicture) {
      const formData = new FormData();
      formData.append('file', this.profilePicture);

      this.http.post('http://localhost:8088/api/v1/profile/image', formData
      ).subscribe({
        next: () => {
          this.loadUserPrivateProfile()
          this.showSuccess('Profile was successfully updated')
        }
      });
    } else {
      this.closeModal()
    }

    if (this.profileBanner) {
      const formData = new FormData();
      formData.append('file', this.profileBanner);

      this.http.post('http://localhost:8088/api/v1/profile/banner', formData
      ).subscribe(({
        next: () => {
          this.loadUserPrivateProfile()
          this.showSuccess('Profile was successfully updated')
        }
      }))
    } else {
      this.closeModal()
    }
  }

  closeModal() {
    this.isProfileModalOpen = false;
    this.isEditProfileModalOpen = false;
    this.isEditBioModalOpen = false;
    this.bioUpdateRequest.bio = this.userResponse.bio
    this.userRequest.location = this.userResponse.location?.name as undefined
    this.userRequest.username = this.userResponse.username
    this.userRequest.firstName = this.userResponse.firstName
    this.userRequest.lastName = this.userResponse.lastName
    this.userRequest.email = this.userResponse.email
    this.previewImage = 'data:image/jpeg;base64,' + this.userResponse.userProfilePicture;
    this.previewBanner = 'data:image/jpeg;base64,' + this.userResponse.bannerImage;
    this.activeTab = 'basic'
  }

  editProfile() {
    this.closeModal();
    this.isEditProfileModalOpen = true;
    this.getLocations();
  }

  getLocations() {
    this.locationService.getLocations().subscribe({
      next: (location) => {
        this.allLocations = location.map(location => ({
          name: location.name!,
          iconPath: location.iconPath!
        }));
      }
    })
  }


  getIconPath(locationName: string): string | undefined {
    const loc = this.allLocations.find(l => l.name === locationName);
    return loc?.iconPath;
  }

  onFileSelected(event: any) {
    const input = event.target as HTMLInputElement;
    this.profilePicture = input.files![0];
    if (this.profilePicture) {
      const reader = new FileReader();
      reader.onload = () => {
        this.previewImage = reader.result as string;
      }
      reader.readAsDataURL(this.profilePicture);
    }
  }

  onBannerSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    this.profileBanner = input.files![0];
    if (this.profileBanner) {
      const reader = new FileReader();
      reader.onload = () => {
        this.previewBanner = reader.result as string;
      }
      reader.readAsDataURL(this.profileBanner);
    }
  }

  senResetLink() {
    this.authenticationService.processForgotPasswordRequest({
      body: this.authenticationRequest
    }).subscribe({
      next: () => {
        this.showSuccess('reset link was send to you email')
    }
    })
  }
}
