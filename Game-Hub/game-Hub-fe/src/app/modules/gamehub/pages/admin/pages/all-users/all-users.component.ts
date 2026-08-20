import {Component, OnInit} from '@angular/core';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {PageResponseAdminUserResponse} from '../../../../../../services/models/page-response-admin-user-response';
import {SearchBar} from '../../../../components/search-bar/search-bar';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {AccountStatusResponse} from '../../../../../../services/models/account-status-response';
import {RoleResponse} from '../../../../../../services/models/role-response';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AdminUserResponse} from '../../../../../../services/models/admin-user-response';
import {UserInfoModalComponent} from '../../components/user/user-info/user-info-modal.component';
import {ChangeUserRoleModalComponent} from '../../components/user/change-user-role/change-user-role-modal.component';
import {BanModalComponent} from '../../components/ban/ban-modal/ban-modal.component';
import {UnbanModalComponent} from '../../components/ban/unban-modal/unban-modal.component';

@Component({
  selector: 'app-all-users',
  imports: [
    SearchBar,
    NgForOf,
    DatePipe,
    NgClass,
    NgIf,
    ReactiveFormsModule,
    FormsModule,
    UserInfoModalComponent,
    ChangeUserRoleModalComponent,
    BanModalComponent,
    UnbanModalComponent
  ],
  templateUrl: './all-users.component.html',
  styleUrl: './all-users.component.scss',
})
export class AllUsersComponent implements OnInit {

  usersResponse: PageResponseAdminUserResponse = {};
  accountStatusResponse: AccountStatusResponse[] = [];
  roleResponse: RoleResponse[] = [];
  isLoaded = false;

  filteredUsers: AdminUserResponse[] = [];

  filters = {
    role: '',
    accountStatus: ''
  }

  selectedUser: AdminUserResponse = {};

  isUserInfoModalOpen = false;
  isChangeRoleModalOpen = false;
  isBanUserModalOpen = false;
  isUnbanUserModalOpen = false;
  isDeleteUserModalOpen = false;

  successMessage: string | null = null;
  toastVisible = false;


  constructor(
    private adminControllerService: AdminControllerService
  ) {
  }

  ngOnInit() {
    this.loadUsers();
    this.loadDropdownData();
  }

  filterUsers() {
    const selectedRole = this.filters.role;
    const selectedAccountStatus = this.filters.accountStatus;
    this.filteredUsers = (this.usersResponse.content || []).filter(user => {
      const selectedRoleMatch = !selectedRole || user.role === selectedRole;
      const selectedAccountStatusMatch = !selectedAccountStatus || user.accountStatus === selectedAccountStatus;

      return selectedRoleMatch && selectedAccountStatusMatch;
    })
  }

  loadUsers() {
    this.adminControllerService.getAllUsers().subscribe({
      next: data => {
        this.filteredUsers = [...(data.content || [])]
        this.usersResponse = data;
        this.isLoaded = true;
        console.log(data);
      },
      error: err => {
        this.isLoaded = false;
        console.log(err);
      }
    })
  }

  loadDropdownData() {
    this.adminControllerService.getAllRoles().subscribe({
      next: data => {
        this.roleResponse = data;
        console.log(data);
      }
    })
    this.adminControllerService.getAllAccountStatuses().subscribe({
      next: data => {
        this.accountStatusResponse = data;
        console.log(data);
      }
    })
  }

  resetFilters() {
    this.filters = {
      role: '',
      accountStatus: ''
    };

    this.filteredUsers = [...(this.usersResponse.content || [])]
  }

  banOrUnbanUser(user: AdminUserResponse) {
    this.selectedUser = {};
    if (user.accountStatus !== 'BANNED') {
      this.selectedUser = user;
      this.isBanUserModalOpen = true;
    }

    this.selectedUser = user;
    this.isUnbanUserModalOpen = true;
  }


  getUserInfo(userId: any) {
    this.selectedUser = {};
    this.adminControllerService.getUserInfo({userId}).subscribe({
      next: user => {
        this.isUserInfoModalOpen = true;
        this.selectedUser = user;
      }, error: (err) => {
        this.isUserInfoModalOpen = false;
        console.log(err);
      }
    })

  }

  changeRole(user: AdminUserResponse) {
    this.selectedUser = {};
    this.selectedUser = user;
    this.isChangeRoleModalOpen = true;
    console.log(this.selectedUser.userId);
  }

  showSuccess(message: string) {
    this.loadUsers();
    this.successMessage = message;

    setTimeout(() => this.toastVisible = true, 10);

    setTimeout(() => this.hideToast(), 3000);

  }

  hideToast() {
    this.toastVisible = false;

    setTimeout(() => this.successMessage = null, 500);
  }

  closeModal() {
    this.isUserInfoModalOpen = false;
    this.isChangeRoleModalOpen = false;
    this.isBanUserModalOpen = false;
    this.isUnbanUserModalOpen = false;
    this.isDeleteUserModalOpen = false;
  }
}
