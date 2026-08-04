import {Component, OnInit} from '@angular/core';
import {WishlistControllerService} from '../../../../services/services/wishlist-controller.service';
import {StoreControllerService} from '../../../../services/services/store-controller.service';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {NgForOf, NgIf} from '@angular/common';
import {GameResponse} from '../../../../services/models/game-response';
import {Router} from '@angular/router';
import {MatCheckbox} from '@angular/material/checkbox';
import {FormsModule} from '@angular/forms';
import {platform} from 'node:os';
import {SearchBar} from '../../components/search-bar/search-bar';
import {WishlistResponse} from '../../../../services/models/wishlist-response';
import {PageResponseWishlistResponse} from '../../../../services/models/page-response-wishlist-response';

@Component({
  selector: 'app-wishlist',
  imports: [
    NgForOf,
    NgIf,
    FormsModule,
    SearchBar
  ],
  templateUrl: './wishlist.component.html',
  styleUrl: './wishlist.component.scss'
})
export class WishlistComponent implements OnInit{

  wishlistPageResponse: PageResponseWishlistResponse = {};
  filteredGames: WishlistResponse[] = [];
  allPlatforms: string[] = [];
  allGenres: string[] = [];
  emptyWishlist = false;
  isLoaded = false;


  filters = {
    genre: '',
    platform: '',
    onSale: false
  }

  constructor(
    private wishListService: WishlistControllerService,
    private gameService: StoreControllerService,
    private router: Router
  ) {
  }

  public page = 0;
  public size = 15;

  ngOnInit() {
    this.getAllGamesInWishlist();
    this.getPlatforms();
    this.getGenres();
  }

  getAllGamesInWishlist(){
    this.wishListService.getWishlist({
      page: this.page,
      size: this.size
    }).subscribe(
      {
        next: (games) => {
          this.wishlistPageResponse = games;
          console.log(games.content);
          this.isLoaded = true;
          if (games.totalElements == 0) {
            this.emptyWishlist = true
          } else {
            this.emptyWishlist = false
          }
        },
        error: (err) => {
          console.error('Error loading wishlist:', err);
        }
      }
    )
  }

  goToGame(gameId: any) {
    this.gameService.getGameById({gameId}).subscribe({
      next: (game) => {
        this.router.navigate(['gamehub/game', gameId]);
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

  filterGames() {
    const selectedPlatform = this.filters.platform;
    const selectedGenre = this.filters.genre;
    const onSale = this.filters.onSale;
    this.filteredGames = (this.wishlistPageResponse.content || []).filter(game => {
      // const platformMatch = !selectedPlatform || game.platforms!.some(platform => platform.platformName === selectedPlatform);
      const genreMatch = !selectedGenre || game.game!.genres!.some(genre => genre.name === selectedGenre);
      // TODO
      // const saleMatch = !onSale || game.onSale;
      // return platformMatch && genreMatch && onSale;
      // return platformMatch && genreMatch;
      return genreMatch;
    })
  }

  resetFilters() {
    this.filters = {
      genre: '',
      platform: '',
      onSale: false
    };
    this.filteredGames = [...(this.wishlistPageResponse.content || [])];
  }

  private getPlatforms() {
    this.gameService.getAllPlatforms().subscribe({
      next: (platforms) => {
        this.allPlatforms = platforms.map(p => p.platformName!)
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
}
