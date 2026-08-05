import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {DashboardResponse} from '../../../../../../services/models/dashboard-response';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-dashboard',
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {

  dashboardPageResponse: DashboardResponse = {}
  isLoaded = false

  constructor(
    private adminControllerService: AdminControllerService,
    private router: Router,
  ) {
  }

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    console.log("loadDashboardData called");
    this.adminControllerService.loadDashboardData().subscribe({
      next: (data) => {
        this.dashboardPageResponse = data;
        console.log(this.dashboardPageResponse);
        console.log(data);
        this.isLoaded = true;
      },
      error: (error) => {
        console.log(error);
        this.isLoaded = false;
      }
    })
  }

}
