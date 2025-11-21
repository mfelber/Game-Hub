import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf, NgStyle} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {GameResponse} from '../../../../services/models/game-response';
import {LibraryControllerService} from '../../../../services/services/library-controller.service';
import {UserPublicProfileComponent} from '../user-public-profile/user-public-profile.component';

@Component({
  selector: 'app-game-details-library',
  imports: [
    NgForOf,
    NgIf,
    NgStyle
  ],
  templateUrl: './game-details-library.component.html',
  styleUrl: './game-details-library.component.css'
})
export class GameDetailsLibraryComponent implements OnInit {

  game:any
  inFavorites = false
  isRecommended = false

  constructor(
    private router: ActivatedRoute,
    private gameService: GameControllerService,
    private libraryService: LibraryControllerService,
  ) {
  }

  ngOnInit(): void {
    this.getInfoGame();
    this.checkGameIsFavorite();
    this.isGameRecommended();
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
      }
    })
  }

  removeGameFromFavorite(gameId: any) {
    this.libraryService.removeGameFromFavorites({gameId}).subscribe({
      next: () => {
        this.checkGameIsFavorite()
      }
    })
  }

  private checkGameIsFavorite() {
    const gameId: any = this.router.snapshot.paramMap.get('id')
    this.libraryService.checkGameFavorite({gameId}).subscribe({
      next: (isFavorite) => {
        if (isFavorite) {
          this.inFavorites = true
        } else {
          this.inFavorites = false
        }
      }
    })
  }

  recommendGame(gameId: any) {
    if (!this.isRecommended) {
      this.libraryService.recommendGame({gameId}).subscribe({
        next: () => {
          this.isRecommended = true
          this.isGameRecommended();
        },
        error: (err) => console.error('Failed to recommend game', err),
      })
    } else {
      this.libraryService.removeRecommendGame({gameId}).subscribe({
        next: () => {
          this.isRecommended = false
          this.isGameRecommended();
        },
        error: (err) => console.error('Failed to remove game from recommended', err),
      })
    }

  }

  private isGameRecommended() {
    const gameId: any = this.router.snapshot.paramMap.get('id')
    this.libraryService.checkGameRecommended({gameId}).subscribe({
      next: (recommended)=> {
        this.isRecommended = recommended
    }
    })
  }
}
