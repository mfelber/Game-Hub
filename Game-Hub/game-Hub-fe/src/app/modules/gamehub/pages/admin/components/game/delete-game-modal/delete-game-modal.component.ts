import {Component, EventEmitter, Input, Output} from '@angular/core';
import {GameResponse} from '../../../../../../../services/models/game-response';
import {AdminControllerService} from '../../../../../../../services/services/admin-controller.service';

@Component({
  selector: 'app-delete-game-modal',
  imports: [],
  templateUrl: './delete-game-modal.component.html',
  styleUrl: './delete-game-modal.component.scss',
})
export class DeleteGameModalComponent {

  @Input() game!: GameResponse;
  @Output() close = new EventEmitter<void>();
  @Output() deleted = new EventEmitter<void>();

  constructor(
    private adminControllerService: AdminControllerService
  ) {
  }

  deleteGame(): void {
    this.adminControllerService.deleteGame({gameId: this.game.gameId!}).subscribe({
      next: () => {
        this.deleted.emit();
        this.close.emit();
      },
      error: err => {
        console.log(err);
      }
    })
  }


}
