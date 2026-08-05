import { Component } from '@angular/core';
import {MenuComponent} from '../../../components/menu/menu.component';
import {RouterOutlet} from '@angular/router';
import {AdminMenuComponent} from '../components/menu/menu.component';

@Component({
  selector: 'app-main',
  imports: [
    RouterOutlet,
    AdminMenuComponent
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css',
})
export class MainAdminComponent {

}
