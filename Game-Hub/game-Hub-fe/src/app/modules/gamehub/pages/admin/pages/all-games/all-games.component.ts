import {Component, OnInit} from '@angular/core';
import {GamePreviewResponse} from '../../../../../../services/models/game-preview-response';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {Router} from '@angular/router';
import {PageResponseGamePreviewResponse} from '../../../../../../services/models/page-response-game-preview-response';
import {NgForOf, NgIf} from '@angular/common';
import {SearchBar} from '../../../../components/search-bar/search-bar';
import {ReactiveFormsModule} from '@angular/forms';
import {GameResponse} from '../../../../../../services/models/game-response';

@Component({
  selector: 'app-all-games',
  imports: [
    NgIf,
    SearchBar,
    NgForOf,
    ReactiveFormsModule
  ],
  templateUrl: './all-games.component.html',
  styleUrl: './all-games.component.scss',
})
export class AllGamesComponent implements OnInit {

  gamesResponse: PageResponseGamePreviewResponse = {}
  isLoaded = false;

  constructor(
    private adminControllerService: AdminControllerService,
    private router: Router,
  ) {
  }

  ngOnInit() {
    this.loadGamesTableData();
  }

  loadGamesTableData() {
    this.adminControllerService.getAllGames().subscribe({
      next: (data) => {
        this.gamesResponse = data;
        console.log(data);
        this.isLoaded = true;
      },
      error: (err) => {
        console.log(err);
        this.isLoaded = false;
      }
    })
  }

  getGameImageCover(game: GamePreviewResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }


  searchGames($event: string) {
    console.log($event);
  }
}
