import { Component } from '@angular/core';
import {HlmSpinner} from '@spartan/spinner';


@Component({
  selector: 'app-loading',
  imports: [
    HlmSpinner
  ],
  templateUrl: './loading.component.html',
  styleUrl: './loading.component.scss',
})
export class LoadingComponent {

}
