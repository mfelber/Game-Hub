import { Component } from '@angular/core';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';

@Component({
  selector: 'app-friends',
  imports: [
    UserActionsComponent
  ],
  templateUrl: './friends.component.html',
  styleUrl: './friends.component.scss'
})
export class FriendsComponent {

}
