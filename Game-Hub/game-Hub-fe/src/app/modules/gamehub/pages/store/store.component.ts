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


@Component({
  selector: 'app-store',
  imports: [
    NgForOf,
    NgIf,
    FormsModule,
    SearchBar,
    NgClass,
    UserActionsComponent,
    EmptyStateComponent
  ],
  templateUrl: './store.component.html',
  styleUrl: './store.component.scss'
})
export class StoreComponent implements OnInit {
  gamePageResponse: PageResponseGameResponse = {}
  allPlatforms: string[] = [];
  allGenres: string[] = [];
  public page = 0;
  public size = 5;
  gameWishListMap: { [key: number]: boolean } = {};
  gamesOwnedMap: { [key: number]: boolean } = {};

  filteredGames: GameResponse[] = [];

  filters = {
    genre: '',
    platform: '',
    maxPrice: ''
  };

  isLoaded = false
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
      this.getAllGames();
    })

    this.getPlatforms();
    this.getGenres();
  }

  filterGames() {
    const maxPrice = Number(this.filters.maxPrice);
    const selectedPlatform = this.filters.platform;
    const selectedGenre = this.filters.genre;
    this.filteredGames = (this.gamePageResponse.content || []).filter(game => {
        const priceMatch = !maxPrice
          || (maxPrice === 101 && game.price! >= 100)
          || (maxPrice !== 101 && game.price! <= maxPrice);

        const platformMatch =
          !selectedPlatform ||
          game.platforms!.some(platform => platform.platformName === selectedPlatform);

        const genreMatch = !selectedGenre || game.genres!.some(genre => genre.name === selectedGenre)

        return priceMatch && platformMatch && genreMatch;
      }
    );
  }

  private getAllGames() {
    this.isLoading = true;
    this.storeService.findAllGames({
      page: this.page,
      size: this.size
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
        this.allPlatforms = platforms.map(p => p.platformName!);
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
      platform: '',
      maxPrice: ''
    };
    this.filteredGames = [...(this.gamePageResponse.content || [])]
  }

  buyGame(gameId: any) {
    console.log(gameId);
  }

  searchGames(query: string) {
    console.log(query);
  }

  goToFirstPage() {
    this.changePage(0)
  }

  goToPreviousPage() {
    this.changePage(this.page - 1)
  }

  goToPage(page: number) {
    this.changePage(page)
  }

  goToNextPage() {
    this.changePage(this.page + 1)
  }

  goToLastPage() {
    this.changePage(this.gamePageResponse.totalPages! - 1);
  }

  get isLastPage(): boolean {
    return this.page === this.gamePageResponse.totalPages as number - 1;
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

    this.getAllGames();
  }
}

