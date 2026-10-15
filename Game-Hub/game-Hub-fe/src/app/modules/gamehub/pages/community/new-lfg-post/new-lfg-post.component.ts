import {Component, OnInit} from '@angular/core';
import {UserActionsComponent} from '../../../components/user-actions/user-actions.component';
import {SearchBar} from '../../../components/search-bar/search-bar';
import {Router} from '@angular/router';
import {LfgControllerService} from '../../../../../services/services/lfg-controller.service';
import {LfgOptionResponse} from '../../../../../services/models/lfg-option-response';
import {LfgOptionsResponse} from '../../../../../services/models/lfg-options-response';
import {DatePipe, NgClass} from '@angular/common';
import {ToastService} from '../../../../../services/ToastService/toast.service';
import {LoadingComponent} from '../../../components/loading/loading.component';
import {HlmSwitch} from '@spartan/switch';
import {HlmTextarea} from '@spartan/textarea';
import {ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-new-lfg-post',
  imports: [
    UserActionsComponent,
    SearchBar,
    NgClass,
    LoadingComponent,
    HlmSwitch,
    HlmTextarea,
    ReactiveFormsModule,
    DatePipe,
  ],
  templateUrl: './new-lfg-post.component.html',
  styleUrl: './new-lfg-post.component.scss',
})
export class NewLfgPostComponent implements OnInit {

  optionResponse: LfgOptionsResponse = {};

  selectedLookingFor: string | null = null;
  selectedCommitment: string | null = null;
  selectedPlatform: string | null = null;
  selectedVoiceChat: string | null = null;
  selectedPlayStyle: string | null = null;
  selectedDays: string[] = [];
  selectedGameTypes: string[] = [];
  selectedTimeOfDay: string | null = null;
  selectedPersonality: string | null = null;
  selectedLanguages: string[] = [];
  selectedRegions: string[] = [];
  selectedDuration: string | null = null;

  readonly maxGameTypes = 3;
  readonly maxLanguages = 3;
  readonly maxRegions = 3;

  isLoading = false;
  flexible = false;
  showAllLanguages = false;
  showAllGameTypes = false;

  ngOnInit(): void {
    this.loadLfgOptions()
  }

  constructor(
    private lfgService: LfgControllerService,
    private router: Router,
    private toastService: ToastService,
  ) {
  }

  loadLfgOptions() {
    this.isLoading = true;
    this.lfgService.getLfgOptions().subscribe({
      next: data => {
        console.log(data);
        this.optionResponse = data;
        this.selectedLookingFor = data.lookingFors?.[0]?.value ?? null;
        this.selectedCommitment = data.commitments?.[0]?.value ?? null;
        this.selectedVoiceChat = data.voiceChats?.[1]?.enumName ?? null;
        this.isLoading = false;
      },
      error: err => {
        this.isLoading = false;
        console.log(err);
        this.toastService.error("Error loading Lfg options");
      }
    })
  }

  selectLookingFor(value: string | undefined) {
    if (value) {
      this.selectedLookingFor = value;
    }
  }

  selectCommitment(value: string | undefined) {
    if (value) {
      this.selectedCommitment = value;
    }
  }

  selectPlatform(value: string | undefined) {
    if (value) {
      this.selectedPlatform = value;
    }
  }

  selectVoiceChat(value: string | undefined) {
    if (value) {
      this.selectedVoiceChat = value;
    }
  }

  selectPlayStyle(value: string | undefined) {
    if (value) {
      this.selectedPlayStyle = value;
    }
  }

  selectGameType(value: string | undefined) {
    if (!value) {
      return;
    }
    if (this.selectedGameTypes.includes(value)) {
      this.selectedGameTypes = this.selectedGameTypes.filter(gameType => gameType !== value);
      return;
    }

    if (this.selectedGameTypes.length >= this.maxGameTypes) {
      return;
    }

    this.selectedGameTypes = [...this.selectedGameTypes, value];
  }

  selectDuration(value: string | undefined) {
    if (value) {
      this.selectedDuration = value;
    }
  }

  isGameTypeDisabled(value: string | undefined): boolean {
    return this.selectedGameTypes.length >= this.maxGameTypes && !this.selectedGameTypes.includes(value!)
  }

