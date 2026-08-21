import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgClass, NgIf} from '@angular/common';
import {AdminReportsResponse} from '../../../../../../services/models/admin-reports-response';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-preview-report-modal',
  imports: [
    NgIf,
    NgClass,
    FormsModule
  ],
  templateUrl: './preview-report-modal.component.html',
  styleUrl: './preview-report-modal.component.scss',
})
export class PreviewReportModalComponent {

  @Input() report!: AdminReportsResponse
  @Output() close = new EventEmitter<void>();
  @Output() resolveReport = new EventEmitter<string>();

  isResolving = false;
  selectedAction: 'NONE' | 'WARNING' | 'SUSPEND' | 'BAN' | undefined;

  openResolve() {
    this.isResolving = true;
  }

  closeResolve() {
    this.isResolving = false;
    this.selectedAction = undefined;
  }

  confirmResolution() {
    switch (this.selectedAction) {
      case 'NONE':
        console.log('no action');
        break;
      case 'WARNING':
        console.log('warning action');
        break;
      case 'SUSPEND':
        console.log('suspend action');
        break;
      case 'BAN':
        console.log('ban action');
        break;
    }

  }

  formatStatus(status?: string) {
    return status?.replace('_', ' ') ?? '';
  }

  rejectReport() {
    console.log('Reject Report');
  }

//   when close modal and status was change to reject/resolved/inreview reload table
}
