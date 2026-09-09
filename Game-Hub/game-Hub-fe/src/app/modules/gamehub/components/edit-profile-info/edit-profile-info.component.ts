import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {UserUpdateRequest} from '../../../../services/models/user-update-request';
import {CardColorResponse} from '../../../../services/models/card-color-response';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {initFlowbite} from 'flowbite';
import {SearchBar} from '../search-bar/search-bar';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserLibraryResponse} from '../../../../services/models/user-library-response';
import {GameResponse} from '../../../../services/models/game-response';

@Component({
  selector: 'app-edit-profile-info',
  imports: [
    NgIf,
    FormsModule,
    NgForOf,
    NgStyle,
    NgClass,
    SearchBar
  ],
  templateUrl: './edit-profile-info.component.html',
  styleUrl: './edit-profile-info.component.scss',
})
export class EditProfileInfoComponent implements OnInit {


  ngOnInit(): void {
    initFlowbite();
    this.getProfilePicture(this.user)
  }

  @Input() user!: UserPrivateResponse;
  @Input() userRequest!: UserUpdateRequest;
  @Input() allLocations: {
    name: string;
    iconPath: string;
  }[] = [];
  @Input() cardColors: CardColorResponse[] = [];
  @Output() save = new EventEmitter<{
    profilePicture: File | null,
    profileBanner: File | null,
    selectedBannerId: number | null,
    favoriteGameId: number | null,
  }>();
  @Output() close = new EventEmitter<void>();
  @Output() updated = new EventEmitter<void>();
  @Output() preview = new EventEmitter<{
    selectedColorCode: String,
    previewBanner: String | undefined,
    isPreviewImageInserted: boolean,
    previewImage: String | undefined;
  }>();

  activeTab: 'basic' | 'profile' | 'gaming' = 'basic';
  userLibraryResponse: UserLibraryResponse[] = [];

  selectedColorCode: string = '';
  selectedColorId: number | null = null;

  profilePicture: File | null = null;
  previewImage: string | undefined;
  previewBanner: string | undefined;
  profileBanner: File | null = null;
  selectedBannerId: number | null = null;

  selectedFavoriteGame: UserLibraryResponse | null = null;

  showPredefinedBanners = false;
  isPreviewImageInserted = false;
  isPreviewBannerInserted = false;

  userHasProfilePicture = true

  predefinedBanners = [1, 2, 3, 4];


  searchedQuery = '';

  constructor(
    private userService: UserProfileControllerService
  ) {
  }

  getBanner(user: UserPrivateResponse) {
    if (user.bannerImage) {
      return 'data:image/jpeg;base64,' + user.bannerImage;
    }
    return user.predefinedBannerPath;
  }

  selectColor(id: number, colorCode: string) {
    console.log('you have selected', id, colorCode)
    this.selectedColorCode = colorCode;
    this.selectedColorId = id;
    this.userRequest.cardColorId = id;
  }

  selectedLocationIcon() : string | undefined {
    const location = this.allLocations.find(location => location.name === this.userRequest.location);
    return location?.iconPath;
  }

  removeSelectedPredefinedBanner() {
    this.selectedBannerId = null;
    this.previewBanner = undefined;
    this.isPreviewBannerInserted = false;
  }

  onBannerSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    this.profileBanner = input.files![0];
    if (this.profileBanner) {
      const reader = new FileReader();
      reader.onload = () => {
        this.previewBanner = reader.result as string;
        this.isPreviewBannerInserted = true;
        this.selectedBannerId = null;
      }
      reader.readAsDataURL(this.profileBanner);
    }
  }

  onFileSelected(event: any) {

    const input = event.target as HTMLInputElement;
    // console.log(input.files);
    this.profilePicture = input.files![0];
    // console.log(this.profilePicture);
    if (this.profilePicture) {
      const reader = new FileReader();
      reader.onload = () => {
        this.previewImage = reader.result as string;
        this.isPreviewImageInserted = true;
      }
      reader.readAsDataURL(this.profilePicture);
    }
  }

  getProfilePicture(user: UserPrivateResponse) {
    if (user.userProfilePicture) {
      this.userHasProfilePicture = true;
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    } else {
      this.userHasProfilePicture = false;
    }
    return this.userHasProfilePicture;
  }

  removeProfileImage() {
    this.isPreviewImageInserted = false;
    this.previewImage = undefined;
  }

  showBanners() {
    this.showPredefinedBanners = !this.showPredefinedBanners;
  }

  selectedBanner(bannerId: number) {
    if (this.selectedBannerId === bannerId) {
      this.selectedBannerId = null;
      this.previewBanner = undefined;
    } else {
      this.selectedBannerId = bannerId;
      this.previewBanner = `assets/banners/banner_${bannerId}.jpg`;
    }
    console.log(bannerId);

  }

  removeSelectedCardColor() {
    this.selectedColorCode = '';
    this.selectedColorId = null;

  }

  saveChanges(){
    this.save.emit({
      profilePicture: this.profilePicture,
      profileBanner: this.profileBanner,
      selectedBannerId: this.selectedBannerId,
      favoriteGameId: this.selectedFavoriteGame?.gameId ?? null,
    });

  }

  get changesExist(): boolean {
    return this.selectedColorId !== null || this.isPreviewBannerInserted || this.isPreviewImageInserted || this.selectedBannerId !== null;
  }

  showPreview(){
    this.preview.emit({
      selectedColorCode: this.selectedColorCode,
      previewBanner: this.previewBanner,
      isPreviewImageInserted: this.isPreviewImageInserted,
      previewImage: this.previewImage
    });
  }


  searchLibrary(query: string) {
    this.searchedQuery = query;
    this.getLibraryGames(this.searchedQuery);
  }

  getLibraryGames(query: string) {
    this.userService.getLibraryGames({
      query: query
    }).subscribe({
      next: (result) => {
        this.userLibraryResponse = result;
        console.log(this.userLibraryResponse);
      }
    })
  }

  getGameImageCover(game: UserLibraryResponse): string {
    if (game.gameCoverImage) {
      return 'data:image/jpeg;base64,' + game.gameCoverImage;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  selectFavoriteGame(game: UserLibraryResponse) {
    this.searchedQuery = '';
    this.selectedFavoriteGame = game;
    this.userLibraryResponse = [];
  }

  removeSelectedGame() {
    this.selectedFavoriteGame = null;
    this.searchedQuery = '';
  }

  showGameSearch = false;

  changeFavoriteGame() {
    this.showGameSearch = true;
  }
}
