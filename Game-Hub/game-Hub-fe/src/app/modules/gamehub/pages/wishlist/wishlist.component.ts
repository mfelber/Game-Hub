import {Component, OnInit} from '@angular/core';
import {WishlistControllerService} from '../../../../services/services/wishlist-controller.service';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {NgForOf, NgIf} from '@angular/common';
import {GameResponse} from '../../../../services/models/game-response';
import {Router} from '@angular/router';
import {MatCheckbox} from '@angular/material/checkbox';
import {FormsModule} from '@angular/forms';
import {platform} from 'node:os';
import {SearchBar} from '../../components/search-bar/search-bar';

@Component({
  selector: 'app-wishlist',
  imports: [
    NgForOf,
    NgIf,
    MatCheckbox,
    FormsModule,
    SearchBar
  ],
  templateUrl: './wishlist.component.html',
  styleUrl: './wishlist.component.scss'
})
export class WishlistComponent implements OnInit{

  gamePageResponse: PageResponseGameResponse = {};
  allPlatforms: string[] = [];
  allGenres: string[] = [];
  emptyWishlist = false;
  isLoaded = false;

  filteredGames: GameResponse[] = [];

  filters = {
    genre: '',
    platform: '',
    onSale: false
  }

  constructor(
    private wishListService: WishlistControllerService,
    private gameService: GameControllerService,
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
          this.gamePageResponse = games;
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
    this.filteredGames = (this.gamePageResponse.content || []).filter(game => {
      const platformMatch = !selectedPlatform || game.platforms!.some(platform => platform.platformName === selectedPlatform);
      const genreMatch = !selectedGenre || game.genres!.some(genre => genre.name === selectedGenre);
      // TODO
      // const saleMatch = !onSale || game.onSale;
      // return platformMatch && genreMatch && onSale;
      return platformMatch && genreMatch;
    })
  }

  resetFilters() {
    this.filters = {
      genre: '',
      platform: '',
      onSale: false
    };
    this.filteredGames = [...(this.gamePageResponse.content || [])];
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
}
