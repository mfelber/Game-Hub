import {Component, OnInit} from '@angular/core';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserPublicResponse} from '../../../../services/models/user-public-response';
import {ActivatedRoute, Router} from '@angular/router';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {GameResponse} from '../../../../services/models/game-response';
import {CommunityControllerService} from '../../../../services/services/community-controller.service';
import {cancelFriendRequest} from '../../../../services/fn/community-controller/cancel-friend-request';

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
export class UserPublicProfileComponent implements OnInit {

  ngOnInit(): void {
    this.loadUserPublicProfile();
    // this.checkIfUserIsFriend();
  }

  constructor(
    private userService: UserProfileControllerService,
    private gameService: GameControllerService,
    private communityService: CommunityControllerService,
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
    libraryCount: 0,
    bannerType: ''
  }

  userHasProfilePicture = true
  isLoaded = false;
  friendRequestFromThisUser: boolean | null = null;
  friendRequestExistsFromSender: boolean | null = null;
  userIsMyFriend: boolean | null = null;


  private loadUserPublicProfile() {
    const userId: any = this.router.snapshot.paramMap.get('id')
    this.userService.getUserPublic({userId}).subscribe({
      next: (user) => {
        this.userResponse = user;
        console.log(user)
        this.userResponse.badges = user.badges?.sort((a, b) =>
          a.name!.localeCompare(b.name!)
        );
        if (user.userProfilePicture) {
          this.userHasProfilePicture = true
        } else {
          this.userHasProfilePicture = false
        }
        this.communityService.friendRequestExistsFromSender({userId}).subscribe({
          next: (res) => {
            this.friendRequestExistsFromSender = res;
            console.log(this.friendRequestExistsFromSender)
            // this.isLoaded = true;
          }
        })
        this.communityService.friendRequestExistsForReceiver({userId}).subscribe({
          next: (res) => {
            this.friendRequestFromThisUser = res;
            console.log(this.friendRequestFromThisUser)

          }
        })
        this.communityService.friendExistsForUser({userId}).subscribe({
          next: (res) => {
            this.userIsMyFriend = res
            console.log(this.userIsMyFriend)
            this.isLoaded = true;
          }
        })
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
    return user.predefinedBannerPath;
  }

  goToGame(gameId: any) {
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

  sendFriendRequest(userId: number) {
    this.communityService.sendFriendRequest({userId}).subscribe({
      next: () => {
        this.friendRequestExistsFromSender = true;
        this.loadUserPublicProfile();
    }
    })
  }

  cancelFriendRequest(userId: number) {
    this.communityService.cancelFriendRequest({userId}).subscribe({
      next: () => {
        this.friendRequestExistsFromSender = false;
        this.loadUserPublicProfile();
      }
    })

  }

  rejectFriendRequest(userId: number) {
    this.communityService.rejectFriendRequest({userId}).subscribe({
      next: () => {
        this.friendRequestExistsFromSender = false;
        this.loadUserPublicProfile();
      }
    })
  }

  acceptFriendRequest(userId: number) {
    this.communityService.acceptFriendRequest({userId}).subscribe({
      next: () => {
        this.friendRequestExistsFromSender = false;
        this.loadUserPublicProfile();
      }
    })

  }

  message(number: number) {

  }

}
