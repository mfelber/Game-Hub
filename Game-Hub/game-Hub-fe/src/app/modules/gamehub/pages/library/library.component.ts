import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {LibraryControllerService} from '../../../../services/services/library-controller.service';
import {GameResponse} from '../../../../services/models/game-response';
import {StoreControllerService} from '../../../../services/services';
import {ActivatedRoute, Router} from '@angular/router';
import {SearchBar} from '../../components/search-bar/search-bar';
import {PageResponseUserLibraryResponse} from '../../../../services/models/page-response-user-library-response';
import {UserLibraryResponse} from '../../../../services/models/user-library-response';
import {EmptyStateComponent} from '../../components/empty-state/empty-state.component';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {PaginationComponent} from '../../components/pagination/pagination.component';
import {PlayingWarningModalComponent} from '../../components/playing-warning/playing-warning-modal.component';

@Component({
  selector: 'app-library',
  imports: [
    NgForOf,
    NgIf,
    SearchBar,
    EmptyStateComponent,
    UserActionsComponent,
    PaginationComponent,
    PlayingWarningModalComponent
  ],
  templateUrl: './library.component.html',
  styleUrl: './library.component.scss'
})
export class LibraryComponent implements OnInit {

  gamePageResponse: PageResponseUserLibraryResponse = {};
  libraryResponse: UserLibraryResponse = {};
  public page = 0;
  public size = 12;
  emptyLibrary = false;
  emptyFavoriteGames = false;
  emptyDownloadedGames = false;
  loadFavoriteGames = false;
  loadDownloadedGames = false;
  loadAllGames = false;
  isLoading = false;
  activeFilter = 'ALL';

  currentlyPlayingGameId : any = undefined;
  currentlyPlayingGame: UserLibraryResponse = {};

  userIsPlayingGame = false;

  constructor(
    private libraryService: LibraryControllerService,
    private storeService: StoreControllerService,
    private router: Router,
    private route: ActivatedRoute,
  ) {
  }

  ngOnInit() {
    this.loadCurrentlyPlayingGame();
    this.route.queryParams.subscribe(params => {
      this.page = Number(params['page'] ?? 1) - 1;
      this.activeFilter = params['filter'] ?? 'ALL';
      this.loadCurrentGames();
    });
  }

  loadCurrentGames() {
    switch (this.activeFilter) {
      case 'ALL':
        this.getOwnedGame();
        break;
      case 'FAVORITE':
        this.getFavoriteGames();
        break;
      case 'DOWNLOADED':
        this.getDownloadedGames();
        break;
    }
  }

  setFilter(filter: string) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        filter: filter
      },
      queryParamsHandling: 'merge'
    });

  }

  getFavoriteGames() {
    this.isLoading = true;
    this.libraryService.getFavorites({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (games) => {
        this.gamePageResponse = games;
        this.emptyFavoriteGames = games.totalElements === 0;
        this.loadFavoriteGames = true;
        this.loadAllGames = false;
        this.loadDownloadedGames = false;
        this.currentlyPlayingGameId = games.content?.find(game => game.currentlyPlaying)?.gameId ?? null
        this.isLoading = false;
        console.log(this.currentlyPlayingGameId);
        console.log(this.gamePageResponse);
      },
      error: (e) => {
        this.isLoading = true;
        console.error(e);
      }
    })
  }

  getDownloadedGames() {
    this.isLoading = true;
    this.libraryService.getDownloadedGames({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (games) => {
        this.gamePageResponse = games;
        this.emptyDownloadedGames = games.totalElements === 0;
        this.currentlyPlayingGameId = games.content?.find(game => game.currentlyPlaying)?.gameId ?? null
        this.loadAllGames = false;
        this.loadFavoriteGames = false;
        this.loadDownloadedGames = true;
        this.isLoading = false;
      },
      error: (e) => {
        this.isLoading = true;
      }
    })
  }

  getOwnedGame() {
    this.isLoading = true;
    this.libraryService.getLibrary({
      page: this.page,
      size: this.size
    }).subscribe(
      {
        next: (games) => {
          this.gamePageResponse = games;
          console.log(this.gamePageResponse);
          this.loadDownloadedGames = false;
          this.loadFavoriteGames = false;
          this.emptyLibrary = games.totalElements === 0;
          this.currentlyPlayingGameId = games.content?.find(game => game.currentlyPlaying)?.gameId ?? null
          this.isLoading = false;
          this.loadAllGames = true
        },
        error: (err) => {
          console.error('Error loading library:', err);
          this.isLoading = true;
        }
      }
    )
  }

  goToGame(gameId: any) {
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

  downloadGame(game: UserLibraryResponse) {
    this.libraryService.downloadGame({gameId: game.gameId!}).subscribe({
      next: res => {
        console.log('game was downloaded');
        game.installed = true;
      }
    })
  }

  changePage(page: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: page + 1
      },
      queryParamsHandling: 'merge'
    });
  }

  playGame(game: UserLibraryResponse) {

    if (this.currentlyPlayingGameId !== null && this.currentlyPlayingGameId !== game.gameId) {
      this.userIsPlayingGame = true;
      return;
    }

    this.libraryService.playGame({gameId: game.gameId!}).subscribe({
      next: (gameId: any) => {
        game.currentlyPlaying = true;
        this.currentlyPlayingGameId = game.gameId;
        this.loadCurrentlyPlayingGame();
      }
    })
  }

  stopPlayingGame(game: UserLibraryResponse) {
    this.libraryService.stopPlayingGame({gameId: game.gameId!}).subscribe({
      next: (gameId: any) => {
        this.loadCurrentlyPlayingGame()
        game.currentlyPlaying = false;
      }
    })
  }

  closeModal() {
    this.userIsPlayingGame = false;
  }

  loadCurrentlyPlayingGame() {
    this.libraryService.currentlyPlaying().subscribe({
      next: data => {
        this.currentlyPlayingGame = data;
        this.currentlyPlayingGameId = data?.gameId ?? null;
      },
      error: (e) => {
        this.currentlyPlayingGame = {};
        this.currentlyPlayingGameId = null;
      }
    })
  }

  gameStopped() {
    this.loadCurrentGames();
  }
}
