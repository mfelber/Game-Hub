import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NgIf} from "@angular/common";
import {UserLibraryResponse} from '../../../../services/models/user-library-response';
import {LibraryControllerService} from '../../../../services/services/library-controller.service';

@Component({
  selector: 'app-playing-warning-modal',
    imports: [
    ],
  templateUrl: './playing-warning-modal.component.html',
  styleUrl: './playing-warning-modal.component.scss',
})
export class PlayingWarningModalComponent implements OnInit {

  @Input() game!: UserLibraryResponse
  @Output() close = new EventEmitter<void>()
  @Output() stoppedPlayingGame = new EventEmitter<void>();

  constructor(
    private libraryService: LibraryControllerService
  ) {
  }

  ngOnInit() {
    console.log(this.game.title);
  }

  stopPlaying(gameId: any) {
    this.libraryService.stopPlayingGame({gameId}).subscribe({
      next: () => {
        this.stoppedPlayingGame.emit();
        this.close.emit();
      }
    })
  }
}
