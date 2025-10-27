import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {Router, RouterOutlet} from '@angular/router';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {GameResponse} from '../../../../services/models/game-response';
import {checkGameInWishlist} from '../../../../services/fn/game-controller/check-game-in-wishlist';

@Component({
  selector: 'app-store',
  imports: [
    NgForOf,
    NgIf
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

  constructor(
    private gameService: GameControllerService,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.findAllGames()
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
