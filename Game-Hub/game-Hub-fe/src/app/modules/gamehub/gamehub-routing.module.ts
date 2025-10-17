import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainComponent} from './pages/main/main.component';
import {StoreComponent} from './pages/store/store.component';
import {LibraryComponent} from './pages/library/library.component';

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
      }
    ]

  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class GamehubRoutingModule { }
