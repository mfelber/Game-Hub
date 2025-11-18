import {Component, OnInit} from '@angular/core';
import {CommunityControllerService} from '../../../../services/services/community-controller.service';
import {PageResponseUserCommunityResponse} from '../../../../services/models/page-response-user-community-response';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-friend-requests',
  imports: [
    NgIf,
    NgForOf
  ],
  templateUrl: './friend-requests.component.html',
  styleUrl: './friend-requests.component.scss'
})
export class FriendRequestsComponent implements OnInit{

    ngOnInit(): void {
        this.getAllMyFriendRequests();
    }

  friendRequestsResponse: PageResponseUserCommunityResponse = {};

    isLoaded = false;
    zeroFriendRequests = false;


  constructor(private communityService: CommunityControllerService) {
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
}
