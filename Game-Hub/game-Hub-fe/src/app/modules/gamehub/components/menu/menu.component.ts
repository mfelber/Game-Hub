import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {initFlowbite} from 'flowbite';

@Component({
  selector: 'app-menu',
  imports: [
    MatIcon,
    RouterLink,
    RouterLinkActive
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

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const url = this.router.url;
        this.updateActiveTabs(url);
      }
    });
  }

  updateActiveTabs(url: string) {
    this.isStoreActive = url === '/gamehub' || url.startsWith('/gamehub/game/');
    this.isLibraryActive = url === '/gamehub/library';
  }

  goToCart() {
    console.log("Cart clicked!");
  }

  showNotifications() {
    console.log("Notifications clicked!");
  }
}
