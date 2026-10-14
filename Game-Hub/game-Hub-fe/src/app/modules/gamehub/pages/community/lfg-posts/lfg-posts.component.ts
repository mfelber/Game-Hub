import { Component } from '@angular/core';
import {NgClass} from '@angular/common';
import {SearchBar} from '../../../components/search-bar/search-bar';
import {Router} from '@angular/router';
import {HlmTabs, HlmTabsList, HlmTabsTrigger} from '@spartan/tabs';

@Component({
  selector: 'app-lfg-posts',
  imports: [
    NgClass,
    SearchBar,
    HlmTabs,
    HlmTabsList,
    HlmTabsTrigger
  ],
  templateUrl: './lfg-posts.component.html',
  styleUrl: './lfg-posts.component.scss',
})
export class LfgPostsComponent {

  constructor(
    private router: Router,
  ) {
  }


  openLfgFilter = false;

  resetFilters() {

  }

  navigateToNewPost() {
    this.router.navigate(['/gamehub/find-players/new-lfg-post']);
  }
}
