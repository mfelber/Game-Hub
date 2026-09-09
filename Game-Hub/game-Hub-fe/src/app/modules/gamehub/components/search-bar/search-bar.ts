import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-search-bar',
  imports: [],
  templateUrl: './search-bar.html',
  styleUrl: './search-bar.css',
})
export class SearchBar {

  @Input() value = '';
  @Input() placeholder = 'Search...';
  @Output() search = new EventEmitter<string>();

  onSearch(value: string) {
    this.value = value;
    this.search.emit(value);
  }

}