  isLanguagesDisabled(value: string | undefined): boolean {
    return this.selectedLanguages.length >= this.maxLanguages && !this.selectedLanguages.includes(value!)
  }

  isRegionsDisabled(value: string | undefined): boolean {
    return this.selectedRegions.length >= this.maxRegions && !this.selectedRegions.includes(value!)
  }

  toggleDay(value: string | undefined) {
    if (!value) {
      return;
    }
    this.selectedDays = this.selectedDays.includes(value)
      ? this.selectedDays.filter(day => day !== value)
      : [...this.selectedDays, value];
  }

  toggleLanguage(value: string | undefined) {
    if (!value) {
      return;
    }

    this.selectedLanguages = this.selectedLanguages.includes(value)
      ? this.selectedLanguages.filter(language => language !== value)
      : [...this.selectedLanguages, value];
  }

  toggleRegion(value: string | undefined) {
    if (!value) {
      return;
    }

    if (this.selectedRegions.includes(value)) {
      this.selectedRegions = this.selectedRegions.filter(region => region !== value);
      return;
    }

    if (this.selectedRegions.length >= this.maxLanguages) {
      return;
    }

    this.selectedRegions = [...this.selectedRegions, value];
  }

  selectTimeOfDay(value: string | undefined) {
    if (value) {
      this.selectedTimeOfDay = value;
    }
  }

  selectPersonality(value: string | undefined) {
    if (!value) {
      return;
    }
    this.selectedPersonality = this.selectedPersonality === value ? null : value;
  }

  get selectedCommitmentDescription(): string | undefined {
    return this.optionResponse.commitments?.find(c => c.value === this.selectedCommitment)?.description;
  }

  get selectedPlayStyleDescription(): string | undefined {
    return this.optionResponse.playStyles?.find(p => p.value === this.selectedPlayStyle)?.description;
  }

  get selectedPersonalityDescription(): string | undefined {
    return this.optionResponse.personalities?.find(p => p.value === this.selectedPersonality)?.description;
  }

  get numberOfSelectedGameTypes(): number {
    return this.selectedGameTypes?.length ? this.selectedGameTypes.length : 0;
  }

  get numberOfSelectedLanguages(): number {
    return this.selectedLanguages?.length ? this.selectedLanguages.length : 0;
  }

  get numberOfSelectedRegions(): number {
    return this.selectedRegions?.length ? this.selectedRegions.length : 0;
  }

  get isAnyRegion(): boolean {
    return this.selectedRegions.length === 0;
  }

  selectAnyRegion() {
    this.selectedRegions = [];
  }

  get visibleLanguages() {
    const all = this.optionResponse.preferredLanguages ?? [];

    if (this.showAllLanguages) {
      return all;
    }

    const firstFive = all.slice(0, 5);
    const hiddenSelected = all.slice(5).filter(l => this.selectedLanguages.includes(l.name!));

    return [...firstFive, ...hiddenSelected];
  }

  get hiddenLanguagesCount(): number {
    const total = this.optionResponse.preferredLanguages?.length ?? 0;
    return total - this.visibleLanguages.length;
  }

  get visibleGameTypes() {
    const all = this.optionResponse.gameTypes ?? [];

    if (this.showAllGameTypes) {
      return all;
    }
    const firstTwelve = all.slice(0, 12);
    const hiddenSelected = all.slice(12).filter(gt => this.selectedGameTypes.includes(gt.value!));
    return [...firstTwelve, ...hiddenSelected];
  }

  get hiddenGameTypes(): number {
    const total = this.optionResponse.gameTypes?.length ?? 0;
    return total - this.visibleGameTypes.length;
  }

  get selectedDurationDays(): number | undefined{
    return this.optionResponse.postDurations?.find(d => d.name === this.selectedDuration)?.days;
  }

  get expirationDate(): Date | null {
    const days = this.selectedDurationDays;
    if (!days) {
      return null;
    }

    const date = new Date();
    date.setDate(date.getDate() + days);
    return date;
  }

  onFlexibleChange(value: boolean) {
    this.flexible = value;
  }
}
