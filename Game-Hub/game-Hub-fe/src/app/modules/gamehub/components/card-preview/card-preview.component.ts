import {Component, Input, OnInit} from '@angular/core';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {FormsModule} from '@angular/forms';
import {DatePipe, NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {UserActionsComponent} from '../user-actions/user-actions.component';
import {RecentGamesResponse} from '../../../../services/models/recent-games-response';
import {UserLibraryResponse} from '../../../../services/models/user-library-response';

@Component({
  selector: 'app-card-preview',
  imports: [
    FormsModule,
    NgIf,
    NgClass,
    NgStyle,
    NgForOf,
    UserActionsComponent,
    DatePipe
  ],
  templateUrl: './card-preview.component.html',
  styleUrl: './card-preview.component.scss'
})
export class CardPreviewComponent{
  @Input() userResponse!: UserPrivateResponse;
  @Input() cardColorsResponse!: any[];
  userHasProfilePicture = true;
  @Input() selectedColor!: string;
  @Input() previewBanner!: string | undefined;
  @Input() isPreviewImageInserted!: boolean;
  @Input() previewImage!: string | undefined;
  @Input() recentGames!: RecentGamesResponse[];
  @Input() favoriteGame!: RecentGamesResponse[];

  getBanner(user: UserPrivateResponse) {
    if (user.bannerImage) {
      return 'data:image/jpeg;base64,' + user.bannerImage;
    }
    return user.predefinedBannerPath;
  }

  getProfilePicture(user: UserPrivateResponse) {
    if (user.userProfilePicture) {
      this.userHasProfilePicture = true;
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return this.userHasProfilePicture;
  }

  getGameImageCover(game: UserLibraryResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

}
