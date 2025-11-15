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
import {UserPrivateProfileComponent} from './pages/user-private-profile/user-private-profile.component';
import {UserPublicProfileComponent} from './pages/user-public-profile/user-public-profile.component';
import {authGuard} from '../../services/guard/auth.guard';

const routes: Routes = [
  {
    path:'',
    component: MainComponent,
    children: [
      {
        path:'',
        component: StoreComponent,
        canActivate: [authGuard]
      },
      {
        path: 'library',
        component: LibraryComponent,
        canActivate: [authGuard]
      },
      {
        path: 'wishlist',
        component: WishlistComponent,
        canActivate: [authGuard]
      },
      {
        path: 'community',
        component: CommunityComponent,
        canActivate: [authGuard]
      },
      {
        path: 'discounts',
        component: DiscountsComponent,
        canActivate: [authGuard]
      },
      {
        path: 'friend-requests',
        component: FriendRequestsComponent,
        canActivate: [authGuard]
      },
      {
        path: 'friends',
        component: FriendsComponent,
        canActivate: [authGuard]
      },
      { path: 'game/:id',
        component: GameDetailsComponent,
        canActivate: [authGuard]
      },
      { path: 'library/game/:id',
        component: GameDetailsLibraryComponent,
        canActivate: [authGuard]
      },
      {
        path: 'user/me',
        component: UserPrivateProfileComponent,
        canActivate: [authGuard]
      },
      {
        path: 'user/:id',
        component: UserPublicProfileComponent,
        canActivate: [authGuard]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GamehubRoutingModule { }
