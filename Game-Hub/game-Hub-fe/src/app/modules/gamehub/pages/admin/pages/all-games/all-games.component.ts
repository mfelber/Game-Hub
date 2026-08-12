import {Component, OnInit} from '@angular/core';
import {GamePreviewResponse} from '../../../../../../services/models/game-preview-response';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {Router} from '@angular/router';
import {PageResponseGamePreviewResponse} from '../../../../../../services/models/page-response-game-preview-response';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {SearchBar} from '../../../../components/search-bar/search-bar';
import {ReactiveFormsModule} from '@angular/forms';
import {GameInfoModalComponent} from '../../components/game-info/game-info-modal.component';
import {GameResponse} from '../../../../../../services/models/game-response';
import {DeleteGameModalComponent} from '../../components/delete-game-modal/delete-game-modal.component';
import {AddGameModalComponent} from '../../components/add-game-modal/add-game-modal.component';

@Component({
  selector: 'app-all-games',
  imports: [
    NgIf,
    SearchBar,
    NgForOf,
    ReactiveFormsModule,
    GameInfoModalComponent,
    DeleteGameModalComponent,
    AddGameModalComponent,
    NgClass
  ],
  templateUrl: './all-games.component.html',
  styleUrl: './all-games.component.scss',
})
export class AllGamesComponent implements OnInit {

  gamesResponse: PageResponseGamePreviewResponse = {}
  selectedGame: GameResponse = {}
  isLoaded = false;
  isGameInfoModalOpen = false;
  isGameDeleteModalOpen = false;
  isAddGameModalOpen = false;

  constructor(
    private adminControllerService: AdminControllerService,
    private router: Router,
  ) {
  }

  ngOnInit() {
    this.loadGamesTableData();
  }

  getGameInfo(gameId: any) {
    this.selectedGame = {};
    this.isGameInfoModalOpen = true;
    this.adminControllerService.getGameInfo({gameId}).subscribe({
      next: (data: any) => {
        this.selectedGame = data;
        console.log(data);
      },
      error: (err) => {
        console.log(err);
        this.isGameInfoModalOpen = false;
      }
    })
  }

  deleteGame(game: any) {
    this.isGameDeleteModalOpen = true;
    this.selectedGame = game;
    // this.adminControllerService.deleteGame(gameId).subscribe({
    //   next: (data) => {
    //     console.log(data);
    //   }
    // })
  }

  addGame() {
    this.isAddGameModalOpen = true;
  }

  successMessage: string | null = null;
  toastVisible = false;

  showSuccess(message: string) {
    this.loadGamesTableData();
    this.successMessage = message;

    setTimeout(() => this.toastVisible = true, 10);

    setTimeout(() => this.hideToast(), 3000);

  }

  hideToast() {
    this.toastVisible = false;

    setTimeout(() => this.successMessage = null, 500);
  }

  loadGamesTableData() {
    this.adminControllerService.getAllGames().subscribe({
      next: (data) => {
        this.gamesResponse = data;
        console.log(data);
        this.isLoaded = true;
      },
      error: (err) => {
        console.log(err);
        this.isLoaded = false;
      }
    })
  }

  getGameImageCover(game: GamePreviewResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }


  searchGames($event: string) {
    console.log($event);
  }

  closeModal() {
    this.isGameInfoModalOpen = false;
    this.isGameDeleteModalOpen = false;
    this.isAddGameModalOpen = false;
    this.selectedGame = {};
  }
}
