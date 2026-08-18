import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AdminUserResponse} from '../../../../../../services/models/admin-user-response';
import {DatePipe, NgClass, NgIf, NgStyle} from '@angular/common';

@Component({
  selector: 'app-user-info-modal',
  imports: [
    NgClass,
    DatePipe,
    NgIf,
    NgStyle
  ],
  templateUrl: './user-info-modal.component.html',
  styleUrl: './user-info-modal.component.scss',
})
export class UserInfoModalComponent {

  @Input() user!: AdminUserResponse;
  @Output() close = new EventEmitter<void>();

  getProfilePicture(user: AdminUserResponse) {
    if (user.profilePicture) {
      return 'data:image/jpeg;base64,' + user.profilePicture;
    }
    return;
  }
}
