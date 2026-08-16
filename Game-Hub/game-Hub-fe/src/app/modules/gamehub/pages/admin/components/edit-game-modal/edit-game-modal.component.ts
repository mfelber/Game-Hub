import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {GameResponse} from '../../../../../../services/models/game-response';
import {DecimalPipe, NgClass, NgForOf, NgIf, NgStyle} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {GenreResponse} from '../../../../../../services/models/genre-response';
import {PlatformResponse} from '../../../../../../services/models/platform-response';
import {LanguageResponse} from '../../../../../../services/models/language-response';
import {SubtitleResponse} from '../../../../../../services/models/subtitle-response';
import {AgeRatingResponse} from '../../../../../../services/models/age-rating-response';
import {StoreControllerService} from '../../../../../../services/services/store-controller.service';
import {AdminControllerService} from '../../../../../../services/services/admin-controller.service';
import {HttpClient} from '@angular/common/http';
import {GameUpdateRequest} from '../../../../../../services/models/game-update-request';
import {GameRequest} from '../../../../../../services/models/game-request';

@Component({
  selector: 'app-edit-game-modal',
  imports: [
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    NgClass,
    FormsModule,
    NgStyle,
    DecimalPipe
  ],
  templateUrl: './edit-game-modal.component.html',
  styleUrl: './edit-game-modal.component.scss',
})
export class EditGameModalComponent implements OnInit {

  @Input() game!: GameResponse
  @Output() close = new EventEmitter<void>();
  @Output() gameEdited = new EventEmitter<string>();

  isBasicInfoOpen: boolean = true;
  isPricingOpen: boolean = true;
  isCoverPhotoOpen: boolean = true;
  isDescriptionOpen: boolean = true;
  isSystemRequirementsOpen: boolean = false;
  isAgeRatingOpen: boolean = true;
  isGenreOptionOpen: boolean = true;
  isPlatformOptionOpen: boolean = true;
  isLanguageOptionOpen: boolean = true;
  isSubtitleOptionOpen: boolean = true;

  updateGameRequest: GameUpdateRequest = {
    title: '',
    description: '',
    publisher: '',
    developer: '',
    releaseYear: '',
    price: 0,
    discountPercent: null,
    cpu: '',
    gpu: '',
    ram: '',
    storage: '',
    platformIds: [],
    languageIds: [],
    subtitleIds: [],
    genresIds: [],
    ageRatingId: 0
  };

  originalGameRequest!: GameUpdateRequest;

  genreResponse: GenreResponse[] = [];
  platformResponse: PlatformResponse[] = [];
  languageResponse: LanguageResponse[] = [];
  subtitleResponses: SubtitleResponse[] = [];
  ageRatingResponse: AgeRatingResponse[] = [];

  gameHasGenresIds: number[] = []
  selectedGenresIds: Set<number> = new Set<number>();

  gameHasOsIds: number[] = [];
  selectedOsIds: Set<number> = new Set<number>();

  gameHasLanguagesIds: number[] = [];
  selectedLanguagesIds: Set<number> = new Set<number>();

  gameHasSubtitlesIds: number[] = [];
  selectedSubtitlesIds: Set<number> = new Set<number>();

  gameHasSelectedAgeRatingId: number = 0;

  coverPhoto: File | null = null;

  errorMessage: string = '';

  ngOnInit() {
    this.gameHasSelectedAgeRatingId = this.game.ageRating?.id ?? 0;
    this.gameHasGenresIds = this.game.genres?.map(g => g.id!) || [];
    this.gameHasOsIds = this.game.platforms?.map(os => os.id!) || [];
    this.gameHasLanguagesIds = this.game.languages?.map(l => l.id!) || [];
    this.gameHasSubtitlesIds = this.game.subtitles?.map(sub => sub.id!) || [];

    this.updateGameRequest = {
      title: this.game.title ?? '',
      description: this.game.description ?? '',
      publisher: this.game.publisher ?? '',
      developer: this.game.developer ?? '',
      releaseYear: this.game.releaseYear ?? '',
      price: this.game.price ?? 0,
      discountPercent: this.game.discountPercent ?? null,
      cpu: this.game.systemRequirements?.cpu ?? '',
      gpu: this.game.systemRequirements?.gpu ?? '',
      ram: this.game.systemRequirements?.ram ?? '',
      storage: this.game.systemRequirements?.storage ?? '',
      platformIds: this.game.platforms?.map(os => os.id!) || [],
      languageIds: this.game.languages?.map(l => l.id!) || [],
      subtitleIds: this.game.subtitles?.map(sub => sub.id!) || [],
      genresIds: this.game.genres?.map(g => g.id!) || [],
      ageRatingId: this.game.ageRating?.id ?? 0,
    }

    this.originalGameRequest = structuredClone(this.updateGameRequest);
    this.loadGameInfo();
  }

