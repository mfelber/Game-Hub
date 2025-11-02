import {Component, OnInit} from '@angular/core';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {initFlowbite} from 'flowbite';
import {Router} from '@angular/router';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {NgClass, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-user-profile',
  imports: [
    NgIf,
    NgForOf,
    NgClass
  ],
  templateUrl: './user-private-profile.component.html',
  styleUrl: './user-private-profile.component.css'
})
export class UserPrivateProfileComponent implements OnInit{

  ngOnInit(): void {
    this.loadUserPrivateProfile();
  }
  constructor(
    private router: Router,
    private userService: UserProfileControllerService
  ){}

  userResponse: UserPrivateResponse = {};


  private loadUserPrivateProfile() {
    this.userService.getUserPrivate().subscribe({
      next:  (user) => {
        this.userResponse = user;
        console.log(user)
        this.getProfilePicture(user)
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
