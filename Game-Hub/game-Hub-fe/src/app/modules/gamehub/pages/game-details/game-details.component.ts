import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {NgForOf, NgIf, NgStyle} from '@angular/common';
import {GameResponse} from '../../../../services/models/game-response';

@Component({
  selector: 'app-game-details',
  imports: [
    NgIf,
    NgForOf,
    NgStyle
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
    if (gameId) {
      this.gameService.getGameById({gameId}).subscribe({
          next: (data) => {
            this.game = data;
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
            this.gameIsOwned = true;
          } else {
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
          } else {
            this.gameInWishList = false
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
          console.error('Error with buying game:', err);
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
          console.error('Error with buying game:', err);
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
          console.error('Error with buying game:', err);
        }
      })
  }
}
