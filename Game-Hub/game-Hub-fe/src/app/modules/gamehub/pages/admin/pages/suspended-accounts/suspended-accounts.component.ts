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
import {EmptyStateComponent} from '../../../../components/empty-state/empty-state.component';


@Component({
  selector: 'app-suspended-accounts',
  imports: [
    SearchBar,
    NgForOf,
    DatePipe,
    NgClass,
    NgIf,
    PreviewSuspendedUserModalComponent,
    EmptyStateComponent
  ],
  templateUrl: './suspended-accounts.component.html',
  styleUrl: './suspended-accounts.component.scss',
})
export class SuspendedAccountsComponent implements OnInit {

  suspendedAccountsResponse: PageResponseAdminSuspendedAccountsResponse = {};
  selectedUser: AdminSuspendedAccountsResponse = {};
  isPreviewModalOpen = false;
  activeFilter: 'ALL' | 'ONGOING' | 'EXPIRED' = 'ALL';

  constructor(
    private adminService: AdminControllerService
  ) {
  }

  ngOnInit() {
    this.loadSuspendedUsers()
  }

  setFilter(filter: 'ALL' | 'ONGOING' | 'EXPIRED') {
    this.activeFilter = filter;
  }

  get ongoingSuspensions(): number {
    return this.suspendedAccountsResponse.content?.filter(
      suspension => suspension.suspensionStatus === 'ONGOING'
    ).length ?? 0;
  }

  get expiredSuspensions(): number {
    return this.suspendedAccountsResponse.content?.filter(
      suspension => suspension.suspensionStatus === 'EXPIRED'
    ).length ?? 0;
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

  get filteredSuspensions() {
    const suspensions = this.suspendedAccountsResponse.content ?? [];

    if (this.activeFilter === 'ONGOING') {
      return suspensions.filter(
        suspension => suspension.suspensionStatus === 'ONGOING'
      );
    }

    if (this.activeFilter === 'EXPIRED') {
      return suspensions.filter(
        suspension => suspension.suspensionStatus === 'EXPIRED'
      );
    }

    return suspensions;
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
