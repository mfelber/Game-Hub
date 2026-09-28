import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

import {UserLibraryResponse} from '../../../../services/models/user-library-response';
import {LibraryControllerService} from '../../../../services/services/library-controller.service';
import {HlmDialogDescription, HlmDialogFooter, HlmDialogHeader, HlmDialogTitle} from '@spartan/dialog';

@Component({
  selector: 'app-playing-warning-modal',
  imports: [
    HlmDialogHeader,
    HlmDialogTitle,
    HlmDialogDescription,
    HlmDialogFooter
  ],
  templateUrl: './playing-warning-modal.component.html',
  styleUrl: './playing-warning-modal.component.scss',
})
export class PlayingWarningModalComponent implements OnInit {

  @Input() game!: UserLibraryResponse
  @Input() title!: string;
  @Input() description!: string;
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
