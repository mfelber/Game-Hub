import {Component, OnInit} from '@angular/core';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserPublicResponse} from '../../../../services/models/user-public-response';
import {ActivatedRoute, Router} from '@angular/router';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {GameResponse} from '../../../../services/models/game-response';

@Component({
  selector: 'app-user-public-profile',
  imports: [
    NgIf,
    NgClass,
    NgForOf,
    NgStyle
  ],
  templateUrl: './user-public-profile.component.html',
  styleUrl: './user-public-profile.component.css'
})
export class UserPublicProfileComponent implements OnInit{

  ngOnInit(): void {
    this.loadUserPublicProfile();
  }

  constructor(
    private userService: UserProfileControllerService,
    private gameService: GameControllerService,
    private router: ActivatedRoute,
    private route: Router
  ) {
  }

  userResponse: UserPublicResponse = {
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
  }

  userHasProfilePicture = true
  isBannerPredefined = true;


  private loadUserPublicProfile() {
    const userId: any = this.router.snapshot.paramMap.get('id')
    this.userService.getUserPublic({userId}).subscribe({
      next:  (user) => {
        this.userResponse = user;
        this.getProfilePicture(userId)
        if (user.userProfilePicture){
          this.userHasProfilePicture = true
        } else {
          this.userHasProfilePicture = false
        }
        if (user.bannerImage){
          this.isBannerPredefined = false;
        } else {
          this.isBannerPredefined = true
        }
      }
    });
  }

  getProfilePicture(user: UserPublicResponse) {
    if (user.userProfilePicture) {
      this.userHasProfilePicture = true;
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    } else {
      this.userHasProfilePicture = false;
    }
    return;
  }

  getBanner(user: UserPublicResponse) {
    if (user.bannerImage) {
      return 'data:image/jpeg;base64,' + user.bannerImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  goToGame(gameId:any) {
    this.gameService.getGameById({gameId}).subscribe({
      next: (game) => {
        this.route.navigate(['gamehub/game', gameId]);
      },
      error: (err) => {
        console.error('Error with loading game:', err);
      }
    });
  }

  getGameImageCover(game: GameResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }
}
