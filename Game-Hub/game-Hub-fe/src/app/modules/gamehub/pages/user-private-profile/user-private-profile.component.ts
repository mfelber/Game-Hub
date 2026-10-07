import {Component, OnInit} from '@angular/core';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {Router} from '@angular/router';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import { DatePipe, NgClass, NgStyle } from '@angular/common';
import {StoreControllerService} from '../../../../services/services';
import {FormsModule} from '@angular/forms';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {UserLibraryResponse} from '../../../../services/models/user-library-response';
import {RecentGamesResponse} from '../../../../services/models/recent-games-response';
import {LoadingComponent} from '../../components/loading/loading.component';
import {forkJoin} from 'rxjs';

@Component({
  selector: 'app-user-profile',
  imports: [
    NgClass,
    FormsModule,
    NgStyle,
    UserActionsComponent,
    DatePipe,
    LoadingComponent,
  ],
  templateUrl: './user-private-profile.component.html',
  styleUrl: './user-private-profile.component.scss'
})
export class UserPrivateProfileComponent implements OnInit{

  constructor(
    private router: Router,
    private userService: UserProfileControllerService,
    private gameService: StoreControllerService
  ) {
  }

  ngOnInit(): void {
    this.loadUserPrivateProfile();
  }

  profilePicture: File | null = null;

  isLoading = false;

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

  loadUserPrivateProfile() {
    this.isLoading = true;

    forkJoin({
      user: this.userService.getUserPrivate(),
      recentGames: this.userService.getRecentlyPlayedGames(),
    }).subscribe({
      next: ({user, recentGames}) => {
        this.userResponse = user;
        console.log(user);

        this.recentGamesResponse = recentGames;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = true;
        console.log(err);
      }
    }
    );
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
    return user.userProfilePicture;
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

  goToWishList() {
    this.router.navigate(['gamehub/wishlist']);
  }

  goToUser(userId: number | undefined) {
    this.router.navigate(['gamehub/user', userId])
  }

  goToFriends() {

  }
}
