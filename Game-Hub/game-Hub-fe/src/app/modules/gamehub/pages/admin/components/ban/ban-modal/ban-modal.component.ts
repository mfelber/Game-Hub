import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {AdminUserResponse} from '../../../../../../../services/models/admin-user-response';
import {FormsModule} from '@angular/forms';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {ReportControllerService} from '../../../../../../../services/services/report-controller.service';
import {BanUserRequest} from '../../../../../../../services/models/ban-user-request';
import {AdminControllerService} from '../../../../../../../services/services/admin-controller.service';

@Component({
  selector: 'app-ban-modal',
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    NgClass,
  ],
  templateUrl: './ban-modal.component.html',
  styleUrl: './ban-modal.component.scss',
})
export class BanModalComponent implements OnInit {

  @Input() user!: AdminUserResponse;
  @Output() close = new EventEmitter<void>();
  @Output() bannedUser = new EventEmitter<string>();

  allCommunityGuidelines: { id: number; reason: string; category: { id: number; categoryName: string } }[] = [];
  banRequest: BanUserRequest = { banReason: null!, customMessage: null};
  errorMessage: string = '';

  categories = [
    'Abuse & Harassment',
    'Inappropriate Content',
    'Spam & Scams',
    'Privacy & Identity',
    'Rules & Fair Play',
    'Other'
  ]
  selectedCategory: string | null = null;

  constructor(private reportService: ReportControllerService, private adminService: AdminControllerService) {
  }

  ngOnInit() {
    this.loadCommunityGuidelines()
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

  banUser(userId: any) {
    this.errorMessage = '';
    if (this.banRequest.banReason === 15 && !this.banRequest.customMessage?.trim()) {
      this.errorMessage = 'Please write a reason for ban.'
      return;
    }
    if (this.banRequest.banReason !== null) {

      const request: BanUserRequest = {
        banReason: this.banRequest.banReason,
        customMessage: this.banRequest.customMessage?.trim() || null,
      }

      this.adminService.banUser({userId, body: request}).subscribe({
        next: () => {
          this.banRequest = {
            banReason: null!,
            customMessage: null,
          };
          this.bannedUser.emit("User was banned successfully!");
          this.close.emit();
        }
      })
    } else {
      this.errorMessage = 'Please select a reason before submitting';
    }
  }

  selectCategory(categoryName: string) {
    this.selectedCategory = this.selectedCategory === categoryName ? null : categoryName;
  }

  getGuidelineByCategory(categoryName: string) {
    return this.allCommunityGuidelines.filter(
      guideline => guideline.category.categoryName === categoryName
    )
  }
}
