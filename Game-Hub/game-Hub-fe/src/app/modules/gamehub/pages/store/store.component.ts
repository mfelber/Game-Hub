import {Component, OnInit} from '@angular/core';
import {NgForOf, NgOptimizedImage} from '@angular/common';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {Router, RouterOutlet} from '@angular/router';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';
import {GameResponse} from '../../../../services/models/game-response';

@Component({
  selector: 'app-store',
  imports: [
    NgForOf
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
