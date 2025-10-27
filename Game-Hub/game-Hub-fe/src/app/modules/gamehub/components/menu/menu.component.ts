import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {initFlowbite} from 'flowbite';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-menu',
  imports: [
    RouterLink,
    RouterLinkActive,
    NgIf
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {
  ngOnInit(): void {
    initFlowbite();
  }

  isStoreActive = false;
  isLibraryActive = false;
  _isGameDetailsActive = false;

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const url = this.router.url;
        this.updateActiveTabs(url);
        this.isGameDetailsActive(url)
      }
    });
  }

  updateActiveTabs(url: string) {
    this.isStoreActive = url === '/gamehub' || url.startsWith('/gamehub/game/');
    this.isLibraryActive = url === '/gamehub/library' || url.startsWith("/gamehub/library/game/");
  }

  isGameDetailsActive(url: string){
    this._isGameDetailsActive = url.startsWith("/gamehub/library/game/") || url.startsWith("/gamehub/game/");
  }

  goToCart() {
    console.log("Cart clicked!");
  }

  showNotifications() {
    console.log("Notifications clicked!");
  }
}
