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
import {LfgPostCardComponent} from '../../../components/lfg-post-card/lfg-post-card.component';

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
    HlmTooltip,
    EmptyStateComponent,
    LoadingComponent,
    LfgPostCardComponent
  ],
  templateUrl: './lfg-posts.component.html',
  styleUrl: './lfg-posts.component.scss',
})
export class LfgPostsComponent implements OnInit {

  public page = -1;
  public savedPage = -1;
  public size = 12;

  savedCount = 0;
  myActivePostsCount = 0;

  allLfgResponse: PageResponseLfgResponse = {};
  savedLfgResponse: PageResponseLfgResponse = {};
  myActivePosts: LfgResponse[] = [];

  activeTab = 'All'
  openLfgFilter = false;
  searchQuery: string = '';
  maxActivePosts: number = 0;
  filteredAllPosts: LfgResponse[] = []
  filteredSavedPosts: LfgResponse[] = []

  isLoading = false;
  isLoadingSavedPosts = false;
  savedLoaded = false;
  isMyPostsLoading = false;


  constructor(
    private lfgService: LfgControllerService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService,
  ) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {

      const tab = params['lfgTab'] ?? 'All';
      const newAllPage = Number(params['page'] ?? 1) - 1;
      const newSavedPage = Number(params['savedPage'] ?? 1) - 1;

      const tabChanged = tab !== this.activeTab;
      const pageChanged = newAllPage !== this.page;
      const savedPageChanged = newSavedPage !== this.savedPage;

      this.activeTab = tab;
      this.page = newAllPage;
      this.savedPage = newSavedPage;

      if (this.activeTab === 'All' && (pageChanged || tabChanged)) {
        this.getAllLfgPosts();
      }

      if (this.activeTab === 'Saved' && (savedPageChanged || tabChanged)) {
        this.getSavedLfgPosts();
      }

      if (this.activeTab === 'MyPosts' && tabChanged) {
        this.getMyPosts();
      }

    })

    this.getMyActivePostsCount();
    this.getMyLfgPostsLimit();
    this.getSavedLfgPostsCount()
  }

  resetFilters() {

  }

  navigateToNewPost() {
    this.router.navigate(['/gamehub/find-players/new-lfg-post']);
  }

  onTabChange(tab: string | undefined) {

    const newTab = tab ?? 'All';
    if (newTab === this.activeTab) {
      return;
    }

    this.searchQuery = '';
    this.page = 0;
    this.savedPage = 0;

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        lfgTab: newTab === 'All' ? null : newTab,
        page: null,
        savedPage: null
      },
      queryParamsHandling: 'merge'
    });
  }

  getAllLfgPosts(query: string = "") {
    this.isLoading = true;
    this.lfgService.getAllActiveLfgPosts({
      query: query,
      page: this.page,
      size: this.size,
    }).subscribe({
      next: response => {
        this.allLfgResponse = response;
        this.filteredAllPosts = [...(response.content || [])];
        console.log(response);
        this.isLoading = false;
      },
      error: error => {
        this.toastService.error('Error with loading LFG posts')
        this.isLoading = false;
      }
    })
  }

  getSavedLfgPosts(query: string = "") {
    this.isLoadingSavedPosts = true;
    this.lfgService.getSavedLfgPosts({
      query: query,
      page: this.savedPage,
      size: this.size,
    }).subscribe({
      next: response => {
        this.savedLfgResponse = response;
        this.filteredSavedPosts = [...(response.content ?? [])];
        this.savedLoaded = true;
        this.isLoadingSavedPosts = false;
      },
      error: () => {
        this.toastService.error('Error with loading saved LFG posts');
        this.isLoadingSavedPosts = false;
      }
    })
  }

  getMyPosts() {
    this.isMyPostsLoading = true;
    this.lfgService.getMyActiveLfgPosts().subscribe({
      next: response => {
        this.myActivePosts = response;
        console.log(this.myActivePosts);
      }
    })
  }

  getSavedLfgPostsCount() {
    this.lfgService.getSavedLfgPostsCount().subscribe({
      next: response => {
        this.savedCount = response;
      }
    })
  }

  getMyActivePostsCount() {
    this.lfgService.getMyActiveLfgPostsCount().subscribe({
      next: response => {
        this.myActivePostsCount = response;
      }
    })
  }

  getMyLfgPostsLimit() {
    this.lfgService.getMyLfgPostsLimit().subscribe({
      next: response => {
        this.maxActivePosts = response;
        console.log(response);
      }
    })
  }

  toggleSave(post: LfgResponse) {
    if (post.saved) {
      this.removeFromSavedLfgPosts(post);
    } else {
      this.saveLfgPost(post);
    }
  }

  saveLfgPost(post: LfgResponse) {
    if (post.lfgId === undefined) {
      return;
    }
    this.lfgService.saveLfgPost({lfgPostId:post.lfgId}).subscribe({
      next: response => {
        post.saved = true;
        this.savedCount++;
        this.toastService.success('Lfg Post saved');
      },
      error: error => {
        this.toastService.error(error?.error?.message ?? 'Could not save post');
      }
    })
  }

  removeFromSavedLfgPosts(post: LfgResponse) {
    if (post.lfgId === undefined) {
      return;
    }
    this.lfgService.removeSavedLfgPost({lfgPostId: post.lfgId}).subscribe({
      next: response => {
        post.saved = false;
        this.savedCount--;
        this.toastService.success('Removed from saved Lfg posts');
        if (this.activeTab === 'Saved') {
          this.refreshSavedAfterRemove();
        }
      },
      error: error => {
        this.toastService.error(error?.error?.message ?? 'Could not remove the post from your saved list.');
      }
    })
  }

  private refreshSavedAfterRemove() {
    const wasLastOnPage = this.filteredSavedPosts.length === 1;

    if (wasLastOnPage && this.savedPage > 0) {
      this.changeSavedPage(this.savedPage -1)
    } else {
      this.getSavedLfgPosts();
    }
  }

  changePage(page: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: page + 1,
      },
      queryParamsHandling: 'merge'
    });
    this.scrollToTop();
  }

  changeSavedPage(page: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        savedPage: page + 1,
      },
      queryParamsHandling: 'merge'
    });
    this.scrollToTop();
  }

  private scrollToTop() {
    document.getElementById('page-container')?.scrollTo({top: 0});
  }

  searchLfgPosts(value: string) {
    const query = value.trim();

    if (query === this.searchQuery) {
      return;
    }

    this.searchQuery = value;
    this.page = 0;
    this.allLfgResponse = {};
    this.getAllLfgPosts(value);
  }

  searchSavedLfgPosts(value: string) {
    const query = value.trim();

    if (query === this.searchQuery) {
      return;
    }

    this.searchQuery = value;
    this.savedPage = 0;
    this.savedLfgResponse = {};
    this.getSavedLfgPosts(value);
  }

  getWhenLfgPostExpires(expiresAt: string | Date | null | undefined): number {
    if (!expiresAt) {
      return 0;
    }

    const msLeft = new Date(expiresAt).getTime() - Date.now();
    const msPerDay = 24 * 60 * 60 * 1000;

    return Math.max(0, Math.ceil(msLeft / msPerDay));
  }

  edit() {

  }
}
