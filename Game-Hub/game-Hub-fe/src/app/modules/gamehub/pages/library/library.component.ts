import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {LibraryControllerService} from '../../../../services/services/library-controller.service';
import {GameResponse} from '../../../../services/models/game-response';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-library',
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './library.component.html',
  styleUrl: './library.component.scss'
})
export class LibraryComponent implements OnInit{

  gamePageResponse: PageResponseGameResponse = {};
  public page = 0;
  public size = 15;
  emptyLibrary = false;
  loadFavoriteGames = false;
  loadDownloadedGames = false;
  loadAllGames = false;

  ngOnInit() {
    this.getOwnedGame()
  }

  constructor(
    private libraryService: LibraryControllerService,
    private storeService: GameControllerService,
    private router: Router
  ) {
  }

  getFavoriteGames() {
    this.libraryService.getFavorites({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (games) => {
        this.gamePageResponse = games;
        this.loadAllGames = false;
        this.loadDownloadedGames = false;
        this.loadFavoriteGames = true;
      }
    })
  }

  getOwnedGame() {
    this.libraryService.getLibrary({
      page: this.page,
      size: this.size
    }).subscribe(
      {
        next: (games) => {
          this.gamePageResponse = games;
          this.loadAllGames = true
          this.loadDownloadedGames = false;
          this.loadFavoriteGames = false;
          if (games.totalElements == 0) {
            this.emptyLibrary = true
          } else {
            this.emptyLibrary = false
          }
        },
        error: (err) => {
          console.error('Error loading library:', err);
        }
      }
    )
  }

  goToGame(gameId:any) {
    this.storeService.getGameById({gameId}).subscribe({
      next: (game) => {
        console.log(game);
        this.router.navigate(['gamehub/library/game', gameId]);
      },
      error: (err) => {
        console.error('Error with loading game:', err);
      }
    });
  }

  getGameImageCover(game: GameResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }



}
