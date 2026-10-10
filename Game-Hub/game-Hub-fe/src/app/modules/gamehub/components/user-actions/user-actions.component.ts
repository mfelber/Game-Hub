import {Component, OnInit, signal} from '@angular/core';

import {UserProfileControllerService} from '../../../../services/services/user-profile-controller.service';
import {UserNotificationsResponse} from '../../../../services/models/user-notifications-response';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {
  HlmDialog,
  HlmDialogContent,
  HlmDialogDescription, HlmDialogFooter,
  HlmDialogHeader, HlmDialogPortal,
  HlmDialogTitle,
  HlmDialogTrigger
} from '@spartan/dialog';
import {HlmButton} from '@spartan/button';
import {NewsControllerService} from '../../../../services/services/news-controller.service';
import {NewsResponse} from '../../../../services/models/news-response';
import {DatePipe, NgClass} from '@angular/common';
import {NewsOverviewResponse} from '../../../../services/models/news-overview-response';
import {BrnDialogState} from '@spartan-ng/brain/dialog';
import {SettingsControllerService} from '../../../../services/services/settings-controller.service';
import {Observable} from 'rxjs';
import {StoreControllerService} from '../../../../services/services/store-controller.service';
import {FormsModule} from '@angular/forms';
import {platform} from 'node:os';
import {OnboardRequest} from '../../../../services/models/onboard-request';
import {ToastService} from '../../../../services/ToastService/toast.service';

@Component({
  selector: 'app-user-actions',
  imports: [
    RouterLink,
    HlmDialog,
    HlmDialogContent,
    HlmDialogTrigger,
    HlmDialogHeader,
    HlmDialogTitle,
    HlmDialogDescription,
    HlmDialogPortal,
    DatePipe,
    NgClass,
    HlmDialogFooter,
    FormsModule
  ],
  templateUrl: './user-actions.component.html',
  styleUrl: './user-actions.component.scss',
})
export class UserActionsComponent implements OnInit {

  userNotificationsResponse: UserNotificationsResponse = {};

  newsResponse: NewsOverviewResponse = {};

  newsDialogState: BrnDialogState = 'closed';
  onBoardingDialogState: BrnDialogState = 'closed';

  genreResponse: any[] = [];
  selectedGenres: Set<number> = new Set();

  selectedPlatform = signal<OnboardRequest['mainPlatform'] | null>(null);
  selectedVoiceChat = signal<OnboardRequest['microphoneUsage'] | null>(null);
  selectedRegion = signal<OnboardRequest['region'] | null>(null);

  regions: { value: NonNullable<OnboardRequest['region']>; label: string; cls: string }[] = [
    { value: 'EUROPE', label: 'Europe',                      cls: 'region-europe' },
    { value: 'NA',     label: 'North America',               cls: 'region-na' },
    { value: 'SA',     label: 'South America',               cls: 'region-sa' },
    { value: 'CAB',    label: 'Central America & Caribbean', cls: 'region-cab' },
    { value: 'ASIA',   label: 'Asia',                        cls: 'region-asia' },
    { value: 'ME',     label: 'Middle East',                 cls: 'region-me' },
    { value: 'AFRICA', label: 'Africa',                      cls: 'region-africa' },
    { value: 'OC',     label: 'Oceania',                     cls: 'region-oc' },
  ];

  constructor(
    private gameService: StoreControllerService,
    private userService: UserProfileControllerService,
    private settingService: SettingsControllerService,
    private newsService: NewsControllerService,
    private toastService: ToastService,
  ) {
  }

  ngOnInit() {
    this.loadNotifications();
    this.isUserOnboarded();
  }

  loadNotifications() {
    this.userService.getUserNotifications().subscribe({
      next: data => {
        this.userNotificationsResponse = data
      }
    })
  }

  isUserOnboarded() {
    this.settingService.isUserOnboarded().subscribe({
      next: isUserOnboarded => {
        if (isUserOnboarded === true) {
          this.loadNews()
        } else {
          this.openOnboarding();
        }
      }
    });
  }

  loadNews() {
    this.newsService.getNews().subscribe({
      next: data => {
        this.newsResponse = data;
        if (data.hasUnseenNews && data.news?.length) {
          setTimeout(() => {
            this.newsDialogState = 'open';
            this.markNewsAsSeen(data.news![0].newsId);
          }, 650)
        }
      }
    })
  }

  markNewsAsSeen(newsId: number) {
    this.newsService.markNewsAsSeen({
      body: newsId
    }).subscribe({
      next: data => {
      }
    })
  }

  openOnboarding() {
    this.onBoardingDialogState = 'open';
    this.gameService.getAllGenres().subscribe({
      next: (genres) => {
        this.genreResponse = genres;
      }
    })
  }

  selectPlatform(platform: OnboardRequest['mainPlatform']): void {
    this.selectedPlatform.set(platform);
  }

  selectVoiceChat(voiceChat: OnboardRequest['microphoneUsage']): void {
    this.selectedVoiceChat.set(voiceChat);
  }

  selectRegion(region: OnboardRequest['region']): void {
    this.selectedRegion.set(region);
  }

  selectGenres(id: number) {
    const genres = new Set(this.selectedGenres);

    if (genres.has(id)) {
      genres.delete(id);
    } else {
      genres.add(id);
    }

    this.selectedGenres = genres;
  }

  requiredFieldSelected(): boolean {
    if (this.selectedPlatform() === null) {
      return false;
    }

    if (this.selectedVoiceChat() === null) {
      return false;
    }

    if (this.selectedRegion() === null) {
      return false;
    }


    return true;
  }

  onboardUser() {

    const onboardReq: OnboardRequest = {
      mainPlatform: this.selectedPlatform()!,
      microphoneUsage: this.selectedVoiceChat()!,
      region: this.selectedRegion()!,
      genreIds: Array.from(this.selectedGenres),
    };

    this.settingService.onboardUser({
      body: onboardReq
    }).subscribe({
      next: () => {
        this.onBoardingDialogState = 'closed';
        this.toastService.success('Preferences saved. Welcome!');
      },
      error: err => {
        this.toastService.error("We couldn't save your preferences. Please try again.");
        this.onBoardingDialogState = 'open';
        console.log(err)
      }
    })
  }
}
