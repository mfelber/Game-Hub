import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {Router, RouterOutlet} from '@angular/router';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {GameResponse} from '../../../../services/models/game-response';
import {checkGameInWishlist} from '../../../../services/fn/game-controller/check-game-in-wishlist';
import {FormsModule} from '@angular/forms';
import {Game} from '../../../../services/models/game';
import {PlatformResponse} from '../../../../services/models/platform-response';

@Component({
  selector: 'app-store',
  imports: [
    NgForOf,
    NgIf,
    FormsModule
  ],
  templateUrl: './store.component.html',
  styleUrl: './store.component.scss'
})
export class StoreComponent implements OnInit{
  gamePageResponse: PageResponseGameResponse = {}
  gameResponse: GameResponse = {}
  platformResponse: {} = {}
  allPlatforms: string[] = [] ;
  allGenres: string[] = [] ;
  private _gameImageCover: string | undefined
  public page = 0;
  public size = 30;
  gameWishListMap: { [key: number]: boolean } = {};
  gamesOwnedMap: { [key: number]: boolean } = {};

  filteredGames: GameResponse[] = [];

  filters = {
    genre: '',
    platform: '',
    maxPrice: ''
  };


  constructor(
    private gameService: GameControllerService,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.getAllGames()
    this.getPlatforms();
    this.getGenres();
  }

  filterGames() {
    const maxPrice = Number(this.filters.maxPrice);
    const selectedPlatform = this.filters.platform
    const selectedGenre = this.filters.genre
    this.filteredGames = (this.gamePageResponse.content || []).filter(game  => {
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
    this.gameService.findAllGames({
      page:this.page,
      size:this.size
    }).subscribe({
      next: (games) => {
        this.gamePageResponse = games;
        this.gamePageResponse.content?.forEach(game => {
          this.checkIfGameIsInWishlist(game.gameId);
          this.filteredGames = [...(games.content || [])];

        });
        this.gamePageResponse.content?.forEach(game => {
          this.checkIfGameIsOwned(game.gameId);
        });
      }
    })
  }

  private checkIfGameIsInWishlist(gameId: any) {
    this.gameService.checkGameInWishlist({gameId})
      .subscribe({
        next: (inWishList: boolean) => {
          this.gameWishListMap[gameId] = inWishList;
        }
      })
  }

  private checkIfGameIsOwned(gameId: any) {
    this.gameService.checkGameOwned({gameId})
      .subscribe({
        next: (owned: boolean) => {
          this.gamesOwnedMap[gameId] = owned;
        }
      })
  }

  goToGame(gameId:any) {
    this.gameService.getGameById({gameId}).subscribe({
      next: (game) => {
        this.router.navigate(['gamehub/game', gameId]);
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
    this.gameService.getAllPlatforms().subscribe({
      next: (platforms) => {
        this.allPlatforms = platforms.map(p => p.platformName!);
      }
    })
  }

  private getGenres() {
    this.gameService.getAllGenres().subscribe({
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
}
