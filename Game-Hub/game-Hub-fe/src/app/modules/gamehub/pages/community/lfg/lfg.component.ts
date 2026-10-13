import { Component } from '@angular/core';
import {SearchBar} from "../../../components/search-bar/search-bar";
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-lfg',
  imports: [
    SearchBar,
    NgClass
  ],
  templateUrl: './lfg.component.html',
  styleUrl: './lfg.component.scss',
})
export class LfgComponent {

  openLfgFilter = false;

  resetFilters() {

  }
}
