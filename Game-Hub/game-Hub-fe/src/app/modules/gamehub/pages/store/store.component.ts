import {Component, OnInit} from '@angular/core';
import {NgClass, NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {StoreControllerService} from '../../../../services/services';
import {ActivatedRoute, Router} from '@angular/router';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {GameResponse} from '../../../../services/models/game-response';
import {FormsModule} from '@angular/forms';
import {SearchBar} from '../../components/search-bar/search-bar';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {EmptyStateComponent} from '../../components/empty-state/empty-state.component';
import {PaginationComponent} from '../../components/pagination/pagination.component';


@Component({
  selector: 'app-store',
  imports: [
    NgForOf,
    NgIf,
    FormsModule,
    SearchBar,
    NgClass,
    UserActionsComponent,
    EmptyStateComponent,
    PaginationComponent
  ],
  templateUrl: './store.component.html',
  styleUrl: './store.component.scss'
})
export class StoreComponent implements OnInit {
  gamePageResponse: PageResponseGameResponse = {}
  allOperationSystems: string[] = [];
  allGenres: string[] = [];
  public page = 0;
  public size = 12;
  gameWishListMap: { [key: number]: boolean } = {};
  gamesOwnedMap: { [key: number]: boolean } = {};

  filteredGames: GameResponse[] = [];

  filters = {
    genre: '',
    operationSystem: '',
    maxPrice: '',
    discount: false
  };

  isLoading = false;


  constructor(
    private storeService: StoreControllerService,
    private router: Router,
    private route: ActivatedRoute,
  ) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.page = Number(params['page'] ?? 1) - 1;

      this.filters.genre = params['genre'] ?? '';
      this.filters.operationSystem = params['operationSystem'] ?? '';
      this.filters.maxPrice = params['maxPrice'] ?? '';
      this.filters.discount = params['discount'] === 'true';

      this.getAllGames();
    })

    this.getPlatforms();
    this.getGenres();
  }

  private getAllGames() {
    this.isLoading = true;

    const maxPrice = this.filters.maxPrice ? Number(this.filters.maxPrice) : undefined;

    this.storeService.findAllGames({
      page: this.page,
      size: this.size,
      genre: this.filters.genre || undefined,
      operationSystem: this.filters.operationSystem || undefined,
      maxPrice,
      discount: this.filters.discount || undefined,
    }).subscribe({
      next: (games) => {
        this.gamePageResponse = games;
        this.filteredGames = [...(games.content || [])];

        games.content?.forEach(game => {
          this.checkIfGameIsInWishlist(game.gameId);
          this.checkIfGameIsOwned(game.gameId);
        });
        this.isLoading = false;
      },
      error: err => {
        console.log(err);
        this.isLoading = false;
      }
    })
  }

  private checkIfGameIsInWishlist(gameId: any) {
    this.storeService.checkGameInWishlist({gameId})
      .subscribe({
        next: (inWishList: boolean) => {
          this.gameWishListMap[gameId] = inWishList;
        }
      })
  }

  private checkIfGameIsOwned(gameId: any) {
    this.storeService.checkGameOwned({gameId})
      .subscribe({
        next: (owned: boolean) => {
          this.gamesOwnedMap[gameId] = owned;
        }
      })
  }

  goToGame(gameId: any) {
    this.storeService.getGameById({gameId}).subscribe({
      next: (game) => {
        this.router.navigate(['gamehub/store/game', gameId]);
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

  private getPlatforms() {
    this.storeService.getAllPlatforms().subscribe({
      next: (platforms) => {
        this.allOperationSystems = platforms.map(p => p.platformName!);
      }
    })
  }

  private getGenres() {
    this.storeService.getAllGenres().subscribe({
      next: (genres) => {
        this.allGenres = genres.map(g => g.name!);
      }
    })
  }

  resetFilters() {
    this.filters = {
      genre: '',
      operationSystem: '',
      maxPrice: '',
      discount: false
    };
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        genre: null,
        operationSystem: null,
        maxPrice: null,
        discount: false
      },
      queryParamsHandling: 'merge'
    })
  }

  buyGame(gameId: any) {
    console.log(gameId);
  }

  searchGames(query: string) {
    console.log(query);
  }

  changePage(page: number) {
    this.page = page;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: page + 1
      },
      queryParamsHandling: 'merge'
    });
  }

  changeFilter() {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        genre: this.filters.genre || null,
        operationSystem: this.filters.operationSystem || null,
        maxPrice: this.filters.maxPrice || undefined,
        discount: this.filters.discount ? true : null
      }
    })
  }
}

