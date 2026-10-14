import {Component, OnInit} from '@angular/core';
import {UserActionsComponent} from '../../../components/user-actions/user-actions.component';
import {SearchBar} from '../../../components/search-bar/search-bar';
import {Router} from '@angular/router';
import {LfgControllerService} from '../../../../../services/services/lfg-controller.service';
import {LfgOptionResponse} from '../../../../../services/models/lfg-option-response';
import {LfgOptionsResponse} from '../../../../../services/models/lfg-options-response';
import {NgClass} from '@angular/common';
import {ToastService} from '../../../../../services/ToastService/toast.service';
import {LoadingComponent} from '../../../components/loading/loading.component';
import {HlmSwitch} from '@spartan/switch';

@Component({
  selector: 'app-new-lfg-post',
  imports: [
    UserActionsComponent,
    SearchBar,
    NgClass,
    LoadingComponent,
    HlmSwitch,
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
  selectedTimeOfDay: string | null = null;
  selectedPersonality: string | null = null;

  isLoading = false;
  flexible = false;

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

  toggleDay(value: string | undefined) {
    if (!value) {
      return;
    }
    this.selectedDays = this.selectedDays.includes(value)
      ? this.selectedDays.filter(day => day !== value)
      : [...this.selectedDays, value];
  }

  selectTimeOfDay(value: string | undefined) {
    if (value) {
      this.selectedTimeOfDay = value;
    }
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

  selectPersonality(value: string | undefined) {
    if (!value) {
      return;
    }
    this.selectedPersonality = this.selectedPersonality === value ? null : value;
  }
}
