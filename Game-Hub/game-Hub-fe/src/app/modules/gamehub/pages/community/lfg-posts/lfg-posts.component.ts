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
    DatePipe
  ],
  templateUrl: './lfg-posts.component.html',
  styleUrl: './lfg-posts.component.scss',
})
export class LfgPostsComponent implements OnInit {

  public page = 0;
  public size = 10;

  allLfgResponse: PageResponseLfgResponse = {};


  activeTab = 'All'
  openLfgFilter = false;
  searchQuery: string = '';


  constructor(
    private lfgService: LfgControllerService,
    private router: Router,
    private route: ActivatedRoute,
  ) {
  }

  ngOnInit() {
    this.getAllLfgPosts();
  }

  resetFilters() {

  }

  navigateToNewPost() {
    this.router.navigate(['/gamehub/find-players/new-lfg-post']);
  }

  getAllLfgPosts() {
    this.lfgService.getAllActiveLfgs({
      page: this.page,
      size: this.size,
    }).subscribe({
      next: response => {
        this.allLfgResponse = response;
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
        page: page + 1
      },
      queryParamsHandling: 'merge'
    });
  }

  getProfilePicture(user: UserCommunityResponse) {
    if (user.userProfilePicture) {
      return 'data:image/jpeg;base64,' + user.userProfilePicture;
    }
    return user.userProfilePicture;
  }
}
