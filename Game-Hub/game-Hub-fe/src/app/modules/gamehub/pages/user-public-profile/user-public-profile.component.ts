import {Component, OnInit} from '@angular/core';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserPublicResponse} from '../../../../services/models/user-public-response';
import {ActivatedRoute, Router} from '@angular/router';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {NgClass, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-user-public-profile',
  imports: [
    NgIf,
    NgClass,
    NgForOf
  ],
  templateUrl: './user-public-profile.component.html',
  styleUrl: './user-public-profile.component.css'
})
export class UserPublicProfileComponent implements OnInit{

  ngOnInit(): void {
    this.loadUserPublicProfile();
  }

  constructor(
    private userService: UserProfileControllerService,
    private router: ActivatedRoute,
  ) {
  }

  userResponse: UserPublicResponse = {}


  private loadUserPublicProfile() {
    const userId: any = this.router.snapshot.paramMap.get('id')
    this.userService.getUserPublic({userId}).subscribe({
      next:  (user) => {
        this.userResponse = user;
        this.getProfilePicture(userId)
      }
    });
  }

  getProfilePicture(user: UserPrivateResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  getBanner(user: UserPrivateResponse) {
    if (user.bannerImage) {
      return 'data:image/jpeg;base64,' + user.bannerImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }
}
