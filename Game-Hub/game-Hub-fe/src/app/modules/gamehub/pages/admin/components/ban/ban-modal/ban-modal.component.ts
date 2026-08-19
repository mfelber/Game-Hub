import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AdminUserResponse} from '../../../../../../../services/models/admin-user-response';
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {ReportControllerService} from '../../../../../../../services/services/report-controller.service';
import {BanUserRequest} from '../../../../../../../services/models/ban-user-request';
import {AdminControllerService} from '../../../../../../../services/services/admin-controller.service';

@Component({
  selector: 'app-ban-modal',
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
  ],
  templateUrl: './ban-modal.component.html',
  styleUrl: './ban-modal.component.scss',
})
export class BanModalComponent implements OnInit {

  @Input() user!: AdminUserResponse;
  @Output() close = new EventEmitter<void>();
  @Output() bannedUser = new EventEmitter<string>();

  allBanReasons: { id: number; reason: string }[] = [];
  banRequest: BanUserRequest = { banReason: null!, customMessage: ''};
  errorMessage: string = '';

  constructor(private reportService: ReportControllerService, private adminService: AdminControllerService) {
  }

  ngOnInit() {
    this.loadBanReasons()
  }

  loadBanReasons() {
    this.reportService.getAllReportReasons().subscribe({next: (reason) => {
      this.allBanReasons = reason.map(r => ({
        id: r.id!,
        reason: r.reason!
      }));
      },
      error: (err) => {
      console.log(err);
      }
    })
  }

  banUser(userId: any) {
    this.errorMessage = '';
    if (this.banRequest.banReason === 6 && this.banRequest.customMessage === '') {
      this.errorMessage = 'Please write a reason for ban.'
      return;
    }
    if (this.banRequest.banReason !== null) {
      this.adminService.banUser({userId, body: this.banRequest}).subscribe({
        next: () => {
          this.banRequest = {
            banReason: undefined!,
            customMessage: '',
          };
          this.bannedUser.emit("User was banned successfully!");
          this.close.emit();
        }
      })
    } else {
      this.errorMessage = 'Please select a reason before submitting';
    }
  }

}
