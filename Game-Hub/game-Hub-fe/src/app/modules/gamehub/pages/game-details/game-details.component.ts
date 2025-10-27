import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {NgForOf, NgIf} from '@angular/common';
import {GameResponse} from '../../../../services/models/game-response';
import {checkGameInWishlist} from '../../../../services/fn/game-controller/check-game-in-wishlist';

@Component({
  selector: 'app-game-details',
  imports: [
    NgIf,
    NgForOf
  ],
  templateUrl: './game-details.component.html',
  styleUrl: './game-details.component.scss'
})
export class GameDetailsComponent implements OnInit {

  game: any;
  gameIsOwned = false;
  gameInWishList = false;

  constructor(
    private router: ActivatedRoute,
    private gameService: GameControllerService
  ) {
  }

  ngOnInit(): void {
    this.getInfoGame();
    this.checkIfGameIsOwned();
    this.checkIfGameIsInWishlist();
  }

  private getInfoGame() {
    const gameId: any = this.router.snapshot.paramMap.get('id')
    console.log(gameId);
    if (gameId) {
      this.gameService.getGameById({gameId}).subscribe({
          next: (data) => {
            this.game = data;
            console.log(data)
          },

          error: (err) => console.error('Error with loading details of this game', err)
        },
      )
    }

  }

  private checkIfGameIsOwned() {
    const gameId: any = this.router.snapshot.paramMap.get('id')
    this.gameService.checkGameOwned({gameId})
      .subscribe({
        next: (owned) => {
          if (owned) {
            console.log('you owned this game')
            this.gameIsOwned = true;
          } else {
            console.log('you do not owned this game')
            this.gameIsOwned = false;
          }

        }
      })
  }

  private checkIfGameIsInWishlist() {
    const gameId: any = this.router.snapshot.paramMap.get('id')
    this.gameService.checkGameInWishlist({gameId})
      .subscribe({
        next: (inWishList) => {
          if (inWishList){
            this.gameInWishList = true
            console.log('you have this game in wishlist')
          } else {
            this.gameInWishList = false
            console.log('you dont have this game in wishlist')
          }
        }
      })
  }


  buyGame(gameId: any) {
    this.gameService.buyGame({gameId})
      .subscribe({
        next: () => {
          this.checkIfGameIsOwned();
        },
        error: (err) => {
          console.log('Error with buying game:', err);
        }
      });

  }

  getGameImageCover(game: GameResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  addGameToWishList(gameId: any) {
    this.gameService.addGameToWishlist({gameId})
      .subscribe({
        next: () => {
          this.checkIfGameIsInWishlist();
        },
        error: (err) => {
          console.log('Error with buying game:', err);
        }
      })
  }

  removeGameFromWishList(gameId: any) {
    this.gameService.removeGameFromWishlist({gameId})
      .subscribe({
        next: () => {
          this.gameInWishList = false;
        },
        error: (err) => {
          console.log('Error with buying game:', err);
        }
      })
  }
}
