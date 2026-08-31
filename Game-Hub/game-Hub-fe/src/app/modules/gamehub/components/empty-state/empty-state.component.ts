import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-empty-state',
  imports: [],
  templateUrl: './empty-state.component.html',
  styleUrl: './empty-state.component.scss',
})
export class EmptyStateComponent {

  @Input() icon = 'fa-solid fa-circle-info'
  @Input() title = 'Nothing found'
  @Input() message = ''

}
