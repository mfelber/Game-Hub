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
import {GenreResponse} from '../../../../services/models/genre-response';

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
    this.getGenres();
  }

  constructor(
    private router: Router,
    private userService: UserProfileControllerService,
    private gameService: GameControllerService
  ) {
  }

  selectedGenres: Set<number> = new Set<number>()
  allGenres: string[] = [] ;
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
  isEditBioModalOpen = false;
  isEditGenresModalOpen = false;
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
        this.favoriteGenreIds = user.favoriteGenres?.map(g => g.id!) || [];
        this.userResponse.favoriteGenres = user.favoriteGenres?.sort((a, b) =>
          a.name!.localeCompare(b.name!)
        );
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
}
