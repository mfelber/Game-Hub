import {Component, OnInit} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {LibraryControllerService} from '../../../../services/services/library-controller.service';

@Component({
  selector: 'app-library',
    imports: [],
  templateUrl: './library.component.html',
  styleUrl: './library.component.scss'
})
export class LibraryComponent implements OnInit{

  gamePageResponse: PageResponseGameResponse = {}
  public page = 0;
  public size = 15;

  ngOnInit() {
    this.findOwnedGame()
  }

  constructor(
    private libraryService: LibraryControllerService
  ) {
  }

  private findOwnedGame() {
    this.libraryService.getLibrary({
      page: this.page,
      size: this.size
    }).subscribe(
      {
        next: (games) => {
          this.gamePageResponse = games;
          console.log('PageResponse from backend:', this.gamePageResponse);
        },
        error: (err) => {
          console.error('Error loading library:', err);
        }
      }
    )

  }
}