  constructor(
    private gameService: StoreControllerService,
    private adminService: AdminControllerService,
    private http: HttpClient) {
  }

  loadGameInfo() {
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
  }

  onFileSelected(event: any) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.coverPhoto = input.files[0];
    }
  }

  selectAgeRating(id: number) {
    this.gameHasSelectedAgeRatingId = id;
  }

  selectedGenre(id: number) {
    if (this.selectedGenresIds.has(id)) {
      this.selectedGenresIds.delete(id)
    } else {
      this.selectedGenresIds.add(id)
    }
  }

  removeSelectedGenre(id: number) {
    this.gameHasGenresIds = this.gameHasGenresIds.filter(g => g !== id);
    this.selectedGenresIds.delete(id);
  }

  selectedOs(id: number) {
    if (this.selectedOsIds.has(id)) {
      this.selectedOsIds.delete(id)
    } else {
      this.selectedOsIds.add(id)
    }
  }

  removeSelectedOs(id: number) {
    this.gameHasOsIds = this.gameHasOsIds.filter(os => os !== id);
    this.selectedOsIds.delete(id);
  }

  selectedLanguages(id: number) {
    if (this.selectedLanguagesIds.has(id)) {
      this.selectedLanguagesIds.delete(id)
    } else {
      this.selectedLanguagesIds.add(id)
    }
  }

  removeSelectedLanguage(id: number) {
    this.gameHasLanguagesIds = this.gameHasLanguagesIds.filter(l => l !== id);
    this.selectedLanguagesIds.delete(id);
  }

  selectedSubtitles(id: number) {
    if (this.selectedSubtitlesIds.has(id)) {
      this.selectedSubtitlesIds.delete(id)
    } else {
      this.selectedSubtitlesIds.add(id)
    }
  }

  removeSelectedSubtitle(id: number) {
    this.gameHasSubtitlesIds = this.gameHasSubtitlesIds.filter(sub => sub !== id);
    this.selectedSubtitlesIds.delete(id);
  }

  async saveGame() {

    if (!this.hasChanges()) {
      return;
    }

    if (!this.validateSaveGame()) {
      return;
    }

    try {
      if (this.coverPhoto) {
        await this.uploadCoverForGame(this.game.gameId!);
        console.log('Cover uploaded')
      }

      const allGenres = [...new Set([...this.gameHasGenresIds, ...this.selectedGenresIds])];
      const allOperationSystems = [...new Set([...this.gameHasOsIds, ...this.selectedOsIds])];
      const allLanguages = [...new Set([...this.gameHasLanguagesIds, ...this.selectedLanguagesIds])];
      const allSubtitles = [...new Set([...this.gameHasSubtitlesIds, ...this.selectedSubtitlesIds])];

      this.updateGameRequest.genresIds = allGenres;
      this.updateGameRequest.platformIds = allOperationSystems;
      this.updateGameRequest.languageIds = allLanguages;
      this.updateGameRequest.subtitleIds = allSubtitles;
      this.updateGameRequest.ageRatingId = this.gameHasSelectedAgeRatingId

      await this.adminService.updateGame({
        gameId: this.game.gameId!,
        body: this.updateGameRequest,
      }).toPromise()

      this.gameEdited.emit('Game edit successfully!')
      this.close.emit();

    } catch (error) {
      console.error(error);
    }

  }

  preventDecimalInput(event: KeyboardEvent) {
    if (event.key === '.' || event.key === ',') {
      event.preventDefault();
    }
  }

  removeDiscount() {
    this.updateGameRequest.discountPercent = null
  }

  limitDiscount(value: number | null) {

    if (value === null) {
      return;
    }

    if (value === 0) {
      this.updateGameRequest.discountPercent = null;
      return;
    }

    if (value! > 100) {
      this.errorMessage = 'Discount cannot be more than 100%.';
      return;
    }
    if (value! < 0) {
      this.errorMessage = 'Discount must be greater than 0.';
      return;
    }

    this.errorMessage = '';
  }

  private validateSaveGame() {
    const {
      title,
      description,
      publisher,
      developer,
      releaseYear,
      price,
      discountPercent,
      cpu,
      gpu,
      ram,
      storage
    } = this.updateGameRequest;

    const ramValue = Number(ram)
    const storageValue = Number(storage)

    if (!title?.trim() ||
      !publisher?.trim() ||
      !developer?.trim() ||
      !releaseYear) {
      this.errorMessage = 'All basic information fields must be filled in.';
      return false;
    }

    if (!description?.trim()) {
      this.errorMessage = 'Description cannot be empty.';
      return false;
    }

    if (ramValue <= 0 || storageValue <= 0) {
      this.errorMessage = 'RAM or Storage cannot be negative or empty.';
      return false;
    }

    if (!cpu?.trim() &&
      !gpu?.trim() &&
      !ram?.trim() && !storage?.trim()) {
      this.errorMessage = 'System requirements must be filled in!';
      return false;
    }

    if (this.gameHasSelectedAgeRatingId === 0) {
      this.errorMessage = 'Age rating must be selected.';
      return false;
    }

    if (this.selectedGenresIds.size === 0 && this.gameHasGenresIds.length === 0) {
      this.errorMessage = 'At least one genre must be selected.';
      return false;
    }

    if (this.selectedOsIds.size === 0 && this.gameHasOsIds.length === 0) {
      this.errorMessage = 'At least one operating system must be selected.';
      return false;
    }

    if (this.selectedLanguagesIds.size === 0 && this.gameHasLanguagesIds.length === 0) {
      this.errorMessage = 'At least one language must be selected.';
      return false;
    }

    if (this.selectedSubtitlesIds.size === 0 && this.gameHasSubtitlesIds.length === 0) {
      this.errorMessage = 'At least one subtitle must be selected.';
      return false;
    }

    if (price <= 0) {
      this.errorMessage = 'Price must be greater than 0 for paid games.';
      return false;
    }

    if (discountPercent !== null) {
      if (discountPercent! < 0) {
        this.errorMessage = 'Discount must be greater than 0.';
        return false;
      }

      if (discountPercent! > 100) {
        this.errorMessage = 'Discount cannot be more than 100%.';
        this.limitDiscount(discountPercent!)
        return false;
      }
    }

    return true;
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

  hasChanges(): boolean {
    if (this.coverPhoto !== null) {
      return true;
    }

    if (this.gameHasSelectedAgeRatingId !== this.originalGameRequest.ageRatingId) {
      return true;
    }

    const currentGenres = [...new Set([
      ...this.gameHasGenresIds,
      ...this.selectedGenresIds
    ])].sort();

    const currentOperationSystems = [...new Set([
      ...this.gameHasOsIds,
      ...this.selectedOsIds
    ])].sort();

    const currentLanguages = [...new Set([
      ...this.gameHasLanguagesIds,
      ...this.selectedLanguagesIds
    ])].sort();

    const currentSubtitles = [...new Set([
      ...this.gameHasSubtitlesIds,
      ...this.selectedSubtitlesIds
    ])].sort();

    const originalGenres = [...this.originalGameRequest.genresIds].sort();
    const originalOperationSystems = [...this.originalGameRequest.platformIds].sort();
    const originalLanguages = [...this.originalGameRequest.languageIds].sort();
    const originalSubtitles = [...this.originalGameRequest.subtitleIds].sort();

    if (JSON.stringify(currentGenres) !== JSON.stringify(originalGenres)) {
      return true;
    }

    if (JSON.stringify(currentOperationSystems) !== JSON.stringify(originalOperationSystems)) {
      return true;
    }

    if (JSON.stringify(currentLanguages) !== JSON.stringify(originalLanguages)) {
      return true;
    }

    if (JSON.stringify(currentSubtitles) !== JSON.stringify(originalSubtitles)) {
      return true;
    }

    return JSON.stringify(this.updateGameRequest) !==
      JSON.stringify(this.originalGameRequest);

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

  openCoverPhoto() {
    this.isCoverPhotoOpen = !this.isCoverPhotoOpen;
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

}
