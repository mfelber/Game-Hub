import {Component, OnInit} from '@angular/core';
import {DatePipe, NgClass, NgStyle} from '@angular/common';
import {SearchBar} from '../../../components/search-bar/search-bar';
import {ActivatedRoute, Router} from '@angular/router';
import {HlmTabs, HlmTabsContent, HlmTabsList, HlmTabsTrigger} from '@spartan/tabs';
import {LfgControllerService} from '../../../../../services/services/lfg-controller.service';
import {LfgResponse} from '../../../../../services/models/lfg-response';
import {PageResponseLfgResponse} from '../../../../../services/models/page-response-lfg-response';
import {PaginationComponent} from '../../../components/pagination/pagination.component';
import {UserCommunityResponse} from '../../../../../services/models/user-community-response';
import {TimeAgoPipe} from '../../../../../services/TimeAgoPipe/time-ago-pipe';
import {HlmTooltip} from '@spartan/tooltip';
import {ToastService} from '../../../../../services/ToastService/toast.service';
import {EmptyStateComponent} from '../../../components/empty-state/empty-state.component';
import {LoadingComponent} from '../../../components/loading/loading.component';

@Component({
  selector: 'app-lfg-posts',
  imports: [
    NgClass,
    SearchBar,
    HlmTabs,
    HlmTabsList,
    HlmTabsTrigger,
    HlmTabsContent,
    PaginationComponent,
    NgStyle,
    DatePipe,
    TimeAgoPipe,
    HlmTooltip,
    EmptyStateComponent,
    LoadingComponent
  ],
  templateUrl: './lfg-posts.component.html',
  styleUrl: './lfg-posts.component.scss',
})
export class LfgPostsComponent implements OnInit {

  public page = 0;
  public size = 12;

  allLfgResponse: PageResponseLfgResponse = {};

  activeTab = 'All'
  openLfgFilter = false;
  searchQuery: string = '';
  countOfActivePosts: number = 0;
  maxActivePosts: number = 0;
  filteredPosts: LfgResponse[] = []

  isLoading = false;


  constructor(
    private lfgService: LfgControllerService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService,
  ) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      this.page = Number(params['page'] ?? 1) - 1;
      this.getAllLfgPosts();
    })

    this.getCountOfActivePosts();
    this.getMyLfgPosts();
  }

  resetFilters() {

  }

  isExpiring(expiresAt: string | Date | null | undefined): boolean {
    if (!expiresAt) {
      return false;
    }

    const expiring = new Date(expiresAt).getTime();
    const twoDays = 2 * 24 * 60 * 60 * 1000;
    return expiring - Date.now() <= twoDays && expiring > Date.now();
  }

  navigateToNewPost() {
    this.router.navigate(['/gamehub/find-players/new-lfg-post']);
  }

  getAllLfgPosts() {
    this.isLoading = true;
    this.lfgService.getAllActiveLfgs({
      page: this.page,
      size: this.size,
    }).subscribe({
      next: response => {
        this.allLfgResponse = response;
        this.filteredPosts = [...(response.content || [])];
        console.log(response);
        this.isLoading = false;
      },
      error: error => {
        this.toastService.error('Error with loading LFG posts')
        this.isLoading = false;
      }
    })
  }

  getCountOfActivePosts() {
    this.lfgService.getCountOfActivePosts().subscribe({
      next: response => {
        this.countOfActivePosts = response;
        console.log(this.countOfActivePosts);
      }
    })
  }

  getMyLfgPosts() {
    this.lfgService.getMyLfgPosts().subscribe({
      next: response => {
        this.maxActivePosts = response;
        console.log(response);
      }
    })
  }

  onTabChange(tab: string | undefined) {
    console.log(tab);
    this.activeTab = tab ?? 'All';
    this.searchQuery = '';
  }

  changePage(page: number) {
    this.page = page;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: page + 1,
      },
      queryParamsHandling: 'merge'
    });
    this.scrollToTop();
  }

  private scrollToTop() {
    document.getElementById('page-container')?.scrollTo({top: 0});
  }

  getProfilePicture(user: UserCommunityResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return user.userProfilePicture;
  }
}
