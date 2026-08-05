import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainComponent} from './pages/main/main.component';
import {StoreComponent} from './pages/store/store.component';
import {LibraryComponent} from './pages/library/library.component';
import {WishlistComponent} from './pages/wishlist/wishlist.component';
import {FindPlayersComponent} from './pages/find-players/find-players.component';
import {FriendRequestsComponent} from './pages/friend-requests/friend-requests.component';
import {FriendsComponent} from './pages/friends/friends.component';
import {GameDetailsComponent} from './pages/game-details/game-details.component';
import {GameDetailsLibraryComponent} from './pages/game-details-library/game-details-library.component';
import {UserPrivateProfileComponent} from './pages/user-private-profile/user-private-profile.component';
import {UserPublicProfileComponent} from './pages/user-public-profile/user-public-profile.component';
import {authGuard} from '../../services/guard/auth.guard';
import {DashboardComponent} from './pages/admin/pages/dashboard/dashboard.component';
import {MainAdminComponent} from './pages/admin/main/main.component';

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
        path: 'find-players',
        component: FindPlayersComponent,
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
  },
  {
    path: 'admin',
    component: MainAdminComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent,
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GamehubRoutingModule { }
