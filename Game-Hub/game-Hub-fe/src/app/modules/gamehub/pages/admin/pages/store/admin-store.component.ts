import {Component, OnInit} from '@angular/core';
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {SearchBar} from '../../../../components/search-bar/search-bar';
import {UserAlertsComponent} from '../../../../components/user-alerts/user-alerts.component';
import {StoreControllerService} from '../../../../../../services/services/store-controller.service';
import {PageResponseGameResponse} from '../../../../../../services/models/page-response-game-response';
import {GameResponse} from '../../../../../../services/models/game-response';
import {Router} from '@angular/router';

@Component({
  selector: 'app-admin-store',
  imports: [
    NgIf,
    SearchBar,
    UserAlertsComponent,
    NgForOf,
    NgClass
  ],
  templateUrl: './admin-store.component.html',
  styleUrl: './admin-store.component.scss',
})
export class AdminStoreComponent implements OnInit {

  storeResponse: PageResponseGameResponse = {}
  public page = 0;
  public size = 30;
  isLoaded = false

  constructor(
    private storeService: StoreControllerService,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.getAllGames();
  }

  private getAllGames() {
    this.storeService.findAllGames({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (games) => {
        this.storeResponse = games;
        console.log(games);
        this.isLoaded = true;
      }
    })
  }

  getGameImageCover(game: GameResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }


  searchGames(query: string) {
    console.log(query);
  }

  goToGame(gameId:any) {
    this.storeService.getGameById({gameId}).subscribe({
      next: (game) => {
        this.router.navigate(['/gamehub/admin/admin-store/game/', gameId]);
      },
      error: (err) => {
        console.error('Error with loading game:', err);
      }
    });
  }

}
