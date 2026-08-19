import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AdminUserResponse} from '../../../../../../../services/models/admin-user-response';

@Component({
  selector: 'app-unban-modal',
  imports: [],
  templateUrl: './unban-modal.component.html',
  styleUrl: './unban-modal.component.scss',
})
export class UnbanModalComponent {

  @Input() user!: AdminUserResponse;
  @Output() close = new EventEmitter<void>();
  @Output() unBanUser = new EventEmitter<string>();

}
