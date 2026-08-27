import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AdminSuspendedAccountsResponse} from '../../../../../../services/models/admin-suspended-accounts-response';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {AdminUserModerationResponse} from '../../../../../../services/models/admin-user-moderation-response';

@Component({
  selector: 'app-preview-suspended-user-modal',
  imports: [
    NgClass,
    DatePipe,
    NgIf,
    NgForOf
  ],
  templateUrl: './preview-suspended-user-modal.component.html',
  styleUrl: './preview-suspended-user-modal.component.scss',
})
export class PreviewSuspendedUserModalComponent implements OnInit {

  @Input() suspendedUser!: AdminSuspendedAccountsResponse;
  @Output() close = new EventEmitter<void>();

  user : AdminUserModerationResponse = {}

  warningsOpen = false;
  suspensionsOpen = false;
  bansOpen = false;
  reportsOpen = false;

  constructor(
    private adminService: AdminControllerService
  ) {
  }

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.adminService.getSuspendedUserDetails({
      userId: this.suspendedUser.userId!
    }).subscribe({
      next: data => {
        this.user = data;
      }
    })
  }


  formatStatus(status?: string) {
    return status ?.replace('_', ' ') ?? '';
  }
}
