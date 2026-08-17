import {Component, ElementRef, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {GameRequest} from '../../../../../../services/models/game-request';
import {GenreResponse} from '../../../../../../services/models/genre-response';
import {PlatformResponse} from '../../../../../../services/models/platform-response';
import {LanguageResponse} from '../../../../../../services/models/language-response';
import {SubtitleResponse} from '../../../../../../services/models/subtitle-response';
import {NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {StoreControllerService} from '../../../../../../services/services/store-controller.service';
import {MatSlideToggle} from '@angular/material/slide-toggle';
import {AgeRatingResponse} from '../../../../../../services/models/age-rating-response';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {HttpClient} from '@angular/common/http';
import {
  SystemRequirementsControllerService
} from '../../../../../../services/services/system-requirements-controller.service';
import {UnitSizeResponse} from '../../../../../../services/models/unit-size-response';

@Component({
  selector: 'app-add-game-modal',
  imports: [
    FormsModule,
    NgForOf,
    NgClass,
    NgIf,
    MatSlideToggle,
    NgStyle
  ],
  templateUrl: './add-game-modal.component.html',
  styleUrl: './add-game-modal.component.scss',
})
export class AddGameModalComponent implements OnInit {

  @Output() close = new EventEmitter<void>();
  @Output() gameAdded = new EventEmitter<string>();

  selectedGenres: Set<number> = new Set<number>()
  selectedPlatforms: Set<number> = new Set<number>()
  selectedLanguages: Set<number> = new Set<number>()
  selectedSubtitles: Set<number> = new Set<number>()
  selectedAgeRating: number = 0;
  coverPhoto: File | null = null;

  genreResponse: GenreResponse[] = [];
  platformResponse: PlatformResponse[] = [];
  languageResponse: LanguageResponse[] = [];
  subtitleResponses: SubtitleResponse[] = [];
  ageRatingResponse: AgeRatingResponse[] = [];
  unitSizeResponse: UnitSizeResponse[] = [];
  isGameToggleFree: boolean = true;

  isBasicInfoOpen: boolean = true;
  isPricingOpen: boolean = true;
  isDescriptionOpen: boolean = true;
  isSystemRequirementsOpen: boolean = false;
  isAgeRatingOpen: boolean = true;
  isGenreOptionOpen: boolean = true;
  isPlatformOptionOpen: boolean = true;
  isLanguageOptionOpen: boolean = true;
  isSubtitleOptionOpen: boolean = true;

  errorMessage: string = '';

  gameRequest: GameRequest = {
    title: '',
    genresIds: [],
    description: '',
    publisher: '',
    developer: '',
    releaseYear: '',
    price: 0,
    cpu: '',
    gpu: '',
    ram: 0,
    storage: 0,
    gameUnitSize: 'MB',
    platformIds: [],
    languageIds: [],
    subtitleIds: [],
    ageRatingId: 0
  };

  constructor(
    private gameService: StoreControllerService,
    private adminService: AdminControllerService,
    private systemReqService: SystemRequirementsControllerService,
    private http: HttpClient) {
  }

  ngOnInit() {
    this.loadGameFormData();
  }

  private loadGameFormData() {
    this.gameService.getAllGenres().subscribe({
      next: (genres) => {
        this.genreResponse = genres;
      }
    })
    this.gameService.getAllPlatforms().subscribe({
      next: (platforms) => {
        this.platformResponse = platforms;
      }
    })
    this.gameService.getLanguages().subscribe({
      next: (languages) => {
        this.languageResponse = languages;
      }
    })
    this.gameService.getSubtitles().subscribe({
      next: (subtitles) => {
        this.subtitleResponses = subtitles;
      }
    })
    this.gameService.getAgeRating().subscribe({
      next: (ageRating) => {
        this.ageRatingResponse = ageRating;
      }
    })
    this.systemReqService.getUnitSizes().subscribe({
      next: (unitSize) => {
        this.unitSizeResponse = unitSize;
        console.log(unitSize);
      }
    })
  }

  selectedGenre(id: number) {
    this.errorMessage = '';
    if (this.selectedGenres.has(id)) {
      this.selectedGenres.delete(id)
    } else {
      this.selectedGenres.add(id)
    }
    console.log(this.selectedGenres)
  }

  selectedPlatform(id: number) {
    this.errorMessage = '';
    if (this.selectedPlatforms.has(id)) {
      this.selectedPlatforms.delete(id)
    } else {
      this.selectedPlatforms.add(id)
    }
    console.log(this.selectedPlatforms)
  }

  selectedLanguage(id: number) {
    this.errorMessage = '';
    if (this.selectedLanguages.has(id)) {
      this.selectedLanguages.delete(id)
    } else {
      this.selectedLanguages.add(id)
    }
    console.log(this.selectedLanguages)
  }

  selectedSubtitle(id: number) {
    this.errorMessage = '';
    if (this.selectedSubtitles.has(id)) {
      this.selectedSubtitles.delete(id)
    } else {
      this.selectedSubtitles.add(id)
    }
    console.log(this.selectedSubtitles)
  }

  selectAgeRating(id: number) {
    this.errorMessage = '';
    this.selectedAgeRating = id;
    console.log(this.selectedAgeRating)
  }

  setGameAsFree() {
    this.isGameToggleFree = !this.isGameToggleFree
    if (this.isGameToggleFree) {
      this.gameRequest.price = 0;
    }
    console.log(this.gameRequest.price)
  }

  onFileSelected(event: any) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.coverPhoto = input.files[0];
    }
  }

  gameId: number = 0;

  async saveGame(event: any) {
    this.errorMessage = '';
    if (this.errorMessage.length) {
      return;
    }

    if (!this.validateSaveGame()) {
      return;
    }

    this.gameRequest.genresIds = [...this.selectedGenres]
    this.gameRequest.platformIds = [...this.selectedPlatforms]
    this.gameRequest.languageIds = [...this.selectedLanguages]
    this.gameRequest.subtitleIds = [...this.selectedSubtitles]
    this.gameRequest.ageRatingId = this.selectedAgeRating
    this.adminService.addGame({
      body: this.gameRequest
    }).subscribe({
      next: async (gameId) => {
        console.log(gameId)
        this.gameId = gameId;
        await this.uploadCoverForGame(gameId);
        this.gameAdded.emit('Game added successfully!');
        this.close.emit();
    }
    })
  }

  async uploadCoverForGame(gameId: number) {
    if (!this.coverPhoto) {
      return;
    }
    try {
      if (this.coverPhoto) {
        const formData = new FormData();
        formData.append('file', this.coverPhoto);
        try {
          await this.http.post(`http://localhost:8088/api/v1/admin/cover/${gameId}`, formData).toPromise();
        } catch (e) {
          console.error(e);
        }
      }
    } catch (error) {
      console.log(error)
    }
  }

  validateSaveGame() {
    const {title,
      genresIds,
      description,
      publisher,
      developer,
      releaseYear,
      price,
      cpu,
      gpu,
      ram,
      storage,
      platformIds,
      languageIds,
      subtitleIds,
      ageRatingId} = this.gameRequest

    if (!title?.trim() &&
      !genresIds?.length &&
      !description?.trim() &&
      !publisher?.trim() &&
      !developer?.trim() &&
      !releaseYear &&
      !this.coverPhoto &&
      !cpu?.trim() &&
      !gpu?.trim() &&
      ram != null &&
      storage != null &&
      !platformIds?.length &&
      !languageIds?.length &&
      !subtitleIds?.length &&
      !ageRatingId) {
      console.log("nemoze byt nic prazdne")
      this.errorMessage = 'Every field must be filled in!'
      return false;
    }

    if (!title?.trim() ||
      !publisher?.trim() ||
      !developer?.trim() ||
      !releaseYear) {
      this.errorMessage = 'All basic information fields must be filled in.';
      return false;
    }

    if (!this.coverPhoto) {
      this.errorMessage = 'Cover image must be selected.';
      return false;
    }

    if (!this.isGameToggleFree && price === 0) {
      this.errorMessage = 'Price must be greater than 0 for paid games.';
      return false;
    }

    if (!description?.trim()) {
      this.errorMessage = 'Description cannot be empty.';
      return false;
    }

    if (!cpu?.trim() &&
      !gpu?.trim() &&
      ram != null &&
      storage != null) {
      this.errorMessage = 'System requirements must be filled in!';
      return false;
    }

    if (ram <= 0 || storage <= 0) {
      this.errorMessage = 'RAM or Storage cannot be negative or empty.';
      return false;
    }

    if (this.selectedAgeRating === 0) {
      this.errorMessage = 'Age rating must be selected.';
      return false;
    }

    if (this.selectedGenres.size === 0) {
      this.errorMessage = 'At least one genre must be selected.';
      return false;
    }

    if (this.selectedPlatforms.size === 0) {
      this.errorMessage = 'At least one operating system must be selected.';
      return false;
    }

    if (this.selectedLanguages.size === 0) {
      this.errorMessage = 'At least one language must be selected.';
      return false;
    }

    if (this.selectedSubtitles.size === 0) {
      this.errorMessage = 'At least one subtitle must be selected.';
      return false;
    }

    if (price < 0) {
      this.errorMessage = 'Price must be greater than 0 for paid games.';
      return false;
    }

    return true;
  }

  openSystemRequirements() {
    this.isSystemRequirementsOpen = !this.isSystemRequirementsOpen;
  }

  openBasicInfo() {
    this.isBasicInfoOpen = !this.isBasicInfoOpen;
  }

  openPricing() {
    this.isPricingOpen = !this.isPricingOpen;
  }

  openDescription() {
    this.isDescriptionOpen = !this.isDescriptionOpen;
  }

  openAgeRatingOption() {
    this.isAgeRatingOpen = !this.isAgeRatingOpen;
  }

  openGenreOption() {
    this.isGenreOptionOpen = !this.isGenreOptionOpen;
  }

  openPlatformOption() {
    this.isPlatformOptionOpen = !this.isPlatformOptionOpen;
  }

  openLanguageOption() {
    this.isLanguageOptionOpen = !this.isLanguageOptionOpen;
  }

  openSubtitleOption() {
    this.isSubtitleOptionOpen = !this.isSubtitleOptionOpen;
  }

  limitPrice(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = Number(input.value);

    if (value <= 0 ) {
      this.gameRequest.price = 0;
    }
  }
}
