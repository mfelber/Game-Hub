import {Component, OnInit, ViewChild} from '@angular/core';
import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserActionsComponent} from '../../components/user-actions/user-actions.component';
import {HlmTabs, HlmTabsContent, HlmTabsList, HlmTabsTrigger} from '@spartan/tabs';
import {PrivacySettingsResponse} from '../../../../services/models/privacy-settings-response';
import {StoreSettingsResponse} from '../../../../services/models/store-settings-response';
import {CommunitySettingsResponse} from '../../../../services/models/community-settings-response';
import {HlmDropdownMenu, HlmDropdownMenuItem, HlmDropdownMenuTrigger} from '@spartan/dropdown-menu';
import {HlmSwitch} from '@spartan/switch';
import {FormsModule} from '@angular/forms';
import {HlmTooltip} from '@spartan/tooltip';
import {HlmInputGroup, HlmInputGroupAddon, HlmInputGroupButton, HlmInputGroupInput} from '@spartan/input-group';
import {BrnTabs} from '@spartan-ng/brain/tabs';
import {SettingsControllerService} from '../../../../services/services/settings-controller.service';
import {UserSettingsUpdateRequests} from '../../../../services/models/user-settings-update-requests';
import {
  HlmDialog,
  HlmDialogClose,
  HlmDialogContent,
  HlmDialogDescription, HlmDialogFooter, HlmDialogHeader,
  HlmDialogPortal, HlmDialogTitle
} from '@spartan/dialog';
import {NgClass, NgStyle} from '@angular/common';
import {UserSettingsResponse} from '../../../../services/models/user-settings-response';
import {UserPrivateResponse} from '../../../../services/models/user-private-response';
import {HlmButton} from '@spartan/button';
import {HlmField, HlmFieldLabel} from '@spartan/field';
import {HlmInput} from '@spartan/input';
import {HlmTextarea} from '@spartan/textarea';

