import {Component, OnInit} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
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

  goToCart() {
    console.log("Cart clicked!");
  }

  showNotifications() {
    console.log("Notifications clicked!");
  }
}
