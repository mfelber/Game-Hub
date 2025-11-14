import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GamehubRoutingModule } from './gamehub-routing.module';
import {CardPreviewComponent} from './components/card-preview/card-preview.component';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    GamehubRoutingModule,
    CardPreviewComponent,
  ]
})
export class GamehubModule { }
