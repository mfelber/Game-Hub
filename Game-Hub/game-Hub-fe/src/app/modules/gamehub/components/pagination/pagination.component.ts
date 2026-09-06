import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-pagination',
  imports: [
    NgForOf
  ],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.scss',
})
export class PaginationComponent {

  @Input() currentPage = 0;
  @Input() totalPages = 0;

  @Output() pageChange = new EventEmitter<number>();

  goToFirstPage() {
    this.changePage(0)
  }

  goToPreviousPage() {
    this.changePage(this.currentPage - 1)
  }

  goToPage(page: number) {
    this.changePage(page)
  }

  goToNextPage() {
    this.changePage(this.currentPage + 1)
  }

  goToLastPage() {
    this.changePage(this.totalPages - 1);
  }

  get isFirstPage(): boolean {
    return this.currentPage === 0;
  }

  get isLastPage(): boolean {
    return this.currentPage === this.totalPages as number - 1;
  }

  private changePage(page: number) {
    if (page < 0 || page >= this.totalPages) {
      return;
    }

    this.pageChange.emit(page);
  }

}
