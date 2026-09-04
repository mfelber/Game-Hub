import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {StoreControllerService} from '../../../../../../../services/services/store-controller.service';
import {GameResponse} from '../../../../../../../services/models/game-response';
import {DatePipe, NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';

@Component({
  selector: 'app-admin-game-details',
  imports: [
    NgIf,
    NgForOf,
    NgStyle,
    DatePipe,
    NgClass
  ],
  templateUrl: './admin-game-details.component.html',
  styleUrl: './admin-game-details.component.scss',
})
export class AdminGameDetailsComponent implements OnInit {

  game: any;

  constructor(
    private router: ActivatedRoute,
    private storeService: StoreControllerService
  ) {
  }

  ngOnInit() {
    this.getInfoGame();
  }

  private getInfoGame() {
    const gameId: any = this.router.snapshot.paramMap.get('id')
    if (gameId) {
      this.storeService.getGameById({gameId}).subscribe({
          next: (data) => {
            this.game = data;
            console.log(this.game);
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

}
