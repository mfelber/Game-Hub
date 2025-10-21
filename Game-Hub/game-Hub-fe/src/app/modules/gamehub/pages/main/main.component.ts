import {AfterViewInit, Component} from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {MenuComponent} from '../../components/menu/menu.component';

@Component({
  selector: 'app-main',
  imports: [
    RouterOutlet,
    MenuComponent
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent implements AfterViewInit {

  constructor(private router: Router) {}

  ngAfterViewInit() {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const contentDiv = document.querySelector('.scrollable-content');
        if (contentDiv) contentDiv.scrollTop = 0;
      }
    });
  }
}
