import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {UserCommunityResponse} from '../../../../services/models/user-community-response';
import {ReportReasonResponse} from '../../../../services/models/report-reason-response';
import {ReportRequest} from '../../../../services/models/report-request';
import {ReportControllerService} from '../../../../services/services/report-controller.service';

@Component({
  selector: 'app-report-user-modal',
  imports: [
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './report-user-modal.component.html',
  styleUrl: './report-user-modal.component.css'
})
export class ReportUserModalComponent implements OnInit{

  @Input() isOpen: boolean = false;
  @Input() selectedUser: UserCommunityResponse | null = null;

  @Output() close = new EventEmitter<void>();
  @Output() submit = new EventEmitter<ReportRequest>();

  reportRequest: ReportRequest = { reason: null!, message: '' };
  errorMessage: string = '';
  allReportReasons: { id: number; reason: string }[] = [];

  constructor(private reportService: ReportControllerService) {
  }

  ngOnInit(): void {
    this.loadReportReasons();
    }

  private loadReportReasons() {
    this.reportService.getAllReportReasons().subscribe({
      next: (reasons) => {
        this.allReportReasons = reasons.map(r => ({
          id: r.id!,
          reason: r.reason!
        }));
      }
    })
  }

  closeModal() {
    this.isOpen = false;
    this.reportRequest = { reason: null!, message: '' };
    this.errorMessage = '';
    this.close.emit();
  }

  reportUser(userId: number) {
    this.errorMessage = '';
    if (this.reportRequest.reason === 6 && this.reportRequest.message === '') {
      this.errorMessage = 'Please write a reason for reporting';
      return;
    }
    if (this.reportRequest.reason !== null) {
      this.reportService.reportUser({userId, body: this.reportRequest}).subscribe({
        next: () => {
          this.reportRequest = {
            reason: undefined,
            message: ''
          };
          this.submit.emit(this.reportRequest);
          this.closeModal();
        }
      })
    }
    else {
      this.errorMessage = 'Please select a reason before submitting';
    }

  }
}
