import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AdminUserResponse} from '../../../../../../../services/models/admin-user-response';
import {AdminControllerService} from '../../../../../../../services/services/admin-controller.service';

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

  constructor(
    private adminService: AdminControllerService
  ) {}

  unbanUser(userId: any) {
    this.adminService.unBanUser({userId}).subscribe({
      next: (result) => {
        this.unBanUser.emit("User was successfully unbanned");
        this.close.emit();
      }
    });
  }
}
