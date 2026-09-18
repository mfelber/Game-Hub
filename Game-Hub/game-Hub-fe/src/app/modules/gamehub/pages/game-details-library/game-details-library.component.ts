import {Component, OnInit} from '@angular/core';
import { DatePipe, NgStyle } from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {GameResponse} from '../../../../services/models/game-response';
import {LibraryControllerService} from '../../../../services/services/library-controller.service';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {UserLibraryResponse} from '../../../../services/models/user-library-response';
import {LoadingComponent} from '../../components/loading/loading.component';
import {PlayingWarningModalComponent} from '../../components/playing-warning/playing-warning-modal.component';

@Component({
  selector: 'app-game-details-library',
  imports: [
    NgStyle,
    DatePipe,
    UserActionsComponent,
    LoadingComponent,
    PlayingWarningModalComponent
],
  templateUrl: './game-details-library.component.html',
  styleUrl: './game-details-library.component.scss'
})
export class GameDetailsLibraryComponent implements OnInit {

  gameResponse: UserLibraryResponse = {};

  isLoading = false;
  userIsPlayingGame = false;
  cannotUninstallModalOpen = false;

  currentlyPlayingGameId: number | null = null;
  currentlyPlayingGame: UserLibraryResponse | null = null;


  constructor(
    private router: ActivatedRoute,
    private libraryService: LibraryControllerService,
  ) {
  }

  ngOnInit(): void {
    this.getInfoGame();
  }

  getInfoGame() {
    this.isLoading = true;
    const gameId: any = this.router.snapshot.paramMap.get('id')
    if (gameId) {
      this.libraryService.getLibraryGameById({gameId}).subscribe({
          next: (data) => {
            this.getCurrentlyPlayingGame();
            this.gameResponse = data;
            this.isLoading = false;
          },
          error: (e) => {
            this.isLoading = true;
            console.error(e);
          }
        },
      )
    }
  }

  getCurrentlyPlayingGame() {
    this.libraryService.currentlyPlaying().subscribe({
      next: data => {
        this.currentlyPlayingGame = data ?? null;
        this.currentlyPlayingGameId = data?.gameId ?? null;
      },
      error: err => {
        console.error(err);
        this.currentlyPlayingGame = null;
        this.currentlyPlayingGameId = null;
      }
    })
  }

  getGameImageCover(game: GameResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  downloadGame(game: UserLibraryResponse) {
    this.libraryService.downloadGame({gameId: game.gameId!}).subscribe({
      next: () => {
        game.installed = true;
      },
      error: (e) => {
        game.installed = false;
        console.error(e);
      }
    })
  }

  playGame(game: UserLibraryResponse) {
    if (this.currentlyPlayingGameId !== null && this.currentlyPlayingGameId !== game.gameId) {
      this.userIsPlayingGame = true;
      return;
    }
    this.libraryService.playGame({gameId: game.gameId!}).subscribe({
      next: () => {
        game.currentlyPlaying = true;

        this.currentlyPlayingGameId = game.gameId!;
        this.currentlyPlayingGame = game;
      }
    })
  }

  stopPlayingGame(game: UserLibraryResponse) {
    this.libraryService.stopPlayingGame({gameId: game.gameId!}).subscribe({
      next: (gameId: any) => {
        game.currentlyPlaying = false;

        this.currentlyPlayingGameId = null;
        this.currentlyPlayingGame = game;
      }
    })
  }

  uninstallGame(game: UserLibraryResponse) {
    if (game.currentlyPlaying) {
      console.log('nemozes odinstalovat ked ju hras')
      this.cannotUninstallModalOpen = true;
      return;
    }
    console.log('mozes odinstalovat ked ju nehras')

    this.libraryService.uninstallGame({gameId: game.gameId!}).subscribe({
      next: () => {
        game.installed = false;
      },
      error: (e) => {
        console.error(e);
      }
    })
  }

  addGameToFavorite(game: UserLibraryResponse) {
    this.libraryService.addGameToFavorites({gameId: game.gameId!}).subscribe({
      next: () => {
        game.favorite = true;
      },
      error: (e) => {
        console.error(e);
      }
    })
  }

  removeGameFromFavorite(game: UserLibraryResponse) {
    this.libraryService.removeGameFromFavorites({gameId: game.gameId!}).subscribe({
      next: () => {
        game.favorite = false;
      },
      error: (e) => {
        console.error(e);
      }
    })
  }

  gameStopped() {
    this.getInfoGame();
  }

  closeModal() {
    this.cannotUninstallModalOpen = false;
    this.userIsPlayingGame = false;
  }
}
