import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {CartControllerService, StoreControllerService} from '../../../../services/services';
import { DatePipe, NgClass, NgStyle } from '@angular/common';
import {GameResponse} from '../../../../services/models/game-response';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {LoadingComponent} from '../../components/loading/loading.component';

@Component({
  selector: 'app-game-details',
  imports: [
    NgStyle,
    NgClass,
    DatePipe,
    UserActionsComponent,
    LoadingComponent
],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.scss'
})
export class GameDetailsComponent implements OnInit {

  game: any;
  isLoading = false;
  activeTab: 'details' | 'system' | 'dlc'| 'friends' = 'details';

  constructor(
    private router: ActivatedRoute,
    private storeService: StoreControllerService,
    private cartService: CartControllerService
  ) {
  }

  ngOnInit(): void {
    this.getInfoGame();
  }

  private getInfoGame() {
    this.isLoading = true;
    const gameId: any = this.router.snapshot.paramMap.get('id')
    if (gameId) {
      this.storeService.getGameById({gameId}).subscribe({
          next: (data) => {
            this.game = data;
            this.isLoading = false;
          },
        error: err => {
            this.isLoading = true;
            console.log(err);
        }
        },
      )
    }

  }

  // buyGame(gameId: any) {
  //   this.storeService.buyGame({gameId})
  //     .subscribe({
  //       next: () => {
  //         this.game.inLibrary = true;
  //       },
  //       error: (err) => {
  //         console.error('Error with buying game:', err);
  //       }
  //     });

  // }

  addToCart(gameId: any) {
    this.cartService.addGameToCart({body: {
        gameId: gameId,
      }}).subscribe({
      next: (game) => {
        console.log('Added to cart: ', game);
        this.game.inCart = true;
      }, error: err => {
        console.log(err);
      }
    })
  }

  getGameImageCover(game: GameResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  addGameToWishList(gameId: any) {
    this.storeService.addGameToWishlist({gameId})
      .subscribe({
        next: () => {
          console.log("added to wishlist");
          this.game.inWishList = true;
        },
        error: (err) => {
          console.error('Error with adding game to wishlist:', err);
        }
      })
  }

  removeGameFromWishList(gameId: any) {
    console.log(gameId);
    this.storeService.removeGameFromWishlist({gameId})
      .subscribe({
        next: () => {
          this.game.inWishList = false;
        },
        error: (err) => {
          console.error('Error with removing game from wishlist:', err);
        }
      })
  }
}
