import {Component, OnInit} from '@angular/core';
import {CartControllerService} from '../../../../services/services/cart-controller.service';
import {CartResponse} from '../../../../services/models/cart-response';
import {EmptyStateComponent} from '../../components/empty-state/empty-state.component';
import {DecimalPipe, NgForOf, NgIf} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {GameResponse} from '../../../../services/models/game-response';
import {StoreControllerService} from '../../../../services/services/store-controller.service';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';

@Component({
  selector: 'app-cart',
  imports: [
    EmptyStateComponent,
    NgIf,
    RouterLink,
    NgForOf,
    DecimalPipe,
    UserActionsComponent
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent implements OnInit {

  cartResponse: CartResponse = {};

  isLoading = false;

  constructor(
    private cartService: CartControllerService,
    private storeService: StoreControllerService,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.getCart();
  }

  getCart() {
    this.isLoading = true;
    this.cartService.getCart().subscribe({
      next: data => {
        console.log(data);
        this.cartResponse = data;
        this.isLoading = false;
      }
    })
  }


  getGameImageCover(game: GameResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  removeFromCart(gameId: any) {
    this.cartService.removeFromCart({gameId}).subscribe({
      next: data => {
        this.getCart();
      }
    })
  }

  checkout() {

  }

  goToGame(gameId:any) {
    this.storeService.getGameById({gameId}).subscribe({
      next: (game) => {
        this.router.navigate(['/gamehub/store/game/', gameId]);
      },
      error: (err) => {
        console.error('Error with loading game:', err);
      }
    });
  }

  get total(): number {
    return (this.cartResponse.games ?? []).reduce((total, game) => {
      const price = game.hasDiscount
        ? game.discountPrice
        : game.price;

      return total + (price ?? 0);
    }, 0);
  }
}
