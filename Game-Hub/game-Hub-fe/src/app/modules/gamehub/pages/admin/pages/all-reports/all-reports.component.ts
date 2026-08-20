import {Component, OnInit} from '@angular/core';
import {SearchBar} from '../../../../components/search-bar/search-bar';
import {AdminReportsResponse} from '../../../../../../services/models/admin-reports-response';
import {PageResponseAdminUserResponse} from '../../../../../../services/models/page-response-admin-user-response';
import {PageResponseAdminReportsResponse} from '../../../../../../services/models/page-response-admin-reports-response';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {DatePipe, NgForOf} from '@angular/common';

@Component({
  selector: 'app-all-reports',
  imports: [
    SearchBar,
    NgForOf,
    DatePipe
  ],
  templateUrl: './all-reports.component.html',
  styleUrl: './all-reports.component.scss',
})
export class AllReportsComponent implements OnInit {

  reportResponse: PageResponseAdminReportsResponse = {};

  isLoaded = true;

  constructor(
    private adminService: AdminControllerService
  ) {
  }

  ngOnInit() {
    this.loadReports();
  }

  loadReports() {
    this.adminService.getAllReports().subscribe({
      next: data => {
        this.reportResponse = data;
        console.log(data);
      }
    })
  }

}
