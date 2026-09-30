import {Component, OnInit} from '@angular/core';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {HlmTabs, HlmTabsContent, HlmTabsList, HlmTabsTrigger} from '@spartan/tabs';

@Component({
  selector: 'app-settings',
  imports: [
    UserActionsComponent,
    HlmTabs,
    HlmTabsList,
    HlmTabsTrigger,
    HlmTabsContent
  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss',
})
export class SettingsComponent implements OnInit {

  constructor(
    private userService: UserProfileControllerService
  ) {
  }

  ngOnInit() {
    this.userService.getUserSettings().subscribe(
      user => {
        console.log(user);
      }
    )
  }

}
