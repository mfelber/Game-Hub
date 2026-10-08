import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GamehubRoutingModule } from './gamehub-routing.module';
import {CardPreviewComponent} from './components/card-preview/card-preview.component';
import {ReactiveFormsModule} from '@angular/forms';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    GamehubRoutingModule,
    CardPreviewComponent,
    ReactiveFormsModule,
  ]
})
export class GamehubModule { }
