import {Component, EventEmitter, Input, Output} from '@angular/core';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {NgIf, NgStyle} from '@angular/common';

@Component({
  selector: 'app-profile-info',
  imports: [
    NgIf,
    NgStyle
  ],
  templateUrl: './profile-info.component.html',
  styleUrl: './profile-info.component.scss',
})
export class ProfileInfoComponent {
  @Input() user!: UserPrivateResponse;
  @Input() hasProfilePicture!: boolean;
  @Output() close = new EventEmitter<void>();
  @Output() edit = new EventEmitter<void>();

  getBanner(user: UserPrivateResponse) {
    if (user.bannerImage) {
      return 'data:image/jpeg;base64,' + user.bannerImage;
    }
    return user.predefinedBannerPath;
  }

  getProfilePicture(user: UserPrivateResponse) {
    if (user.userProfilePicture) {
      this.hasProfilePicture = true;
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    } else {
      this.hasProfilePicture = false;
    }
    return this.hasProfilePicture;
  }
}
