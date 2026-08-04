import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-search-bar',
  imports: [],
  templateUrl: './search-bar.html',
  styleUrl: './search-bar.css',
})
export class SearchBar {

  @Input() placeholder = 'Search...';
  @Output() search = new EventEmitter<string>();
  onSearch(value: string) {
    console.log(value);
    this.search.emit(value);
  }

}
