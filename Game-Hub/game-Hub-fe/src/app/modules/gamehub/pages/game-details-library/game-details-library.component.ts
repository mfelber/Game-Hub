import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {GameResponse} from '../../../../services/models/game-response';
import {LibraryControllerService} from '../../../../services/services/library-controller.service';
import {UserPublicProfileComponent} from '../user-public-profile/user-public-profile.component';

@Component({
  selector: 'app-game-details-library',
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './game-details-library.component.html',
  styleUrl: './game-details-library.component.css'
})
export class GameDetailsLibraryComponent implements OnInit {

  game:any
  inFavorites = false

  constructor(
    private router: ActivatedRoute,
    private gameService: GameControllerService,
    private libraryService: LibraryControllerService,
  ) {
  }

  ngOnInit(): void {
    this.getInfoGame();
    this.checkGameIsFavorite();
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

  getGameImageCover(game: GameResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }


  addGameToFavorite(gameId: any) {
    this.libraryService.addGameToFavorites({gameId}).subscribe({
      next: () => {
        this.checkGameIsFavorite()
        console.log('game with id ' + gameId + 'was added to user as favorite')
      }
    })
  }

  removeGameFromFavorite(gameId: any) {
    this.libraryService.removeGameFromFavorites({gameId}).subscribe({
      next: () => {
        this.checkGameIsFavorite()
        console.log('game with id ' + gameId + 'was added to user as favorite')
      }
    })
  }

  private checkGameIsFavorite() {
    const gameId: any = this.router.snapshot.paramMap.get('id')
    this.libraryService.checkGameFavorite({gameId}).subscribe({
      next: (isFavorite) => {
        if (isFavorite) {
          this.inFavorites = true
          console.log('in favorites')
        } else {
          this.inFavorites = false
          console.log('not in favorites')
        }
      }
    })
  }
}
