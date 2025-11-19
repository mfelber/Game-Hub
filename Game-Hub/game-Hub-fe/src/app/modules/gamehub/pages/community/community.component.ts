import {Component, OnInit} from '@angular/core';
import {initFlowbite} from 'flowbite';
import {CommunityControllerService} from '../../../../services/services/community-controller.service';
import {UserCommunityResponse} from '../../../../services/models/user-community-response';
import {PageResponseUserCommunityResponse} from '../../../../services/models/page-response-user-community-response';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {UserPublicResponse} from '../../../../services/models/user-public-response';
import {Router} from '@angular/router';
import {exists} from 'node:fs';
import {firstValueFrom} from 'rxjs';

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

  userCommunityResponse: PageResponseUserCommunityResponse = {};
  friendRequestMapFromSender: { [key: number]: boolean } = {};
  friendRequestMapForReceiver: { [key: number]: boolean } = {};
  friendRequestFromThisUser: boolean | null = null;
  friendsMap: { [key: number]: boolean } = {};
  noUsersFound = false;
  friendRequestSent = false;
  isLoaded = false;


  private loadAllUsers(query: string = "") {

    this.communityService.findAllUsers({query: query}).subscribe({
      next: (users) => {
        if (users.content?.length! === 0) {
          this.noUsersFound = true;
          return
        }
        console.log(this.friendRequestMapFromSender)
        this.noUsersFound = false;
        this.userCommunityResponse = users;

        this.userCommunityResponse.content?.forEach(user => {
          this.friendRequestExistsForSender(user.userId)
        })

        this.userCommunityResponse.content?.forEach(user => {
          this.friendRequestExistsForReceiver(user.userId!)
        })

        this.userCommunityResponse.content?.forEach(user => {
          this.areFriends(user.userId!)
        })

        this.loadUsers = true;
      }
    })

  }

  private async areFriends(userId: number) {
    const exists = await firstValueFrom(this.communityService.friendExistsForUser({userId}))
    this.friendsMap[userId] = exists;
    this.isLoaded = true;
    return exists;
  }

  private async friendRequestExistsForSender(userId: any) {
    const exists = await firstValueFrom(this.communityService.friendRequestExistsFromSender({userId}));
    this.friendRequestMapFromSender[userId] = exists;
    console.log(this.friendRequestMapFromSender);
    return exists;
  }

  private async friendRequestExistsForReceiver(userId: number) {
    const exists = await firstValueFrom(this.communityService.friendRequestExistsForReceiver({userId}))
    this.friendRequestMapForReceiver[userId] = exists;
    return exists;
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
        this.friendRequestMapFromSender[userId] = true;
      }
    })
    console.log(userId)
  }

  cancelFriendRequest(userId: number) {
    this.communityService.cancelFriendRequest({userId}).subscribe({
      next: () => {
        this.friendRequestMapFromSender[userId] = false;
      }
    })


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


  acceptFriendRequest(userId: number) {
    this.communityService.acceptFriendRequest({userId}).subscribe({
      next: () => {
        this.friendsMap[userId!] = true;
        this.friendRequestMapForReceiver[userId!] = false;
      }
    });
  }


  rejectFriendRequest(userId: number) {
    this.communityService.rejectFriendRequest({userId}).subscribe({
      next: () => {
        this.friendsMap[userId!] = false;
        this.friendRequestMapForReceiver[userId!] = false;
      }
    })

  }
}
