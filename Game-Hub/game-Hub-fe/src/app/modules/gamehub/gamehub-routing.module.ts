import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainComponent} from './pages/main/main.component';
import {StoreComponent} from './pages/store/store.component';
import {LibraryComponent} from './pages/library/library.component';
import {WishlistComponent} from './pages/wishlist/wishlist.component';
import {CommunityComponent} from './pages/community/community.component';
import {DiscountsComponent} from './pages/discounts/discounts.component';
import {FriendRequestsComponent} from './pages/friend-requests/friend-requests.component';
import {FriendsComponent} from './pages/friends/friends.component';
import {GameDetailsComponent} from './pages/game-details/game-details.component';
import {GameDetailsLibraryComponent} from './pages/game-details-library/game-details-library.component';

const routes: Routes = [
  {
    path:'',
    component: MainComponent,
    children: [
      {
        path:'',
        component: StoreComponent
      },
      {
        path: 'library',
        component: LibraryComponent
      },
      {
        path: 'wishlist',
        component: WishlistComponent
      },
      {
        path: 'community',
        component: CommunityComponent
      },
      {
        path: 'discounts',
        component: DiscountsComponent
      },
      {
        path: 'friend-requests',
        component: FriendRequestsComponent
      },
      {
        path: 'friends',
        component: FriendsComponent
      },
      { path: 'game/:id',
        component: GameDetailsComponent
      },
      { path: 'library/game/:id',
        component: GameDetailsLibraryComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GamehubRoutingModule { }
