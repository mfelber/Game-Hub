import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {AdminReportsResponse} from '../../../../../../services/models/admin-reports-response';
import {FormsModule} from '@angular/forms';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {ReportControllerService} from '../../../../../../services/services/report-controller.service';
import {SuspendAccountRequest} from '../../../../../../services/models/suspend-account-request';

@Component({
  selector: 'app-preview-report-modal',
  imports: [
    NgIf,
    NgClass,
    FormsModule,
    NgForOf
  ],
  templateUrl: './preview-report-modal.component.html',
  styleUrl: './preview-report-modal.component.scss',
})
export class PreviewReportModalComponent implements OnInit {

  @Input() report!: AdminReportsResponse
  @Output() close = new EventEmitter<void>();
  @Output() resolveReport = new EventEmitter<string>();

  isResolving = false;
  suspending: boolean = false;
  selectedAction: 'NONE' | 'WARNING' | 'SUSPEND' | 'BAN' | undefined;
  selectedActionDrop: '7' | '15' | '30' | 'custom' | undefined;
  suspendRequest: SuspendAccountRequest = {customMessage: '', expiresAt: '', suspendReason: 0};

  allCommunityGuidelines: { id: number; reason: string; category: { id: number; categoryName: string } }[] = [];
  categories = [
    'Abuse & Harassment',
    'Inappropriate Content',
    'Spam & Scams',
    'Privacy & Identity',
    'Rules & Fair Play',
    'Other'
  ]
  selectedCategory: string | null = null;
  selectedSuspendReason: string = '';
  minDate: string = '';

  constructor(
    private adminService: AdminControllerService,
    private reportService: ReportControllerService
  ) {
  }

  ngOnInit() {
    this.loadCommunityGuidelines();
    this.setMinDate();
  }

  loadCommunityGuidelines() {
    this.reportService.getAllCommunityGuidelines().subscribe({
      next: (communityGuidelines) => {
        this.allCommunityGuidelines = communityGuidelines.map(r => ({
            id: r.id!,
            reason: r.communityGuideline!,
            category: {
              id: r.category?.id!,
              categoryName: r.category?.categoryName!
            }
          })
        )
        console.log(this.allCommunityGuidelines);
      }
    })
  }

  setMinDate() {
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.minDate = tomorrow.toISOString().split('T')[0];
  }

  getGuidelineByCategory(categoryName: string) {
    return this.allCommunityGuidelines.filter(
      guideline => guideline.category.categoryName === categoryName
    )
  }

  selectCategory(categoryName: string) {
    this.selectedCategory = this.selectedCategory === categoryName ? null : categoryName;
  }

  openResolve() {
    this.isResolving = true;
  }

  closeResolve() {
    if (this.isResolving && this.suspending) {
      this.suspending = false;
      this.isResolving = true;
    } else {
      this.isResolving = false;
      this.selectedAction = undefined;
    }

  }


  confirmResolution(userId: any) {
    console.log(this.suspendRequest);
    switch (this.selectedAction) {
      case 'NONE':
        console.log('no action');
        break;
      case 'WARNING':
        console.log('warning action');
        break;
      case 'SUSPEND':
        if (this.validateSuspendAccount()) {
          this.adminService.suspendAccount({
            userId: userId,
            body: this.suspendRequest
          }).subscribe({
            next: () => {
              console.log('suspend successfully!');
            }
          })
        }
        break;
      case 'BAN':
        console.log('ban action');
        break;
    }

  }

  validateSuspendAccount(): boolean {
    const {customMessage, expiresAt, suspendReason} = this.suspendRequest;
    if (!customMessage.trim()) {
      return false;
    }

    if (this.selectedActionDrop === undefined || expiresAt === '') {
      return false;
    }

    if (suspendReason === null) {
      return false;
    }
    return true;
  }

  formatStatus(status?: string) {
    return status?.replace('_', ' ') ?? '';
  }

  rejectReport() {
    console.log('Reject Report');
  }

//   when close modal and status was change to reject/resolved/inreview reload table

  next() {
    console.log(this.suspendRequest.suspendReason);
    this.suspending = true;
  }

  setDuration() {
    if (this.selectedActionDrop == '7'||
      this.selectedActionDrop == '15' ||
      this.selectedActionDrop === '30'||
      this.selectedActionDrop == 'custom') {
      this.suspendRequest.expiresAt = this.selectedActionDrop;
    }
    if (this.selectedActionDrop == 'custom') {
      this.suspendRequest.expiresAt = '';
    }
  }
}
