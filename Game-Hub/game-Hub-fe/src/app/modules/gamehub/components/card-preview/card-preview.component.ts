import {Component, Input, OnInit} from '@angular/core';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {FormsModule} from '@angular/forms';
import {NgClass, NgIf, NgStyle} from '@angular/common';

@Component({
  selector: 'app-card-preview',
  imports: [
    FormsModule,
    NgIf,
    NgClass,
    NgStyle
  ],
  templateUrl: './card-preview.component.html',
  styleUrl: './card-preview.component.css'
})
export class CardPreviewComponent{
  @Input() userResponse!: UserPrivateResponse;
  @Input() cardColorsResponse!: any[];
  userHasProfilePicture = true;
  @Input() selectedColor!: string;
  @Input() previewBanner!: string | undefined;
  @Input() isPreviewImageInserted!: boolean;
  @Input() previewImage!: string | undefined;

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

}
