import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {GameControllerService} from '../../../../services/services/game-controller.service';
import {NgForOf, NgIf} from '@angular/common';

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

  constructor(
    private router: ActivatedRoute,
    private gameService: GameControllerService
  ) {
  }

  ngOnInit(): void {
    this.getInfoGame();
  }

  private getInfoGame() {
    const gameId: any = this.router.snapshot.paramMap.get('id')
    console.log(gameId);
    if (gameId) {
      this.gameService.getGameById({gameId}).subscribe({
          next: (data) => this.game = data,
          error: (err) => console.error('Error with loading details of this game', err)
        },
      )
    }

  }


}
