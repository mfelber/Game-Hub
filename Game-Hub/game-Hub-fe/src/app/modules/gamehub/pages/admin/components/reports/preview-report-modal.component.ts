import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {AdminReportsResponse} from '../../../../../../services/models/admin-reports-response';
import {FormsModule} from '@angular/forms';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {ReportControllerService} from '../../../../../../services/services/report-controller.service';
import {SuspendAccountRequest} from '../../../../../../services/models/suspend-account-request';
import {WarnUserRequest} from '../../../../../../services/models/warn-user-request';
import {BanUserRequest} from '../../../../../../services/models/ban-user-request';

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
  banning: boolean = false;
  selectedAction: 'NONE' | 'WARNING' | 'SUSPEND' | 'BAN' | undefined;
  selectedActionDrop: '7' | '15' | '30' | 'custom' | undefined;

  suspendRequest: SuspendAccountRequest = {customMessage: '', expiresAt: '', suspendReason: 0, reportId: 0};
  warningRequest: WarnUserRequest = {customMsg: '', reportId: 0};
  banRequest: BanUserRequest = {banReason: null!, reportId: 0, customMessage: null};

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
  selectedGuidelineReason: string = '';
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
      return;
    }

    if (this.isResolving && this.banning) {
      this.banning = false;
      return;
    }

    this.isResolving = false;
    this.selectedAction = undefined;

  }

  confirmResolution(userId: any) {
    switch (this.selectedAction) {
      case 'NONE':
        this.adminService.noActionOnReportedUser({reportId: this.report.reportId!}).subscribe({
          next: () => {
            this.close.emit();
            this.resolveReport.emit("Report was resolved!");
          }
        });
        break;
      case 'WARNING':
        this.warningRequest.reportId = this.report.reportId!;
        this.adminService.warnUser({
          userId: userId,
          body: this.warningRequest,
        }).subscribe({
          next: () => {
            this.close.emit();
            this.resolveReport.emit("User was successfully warned!");
          }
        })
        break;
      case 'SUSPEND':
        if (this.validateSuspendAccount()) {
          this.suspendRequest.reportId = this.report.reportId!;
          this.adminService.suspendAccount({
            userId: userId,
            body: this.suspendRequest
          }).subscribe({
            next: () => {
              console.log('suspend successfully!');
              this.close.emit();
              this.resolveReport.emit("User was successfully suspended!");
            }
          })
        }
        break;
      case 'BAN':
        console.log('ban action');
        if (this.validateBanAccount()) {

          const request: BanUserRequest = {
            reportId: this.report.reportId!,
            banReason: this.banRequest.banReason,
            customMessage: this.banRequest.customMessage?.trim() || null,
          }

          this.adminService.banUser({
            userId: userId,
            body: request
          }).subscribe({
            next: () => {
              this.banRequest = {
                reportId: 0,
                banReason: null!,
                customMessage: null!,
              };
              this.resolveReport.emit("User was successfully banned!");
              this.close.emit();
            }
          })
        }
    }

  }

  validateBanAccount(): boolean {
    const {banReason, customMessage} = this.banRequest;
    if (banReason === 15 && !customMessage?.trim()) {
      return false;
    }
    return true
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
    this.adminService.rejectReport({reportId: this.report.reportId!}).subscribe({
      next: () => {
        this.close.emit();
        this.resolveReport.emit("Report was rejected!");
      }
    })
  }
  
  next() {
    if (this.selectedAction === 'SUSPEND') {
      this.suspending = true;
    }
    if (this.selectedAction === 'BAN') {
      this.banning = true;
    }
  }

  setDuration() {
    if (this.selectedActionDrop == '7' ||
      this.selectedActionDrop == '15' ||
      this.selectedActionDrop === '30' ||
      this.selectedActionDrop == 'custom') {
      this.suspendRequest.expiresAt = this.selectedActionDrop;
    }
    if (this.selectedActionDrop == 'custom') {
      this.suspendRequest.expiresAt = '';
    }
  }

  onActionChange() {
    if (this.selectedAction !== "WARNING") {
      this.warningRequest = {
        customMsg: '',
        reportId: 0
      }
    }
    if (this.selectedAction !== "SUSPEND") {
      this.selectedGuidelineReason = '';
      this.selectedActionDrop = undefined;
      this.selectedCategory = null;
      this.suspendRequest = {
        customMessage: '',
        expiresAt: '',
        suspendReason: 0,
        reportId: 0
      }
    }
    if (this.selectedAction !== "BAN") {
      this.selectedGuidelineReason = '';
      this.selectedCategory = null;
      this.banRequest = {
        banReason: null!,
        reportId: 0,
        customMessage: null
      }
    }
  }

  get isNextDisabled(): boolean {
    if (this.selectedAction === 'SUSPEND') {
      return (this.suspendRequest.suspendReason === 0)
    }

    if (this.selectedAction === 'BAN') {
      return (this.banRequest.banReason === null)
    }
    return false;
  }

  get isConfirmDisabled(): boolean {

    if (this.selectedAction === 'NONE') {
      return false;
    }

    if (this.selectedAction === 'WARNING') {
      return !this.warningRequest.customMsg?.trim();
    }

    if (this.selectedAction === 'SUSPEND') {
      return (!this.suspendRequest.expiresAt || this.selectedActionDrop === undefined || !this.suspendRequest.customMessage?.trim());
    }

    if (this.selectedAction === 'BAN') {
      return !this.validateBanAccount();
    }

    return true;
  }
}
