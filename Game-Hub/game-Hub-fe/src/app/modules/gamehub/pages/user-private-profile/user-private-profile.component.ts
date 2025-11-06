import {Component, OnInit} from '@angular/core';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {initFlowbite} from 'flowbite';
import {Router} from '@angular/router';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {GameResponse} from '../../../../services/models/game-response';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {FormsModule} from '@angular/forms';
import {UserUpdateRequest} from '../../../../services/models/user-update-request';

@Component({
  selector: 'app-user-profile',
  imports: [
    NgIf,
    NgForOf,
    NgClass,
    FormsModule
  ],
  templateUrl: './user-private-profile.component.html',
  styleUrl: './user-private-profile.component.css'
})
export class UserPrivateProfileComponent implements OnInit {

  ngOnInit(): void {
    initFlowbite();
    this.loadUserPrivateProfile();
  }

  constructor(
    private router: Router,
    private userService: UserProfileControllerService,
    private gameService: GameControllerService
  ) {
  }

  successMessage: string | null = null;
  toastVisible = false;
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
  isEditBioModalOpen = false;
  bioUpdateRequest: UserUpdateRequest = {
    bio: ''
  };


  private loadUserPrivateProfile() {
    this.userService.getUserPrivate().subscribe({
      next: (user) => {
        this.userResponse = user;
        console.log(user)
        this.bioUpdateRequest.bio = user.bio || '';
        this.getProfilePicture(user)
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
}
