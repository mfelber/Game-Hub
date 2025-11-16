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

  public page = 0;
  public size = 10;

  userHasProfilePicture = true;
  loadUsers = false;

  constructor(
    private communityService: CommunityControllerService,
    private router: Router
    ) {
  }

  userCommunityResponse: PageResponseUserCommunityResponse = {}
  noUsersFound = false;


  private loadAllUsers(query: string = "") {
    this.communityService.findAllUsers({query:query}).subscribe({
      next: (users) => {
        if (users.content?.length! === 0) {
          this.noUsersFound = true;
          return
        }
        this.noUsersFound = false;
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

  sendFriendRequest(userId: number) {
    this.communityService.sendFriendRequest({userId}).subscribe({
      next: () => {
        console.log('friend request send')
      }
    })
    console.log(userId)
  }

  navigateToUser(userId: number | undefined) {
    this.router.navigate(['gamehub/user', userId]);
  }

  reportUser(userId: number | undefined) {

  }

  searchByUsername(value: string) {
    console.log(value);
    this.page = 0;
    this.userCommunityResponse = {}
    this.loadAllUsers(value);
  }
}
