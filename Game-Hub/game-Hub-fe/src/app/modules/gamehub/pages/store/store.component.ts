import {Component, OnInit} from '@angular/core';
import {NgForOf, NgOptimizedImage} from '@angular/common';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {Router} from '@angular/router';
import {PageResponseGameResponse} from '../../../../services/models/page-response-game-response';

@Component({
  selector: 'app-store',
  imports: [
    NgForOf,
    NgOptimizedImage
  ],
  templateUrl: './store.component.html',
  styleUrl: './store.component.scss'
})
export class StoreComponent implements OnInit{
  gameResponse: PageResponseGameResponse = {};
  public page = 0;
  public size = 15;

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
        this.gameResponse = games;
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
}
