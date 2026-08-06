import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {LibraryControllerService} from '../../../../services/services/library-controller.service';
import {GameResponse} from '../../../../services/models/game-response';
import {StoreControllerService} from '../../../../services/services';
import {Router} from '@angular/router';
import {SearchBar} from '../../components/search-bar/search-bar';
import {PageResponseUserLibraryResponse} from '../../../../services/models/page-response-user-library-response';
import {UserLibraryResponse} from '../../../../services/models/user-library-response';

@Component({
  selector: 'app-library',
  imports: [
    NgForOf,
    NgIf,
    SearchBar
  ],
  templateUrl: './library.component.html',
  styleUrl: './library.component.scss'
})
export class LibraryComponent implements OnInit{

  gamePageResponse: PageResponseUserLibraryResponse = {};
  libraryResponse: UserLibraryResponse = {};
  public page = 0;
  public size = 15;
  emptyLibrary = false;
  loadFavoriteGames = false;
  loadDownloadedGames = false;
  loadAllGames = false;
  isLoaded = false;
  gamesDownloadedMap: { [key: number]: boolean } = {};

  ngOnInit() {
    this.getOwnedGame()
  }

  constructor(
    private libraryService: LibraryControllerService,
    private storeService: StoreControllerService,
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
        this.gamePageResponse.content?.forEach(game => {
          this.checkIfGameIsDownload(game.gameId);
        })
      }
    })
  }

  getDownloadedGames() {
    this.libraryService.getDownloadedGames({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (games) => {
        this.gamePageResponse = games;
        this.loadDownloadedGames = true;
        this.loadAllGames = false;
        this.loadFavoriteGames = false;
        this.gamePageResponse.content?.forEach(game => {
          this.checkIfGameIsDownload(game.gameId);
        })
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
          this.isLoaded = true;
          this.loadAllGames = true
          this.loadDownloadedGames = false;
          this.loadFavoriteGames = false;
          if (games.totalElements == 0) {
            this.emptyLibrary = true
          } else {
            this.emptyLibrary = false
          }
          this.gamePageResponse.content?.forEach(game => {
            this.checkIfGameIsDownload(game.gameId);
          })
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

  searchYourGames($event: string) {
    console.log('searchYourGames');
  }

  downloadGame(gameId:any) {
    console.log(gameId);
    this.libraryService.downloadGame({gameId}).subscribe({
      next: res => {
        console.log('game was downloaded');
        this.checkIfGameIsDownload(gameId);
      }
    })
  }

  checkIfGameIsDownload(gameId:any) {
    this.libraryService.checkDownloadedGame({gameId}).subscribe({
      next: (downloaded: boolean) => {
        this.gamesDownloadedMap[gameId] = downloaded;
      }
    })
  }
}