@Component({
  selector: 'app-settings',
  imports: [
    UserActionsComponent,
    HlmTabs,
    HlmTabsList,
    HlmTabsTrigger,
    HlmTabsContent,
    HlmDropdownMenuTrigger,
    HlmDropdownMenu,
    HlmDropdownMenuItem,
    HlmSwitch,
    FormsModule,
    HlmTooltip,
    HlmInputGroupButton,
    HlmDialog,
    HlmDialogContent,
    HlmDialogPortal,
    HlmDialogClose,
    HlmDialogDescription,
    HlmDialogFooter,
    HlmDialogHeader,
    HlmDialogTitle,
    NgClass,
    HlmInputGroup,
    HlmInputGroupAddon,
    HlmInputGroupInput,
    NgStyle,
    HlmButton,
    HlmField,
    HlmFieldLabel,
    HlmInput,
    HlmTextarea
  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss',
})
export class SettingsComponent implements OnInit {

  originalPrivacySettingsResponse: PrivacySettingsResponse[] = [];
  privacySettingsResponse: PrivacySettingsResponse[] = [];

  originalStoreSettingsResponse: StoreSettingsResponse[] = [];
  storeSettingsResponse: StoreSettingsResponse[] = [];

  originalCommunitySettingsResponse: CommunitySettingsResponse[] = [];
  communitySettingsResponse: CommunitySettingsResponse[] = [];

  response: UserSettingsResponse = {};

  activeTab: string = 'profile';

  @ViewChild(BrnTabs)
  tabs!: BrnTabs;

  @ViewChild('warningDialog')
  warningDialog!: HlmDialog;

  constructor(
    private userService: UserProfileControllerService,
    private settingsService: SettingsControllerService,
  ) {
  }

  ngOnInit() {
    console.log('Initial tab:', this.activeTab);
    this.userService.getUserSettings().subscribe(
      userSettings => {
        console.log(userSettings);
        this.response = userSettings;
        this.privacySettingsResponse = userSettings.privacySettingsResponse ?? []
        this.originalPrivacySettingsResponse = userSettings.privacySettingsResponse?.map(setting => ({
          ...setting,
        })) ?? [];

        this.storeSettingsResponse = userSettings.storeSettingsResponse ?? [];
        this.originalStoreSettingsResponse = userSettings.storeSettingsResponse?.map(setting => ({
          ...setting
        })) ?? [];


        this.communitySettingsResponse = userSettings.communitySettingsResponse ?? [];
        this.originalCommunitySettingsResponse = userSettings.communitySettingsResponse?.map(setting => ({
          ...setting,
        })) ?? [];
      }
    )
  }

  savePrivacySettings() {
    const request: UserSettingsUpdateRequests = {
      communitySettingsIds: [],
      privacySettingsIds: this.privacySettingsResponse.map(setting => ({
        settingId: setting.settingId!,
        access: this.getAccessEnum(setting.access),
      })),
      storeSettingsIds: [],
    }

    this.settingsService.updateUserSettings({body: request}).subscribe({
      next: response => {
        this.originalPrivacySettingsResponse = this.privacySettingsResponse.map(setting => ({
          settingId: setting.settingId,
          access: setting.access
        }));

        this.warningDialog.close();

      },
      error: error => {
        console.log(error);
      }
    })
  }

  saveCommunitySettings() {
    const request: UserSettingsUpdateRequests = {
      communitySettingsIds: this.communitySettingsResponse.map(setting => ({
        settingId: setting.settingId!,
        access: this.getAccessEnum(setting.access),
      })),
      privacySettingsIds: [],
      storeSettingsIds: [],
    }

    this.settingsService.updateUserSettings({body: request}).subscribe({
      next: response => {
        this.originalCommunitySettingsResponse = this.communitySettingsResponse.map(setting => ({
          settingId: setting.settingId,
          access: setting.access
        }))

        this.warningDialog.close();
      },
      error: error => {
        console.log(error);
      }
    })
  }

  saveStoreSettings() {
    const request: UserSettingsUpdateRequests = {
      communitySettingsIds: [],
      privacySettingsIds: [],
      storeSettingsIds: this.storeSettingsResponse.map(setting => ({
        settingId: setting.settingId!,
        disabled: setting.disabled
      }))
    }
    console.log(request);
    this.settingsService.updateUserSettings({body: request}).subscribe({
      next: response => {
        this.originalStoreSettingsResponse = this.storeSettingsResponse.map(setting => ({
          ...setting,
        }))
        this.warningDialog.close();
      },
      error: error => {
        console.log(error);
      }
    })
  }

  isPrivacySettingsChanged(setting: PrivacySettingsResponse): boolean {
    const originalSettings = this.originalPrivacySettingsResponse.find(
      original => original.settingId === setting.settingId
    );

    return originalSettings?.access !== setting.access;
  }

  hasPrivacyChanges(): boolean {
    return this.privacySettingsResponse.some(setting =>
      this.isPrivacySettingsChanged(setting)
    );
  }

  isCommunitySettingsChanged(setting: CommunitySettingsResponse): boolean {
    const originalSettings = this.originalCommunitySettingsResponse.find(
      original => original.settingId === setting.settingId
    );

    return originalSettings?.access !== setting.access;
  }

  hasCommunityChanges() {
    return this.communitySettingsResponse.some(setting => this.isCommunitySettingsChanged(setting));
  }

  isStoreSettingsChanged(setting: StoreSettingsResponse): boolean {
    const originalSettings = this.originalStoreSettingsResponse.find(
      original => original.settingId === setting.settingId
    );

    return originalSettings?.disabled !== setting.disabled;
  }

  hasStoreChanges(): boolean {
    return this.storeSettingsResponse.some(setting => this.isStoreSettingsChanged(setting));
  }

  private getAccessEnum(
    access: string | undefined
  ): 'EVERYONE' | 'FRIENDS' | 'NO_ONE' {
    if (access === 'Everyone') {
      return 'EVERYONE';
    }

    if (access === 'Friends') {
      return 'FRIENDS';
    }

    return 'NO_ONE';
  }

  hasUnsavedChanges(): boolean {
    if (this.activeTab === 'privacy') {
      return this.hasPrivacyChanges();
    }

    if (this.activeTab === 'community') {
      return this.hasCommunityChanges();
    }

    if (this.activeTab === 'store') {
      return this.hasStoreChanges();
    }

    return false;
  }

  saveChanges() {
    if (this.activeTab === 'privacy') {
      this.savePrivacySettings();
    }

    if (this.activeTab === 'community') {
      this.saveCommunitySettings();
    }

    if (this.activeTab === 'store') {
      this.saveStoreSettings();
    }
  }

  discardChanges() {
    if (this.activeTab === 'privacy') {
      this.privacySettingsResponse = this.originalPrivacySettingsResponse.map(setting => ({
        ...setting
      }))
    }
    if (this.activeTab === 'community') {
      this.communitySettingsResponse = this.originalCommunitySettingsResponse.map(setting => ({
        ...setting
      }))
    }

    if (this.activeTab === 'store') {
      this.storeSettingsResponse = this.originalStoreSettingsResponse.map(setting => ({
        ...setting
      }))
    }
  }

  onTabChange(tab: string | undefined) {
    if (tab) {
      if (this.hasUnsavedChanges()) {
        this.tabs.setActiveTab(this.activeTab);
        setTimeout(() => {
          this.warningDialog.open();
        });
        return;
      }
      this.activeTab = tab;
      this.tabs.setActiveTab(tab);
      console.log(this.activeTab);
    }
  }

  // getProfilePicture(user: UserPrivateResponse) {
  //   if (user.userProfilePicture) {
  //     return 'data:image/jpeg;base64,' + user.userProfilePicture;
  //   }
  //
  //   return this.hasProfilePicture;
  // }

  getPegiColor(pegiRatingName: string | undefined): string {
    switch (pegiRatingName) {
      case 'PEGI 3':
        return 'text-[#a4c500cc]';
      case 'PEGI 7':
        return 'text-[#a4c500cc]';
      case 'PEGI 12':
        return 'text-[#f69f00cc]';
      case 'PEGI 16':
        return 'text-[#f69f00cc]';
      case 'PEGI 18':
        return 'text-[#e20217cc]';

      default:
        return 'text-white';
    }
  }

  get unsavedChangesTitle(): string {
    return `${this.activeTab}`;
  }

  getGameImageCover(game: UserSettingsResponse): string {
    if (game.favoriteGame) {
      return 'data:image/jpeg;base64,' + game.favoriteGame;
    }
    return 'https://images.pexels.com/photos/1054655/pexels-photo-1054655.jpeg';
  }

  getBanner(user: UserPrivateResponse) {
    if (user.bannerImage) {
      return 'data:image/jpeg;base64,' + user.bannerImage;
    }
    return user.predefinedBannerPath;
  }
}
