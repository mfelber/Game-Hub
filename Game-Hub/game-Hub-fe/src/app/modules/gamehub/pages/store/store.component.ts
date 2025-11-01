import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {Router, RouterOutlet} from '@angular/router';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {GameResponse} from '../../../../services/models/game-response';
import {checkGameInWishlist} from '../../../../services/fn/game-controller/check-game-in-wishlist';
import {FormsModule} from '@angular/forms';
import {Game} from '../../../../services/models/game';

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
    this.findAllGames()
  }

  filterGames() {
    const maxPrice = Number(this.filters.maxPrice);
    this.filteredGames = (this.gamePageResponse.content || []).filter(game =>
      !maxPrice
      || (maxPrice === 101 && game.price! >= 100)
      || (maxPrice !== 101 && game.price! <= maxPrice)
    );
  }

  private findAllGames() {
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
        console.log(game);
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
}
