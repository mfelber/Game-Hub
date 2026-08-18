import {Component, OnInit} from '@angular/core';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {PageResponseAdminUserResponse} from '../../../../../../services/models/page-response-admin-user-response';
import {SearchBar} from '../../../../components/search-bar/search-bar';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {AccountStatusResponse} from '../../../../../../services/models/account-status-response';
import {RoleResponse} from '../../../../../../services/models/role-response';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AdminUserResponse} from '../../../../../../services/models/admin-user-response';
import {UserInfoModalComponent} from '../../components/user/user-info-modal.component';

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
    UserInfoModalComponent
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
    // this.filters = {
    //   genre: '',
    //   platform: '',
    //   maxPrice: ''
    // };
    // this.filteredGames = [...(this.gamePageResponse.content || [])]

    this.filters = {
      role: '',
      accountStatus: ''
    };

    this.filteredUsers = [...(this.usersResponse.content || [])]

  }

  banOrUnbanUser(userId: number | undefined) {
    //   if user is banned popup unban open
    //   if user is not banned and admin want to ban user open ban popup with reason msg

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

  closeModal() {
    this.isUserInfoModalOpen = false;
    this.isChangeRoleModalOpen = false;
    this.isBanUserModalOpen = false;
    this.isUnbanUserModalOpen = false;
    this.isDeleteUserModalOpen = false;
  }
}
