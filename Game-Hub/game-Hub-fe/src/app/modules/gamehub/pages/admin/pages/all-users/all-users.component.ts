import {Component, OnInit} from '@angular/core';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {PageResponseAdminUserResponse} from '../../../../../../services/models/page-response-admin-user-response';

@Component({
  selector: 'app-all-users',
  imports: [],
  templateUrl: './all-users.component.html',
  styleUrl: './all-users.component.scss',
})
export class AllUsersComponent implements OnInit {

  usersResponse: PageResponseAdminUserResponse = {};

  constructor(
    private adminControllerService: AdminControllerService
  ) {
  }

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.adminControllerService.getAllUsers().subscribe({
      next: data => {
        this.usersResponse = data;
        console.log(data);
      }
    })
  }

}
