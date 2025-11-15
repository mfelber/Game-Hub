import {Component, OnInit} from '@angular/core';
import {WishlistControllerService} from '../../../../services/services/wishlist-controller.service';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {NgForOf, NgIf} from '@angular/common';
import {GameResponse} from '../../../../services/models/game-response';
import {Router} from '@angular/router';

@Component({
  selector: 'app-wishlist',
    imports: [
        NgForOf,
        NgIf
    ],
  templateUrl: './wishlist.component.html',
  styleUrl: './wishlist.component.scss'
})
export class WishlistComponent implements OnInit{

  gamePageResponse: PageResponseGameResponse = {};

  emptyWishlist = false;
  isLoaded = false;

  constructor(
    private wishListService: WishlistControllerService,
    private gameService: GameControllerService,
    private router: Router
  ) {
  }

  public page = 0;
  public size = 15;

  ngOnInit() {
    this.getAllGamesInWishlist()
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
}
