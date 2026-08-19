import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AdminUserResponse} from '../../../../../../../services/models/admin-user-response';
import {NgIf} from '@angular/common';
import {AdminControllerService} from '../../../../../../../services/services/admin-controller.service';

@Component({
  selector: 'app-change-user-role-modal',
  imports: [
    NgIf
  ],
  templateUrl: './change-user-role-modal.component.html',
  styleUrl: './change-user-role-modal.component.scss',
})
export class ChangeUserRoleModalComponent {

  @Input() user!: AdminUserResponse
  @Output() close = new EventEmitter<void>();
  @Output() roleChanged = new EventEmitter<string>();

  constructor(
    private adminService: AdminControllerService,
    ) {
  }


  changeRole(userId: any) {
    this.adminService.changeRole({userId}).subscribe({
      next: (userId) => {
        this.roleChanged.emit("User changed successfully!");
        this.close.emit();
      }
    })
  }
}
