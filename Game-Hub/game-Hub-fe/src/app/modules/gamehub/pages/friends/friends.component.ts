import { Component } from '@angular/core';
import {UserAlertsComponent} from '../../components/user-alerts/user-alerts.component';

@Component({
  selector: 'app-friends',
  imports: [
    UserAlertsComponent
  ],
  templateUrl: './friends.component.html',
  styleUrl: './friends.component.scss'
})
export class FriendsComponent {

}
