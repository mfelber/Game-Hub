import {Component, OnInit} from '@angular/core';
import {CommunityControllerService} from '../../../../services/services/community-controller.service';
import {PageResponseUserCommunityResponse} from '../../../../services/models/page-response-user-community-response';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {FriendRequestResponse} from '../../../../services/models/friend-request-response';
import {PageResponseFriendRequestResponse} from '../../../../services/models/page-response-friend-request-response';
import {RefreshService} from '../../../../services/fn/refresh-service/refresh-service';

@Component({
  selector: 'app-friend-requests',
  imports: [
    NgIf,
    NgForOf,
    NgClass,
    NgStyle
  ],
  templateUrl: './friend-requests.component.html',
  styleUrl: './friend-requests.component.scss'
})
export class FriendRequestsComponent implements OnInit {

  ngOnInit(): void {
    this.getAllMyFriendRequests();
  }

  friendRequestsResponse: PageResponseFriendRequestResponse = {};

  isLoaded = false;
  zeroFriendRequests = false;


  constructor(private communityService: CommunityControllerService, private refreshService: RefreshService) {
  }

  private getAllMyFriendRequests() {
    this.communityService.getFriendRequests().subscribe({
      next: (friendRequests) => {
        this.friendRequestsResponse = friendRequests;
        console.log(friendRequests);
        this.isLoaded = true;
        if (friendRequests.totalElements === 0) {
          this.zeroFriendRequests = true;
        } else {
          this.zeroFriendRequests = false;
        }
      }
    })
  }

  getProfilePicture(user: FriendRequestResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return;
  }

  acceptFriendRequest(userId: number) {
    this.communityService.acceptFriendRequest({userId}).subscribe({
      next: () => {
        this.refreshService.triggerRefresh();
        this.getAllMyFriendRequests();
      }
    });
  }

  rejectFriendRequest(userId: number) {
    this.communityService.rejectFriendRequest({userId}).subscribe({
      next: () => {
        this.refreshService.triggerRefresh();
        this.getAllMyFriendRequests()
      }
    })
  }
}
