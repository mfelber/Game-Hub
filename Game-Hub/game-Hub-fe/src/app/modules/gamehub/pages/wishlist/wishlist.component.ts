import {Component, OnInit} from '@angular/core';
import {WishlistControllerService} from '../../../../services/services/wishlist-controller.service';
import {StoreControllerService} from '../../../../services/services/store-controller.service';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {GameResponse} from '../../../../services/models/game-response';
import {ActivatedRoute, Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {SearchBar} from '../../components/search-bar/search-bar';
import {WishlistResponse} from '../../../../services/models/wishlist-response';
import {PageResponseWishlistResponse} from '../../../../services/models/page-response-wishlist-response';
import {EmptyStateComponent} from '../../components/empty-state/empty-state.component';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {PaginationComponent} from '../../components/pagination/pagination.component';

@Component({
  selector: 'app-wishlist',
  imports: [
    NgForOf,
    NgIf,
    FormsModule,
    SearchBar,
    NgClass,
    DatePipe,
    EmptyStateComponent,
    UserActionsComponent,
    PaginationComponent,
  ],
  templateUrl: './wishlist.component.html',
  styleUrl: './wishlist.component.scss'
})
export class WishlistComponent implements OnInit{

  wishlistPageResponse: PageResponseWishlistResponse = {};
  filteredGames: WishlistResponse[] = [];
  allOperationSystems: string[] = [];
  allGenres: string[] = [];
  isLoading = false;

  filters = {
    genre: '',
    operationSystem: '',
    discount: false
  }

  sortBy = 'recentlyAdded'

  constructor(
    private wishListService: WishlistControllerService,
    private gameService: StoreControllerService,
    private router: Router,
    private route: ActivatedRoute,
  ) {
  }

  public page = 0;
  public size = 1;

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.page = Number(params['page'] ?? 1) - 1;

      this.filters.genre = params['genre'] ?? '';
      this.filters.operationSystem = params['operationSystem'] ?? '';
      this.filters.discount = params['discount'] === 'true';

      this.sortBy = params['sortBy'] ?? 'recentlyAdded';

      this.getAllGamesInWishlist();
    })

    this.getPlatforms();
    this.getGenres();
  }

  get hasActiveFilters(): boolean {
    return !!(
      this.filters.genre ||
      this.filters.operationSystem ||
      this.filters.discount
    );
  }

  getAllGamesInWishlist(){
    this.isLoading = true;
    this.filteredGames = [];
    this.wishListService.getWishlist({
      page: this.page,
      size: this.size,
      genre: this.filters.genre || undefined,
      operationSystem: this.filters.operationSystem || undefined,
      discount: this.filters.discount || undefined,
      sortBy: this.sortBy
    }).subscribe(
      {
        next: (games) => {
          this.wishlistPageResponse = games;
          this.filteredGames = [...(games.content || [])];
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error loading wishlist:', err);
          this.isLoading = false;
        }
      }
    )
  }

  goToGame(gameId: any) {
    this.gameService.getGameById({gameId}).subscribe({
      next: (game) => {
        this.router.navigate(['gamehub/store/game', gameId]);
      },
      error: (err) => {
        console.error('Error with loading game:', err);
      }
    });

  }

  getGameImageCover(game: GameResponse) {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  changeFilter() {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        genre: this.filters.genre || null,
        operationSystem: this.filters.operationSystem || null,
        discount: this.filters.discount ? true : null
      }
    })
  }

  changeSort() {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        sortBy: this.sortBy,
      },
      queryParamsHandling: 'merge',
    })
  }

  resetFilters() {
    this.filters = {
      genre: '',
      operationSystem: '',
      discount: false
    };
    this.sortBy = 'recentlyAdded';
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: 1,
        genre: null,
        operationSystem: null,
        discount: null,
        sortBy: null,
      },
      queryParamsHandling: 'merge'
    })
  }

  private getPlatforms() {
    this.gameService.getAllPlatforms().subscribe({
      next: (platforms) => {
        this.allOperationSystems = platforms.map(p => p.platformName!)
      }
    })
  }

  private getGenres() {
    this.gameService.getAllGenres().subscribe({
      next: (genres) => {
        this.allGenres = genres.map(g => g.name!)
      }
    })
  }

  searchWishlistedGames() {
    console.log('searchWishlistedGames');
  }

  removeGameFromWishList(gameId: any) {
    this.gameService.removeGameFromWishlist({gameId})
      .subscribe({
        next: () => {
          console.log('game with id: ' + gameId + ' removed from wishlist');
          this.getAllGamesInWishlist();
        },
        error: (err) => {
          console.error('Error with buying game:', err);
        }
      })
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
}
