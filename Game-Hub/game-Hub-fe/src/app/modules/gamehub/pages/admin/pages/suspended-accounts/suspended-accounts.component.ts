import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {PageResponseAdminReportsResponse} from '../../../../../../services/models/page-response-admin-reports-response';
import {
  PageResponseAdminSuspendedAccountsResponse
} from '../../../../../../services/models/page-response-admin-suspended-accounts-response';
import {SearchBar} from '../../../../components/search-bar/search-bar';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {
  PreviewSuspendedUserModalComponent
} from '../../components/suspended-accounts/preview-suspended-user-modal.component';
import {AdminSuspendedAccountsResponse} from '../../../../../../services/models/admin-suspended-accounts-response';


@Component({
  selector: 'app-suspended-accounts',
  imports: [
    SearchBar,
    NgForOf,
    DatePipe,
    NgClass,
    NgIf,
    PreviewSuspendedUserModalComponent
  ],
  templateUrl: './suspended-accounts.component.html',
  styleUrl: './suspended-accounts.component.scss',
})
export class SuspendedAccountsComponent implements OnInit {

  suspendedAccountsResponse: PageResponseAdminSuspendedAccountsResponse = {};
  selectedUser: AdminSuspendedAccountsResponse = {};
  isPreviewModalOpen = false;

  constructor(
    private adminService: AdminControllerService
  ) {
  }

  ngOnInit() {
    this.loadSuspendedUsers()
  }


  loadSuspendedUsers() {
    this.adminService.getSuspendedAccount().subscribe({
      next: data => {
        this.suspendedAccountsResponse = data;
        console.log(this.suspendedAccountsResponse);
        console.log(data);
      }
    })
  }

  closeModal() {
    this.loadSuspendedUsers();
    this.isPreviewModalOpen = false;
  }

  openModal(suspendedUser: AdminSuspendedAccountsResponse) {
    this.selectedUser = {};
    this.selectedUser = suspendedUser;
    this.isPreviewModalOpen = true;
  }
}
