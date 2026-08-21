import {Component, OnInit} from '@angular/core';
import {SearchBar} from '../../../../components/search-bar/search-bar';
import {AdminReportsResponse} from '../../../../../../services/models/admin-reports-response';
import {PageResponseAdminUserResponse} from '../../../../../../services/models/page-response-admin-user-response';
import {PageResponseAdminReportsResponse} from '../../../../../../services/models/page-response-admin-reports-response';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {ReportStatusResponse} from '../../../../../../services/models/report-status-response';
import {FormsModule} from '@angular/forms';
import {PreviewReportModalComponent} from '../../components/reports/preview-report-modal.component';

@Component({
  selector: 'app-all-reports',
  imports: [
    SearchBar,
    NgForOf,
    DatePipe,
    FormsModule,
    NgIf,
    NgClass,
    PreviewReportModalComponent
  ],
  templateUrl: './all-reports.component.html',
  styleUrl: './all-reports.component.scss',
})
export class AllReportsComponent implements OnInit {

  reportResponse: PageResponseAdminReportsResponse = {};
  reportStatusesResponse: ReportStatusResponse[] = [];
  selectedReport: AdminReportsResponse = {};

  filteredReportedUsers: AdminReportsResponse[] = [];
  filters = {
    reportStatus: ''
  }

  isLoaded = true;
  isPreviewModalOpen = false;

  successMessage: string | null = null;
  toastVisible = false;

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
        this.filteredReportedUsers = [...(data.content || [])];
        this.reportResponse = data;
        console.log(data);
      }
    })
    this.adminService.getAllReportStatuses().subscribe({
      next: data => {
        this.reportStatusesResponse = data;
      }
    })
  }

  formatStatus(status?: string) {
    return status ?.replace('_', ' ') ?? '';
  }

  filterReportedUsers() {
    const selectedReportStatus = this.filters.reportStatus;
    this.filteredReportedUsers = (this.reportResponse.content || []).filter(report => {
      const selectedReportStatusMatch = !selectedReportStatus || report.reportStatus === selectedReportStatus;

      return selectedReportStatusMatch;
    })
  }

  openReportPreview(reportedUser: AdminReportsResponse) {
    this.isPreviewModalOpen = true;
    this.selectedReport = {};
    this.selectedReport = reportedUser;
  }

  closeModal() {
    this.loadReports();
    this.isPreviewModalOpen = false;
  }

  showSuccess(message: string) {
    this.loadReports();
    this.successMessage = message;

    setTimeout(() => this.toastVisible = true, 10);

    setTimeout(() => this.hideToast(), 3000);

  }

  hideToast() {
    this.toastVisible = false;

    setTimeout(() => this.successMessage = null, 500);
  }
}
