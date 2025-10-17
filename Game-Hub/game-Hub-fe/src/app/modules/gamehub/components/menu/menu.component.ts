import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {initFlowbite} from 'flowbite';

@Component({
  selector: 'app-menu',
  imports: [
    MatIcon,
    RouterLink
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
