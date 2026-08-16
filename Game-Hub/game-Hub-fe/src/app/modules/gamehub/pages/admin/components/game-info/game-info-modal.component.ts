import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgForOf, NgIf, NgStyle} from '@angular/common';
import {GameResponse} from '../../../../../../services/models/game-response';

@Component({
  selector: 'app-game-info',
  imports: [
    NgIf,
    NgForOf,
    NgStyle
  ],
  templateUrl: './game-info-modal.component.html',
  styleUrl: './game-info-modal.component.scss',
})
export class GameInfoModalComponent {
  @Input() game!: GameResponse;
  @Output() close = new EventEmitter<void>();


  getGameImageCover(game: GameResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }


}
