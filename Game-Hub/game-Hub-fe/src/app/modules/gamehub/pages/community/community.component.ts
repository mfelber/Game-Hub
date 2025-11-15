import {Component, OnInit} from '@angular/core';
import {initFlowbite} from 'flowbite';
import {CommunityControllerService} from '../../../../services/services/community-controller.service';
import {UserCommunityResponse} from '../../../../services/models/user-community-response';
import {PageResponseUserCommunityResponse} from '../../../../services/models/page-response-user-community-response';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {UserPublicResponse} from '../../../../services/models/user-public-response';
import {Router} from '@angular/router';

@Component({
  selector: 'app-community',
  imports: [
    NgForOf,
    NgIf,
    NgStyle,
    NgClass
  ],
  templateUrl: './community.component.html',
  styleUrl: './community.component.scss'
})
export class CommunityComponent implements OnInit {


  ngOnInit(): void {
    initFlowbite();
    this.loadAllUsers();
  }

  userHasProfilePicture = true;
  loadUsers = false;

  constructor(
    private communityService: CommunityControllerService,
    private router: Router
    ) {
  }

  userCommunityResponse: PageResponseUserCommunityResponse = {}


  private loadAllUsers() {
    this.communityService.findAllUsers().subscribe({
      next: (users) => {
        this.userCommunityResponse = users;
        this.loadUsers = true;
        console.log(this.userCommunityResponse)
      }
    })
  }

  getProfilePicture(user: UserCommunityResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return this.userHasProfilePicture;
  }

  addFriend(userId: number | undefined) {
    console.log(userId)
  }

  navigateToUser(userId: number | undefined) {
    this.router.navigate(['gamehub/user', userId]);
  }
}
