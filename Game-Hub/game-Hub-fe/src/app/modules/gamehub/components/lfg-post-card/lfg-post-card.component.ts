import {Component, EventEmitter, Input, Output} from '@angular/core';
import {LfgResponse} from '../../../../services/models/lfg-response';
import {UserCommunityResponse} from '../../../../services/models/user-community-response';
import {HlmTooltip} from '@spartan/tooltip';
import {TimeAgoPipe} from '../../../../services/TimeAgoPipe/time-ago-pipe';
import {NgClass, NgStyle} from '@angular/common';

@Component({
  selector: 'app-lfg-post-card',
  imports: [
    HlmTooltip,
    TimeAgoPipe,
    NgStyle,
    NgClass
  ],
  templateUrl: './lfg-post-card.component.html',
  styleUrl: './lfg-post-card.component.scss',
})
export class LfgPostCardComponent {
  @Input({required: true}) post!: LfgResponse;
  @Output() toggleSave = new EventEmitter<LfgResponse>();

  isExpiring(expiresAt: string | Date | null | undefined): boolean {
    if (!expiresAt) {
      return false;
    }

    const expiring = new Date(expiresAt).getTime();
    const twoDays = 2 * 24 * 60 * 60 * 1000;
    return expiring - Date.now() <= twoDays && expiring > Date.now();
  }

  getProfilePicture(user: UserCommunityResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return user.userProfilePicture;
  }

}
