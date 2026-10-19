import {Injectable, Pipe, PipeTransform} from '@angular/core';
import * as trace_events from 'node:trace_events';
import {DatePipe} from '@angular/common';

@Injectable({
  providedIn: 'root',
})

@Pipe({name: 'timeAgo', standalone: true})
export class TimeAgoPipe implements PipeTransform {
  private readonly datePipe = new DatePipe('en-US');

  transform(value: string | Date | null | undefined): any {
    if (!value) {
      return '';
    }

    const created = new Date(value);
    const now = new Date();

    const minutes = Math.floor((now.getTime() - created.getTime()) / 60_000);
    const hours = Math.floor(minutes / 60);

    if (minutes < 1) {
      return 'just now';
    }

    if (minutes < 60) {
      return `${minutes} min ago`;
    }

    if (hours < 24) {
      return `${hours} hour ago`;
    }

    const startOfDay = (d: Date) => new Date(d.getFullYear(), d.getMonth(), d.getDate()).getDate();
    const dayDiff = Math.round((startOfDay(now) - startOfDay(created)) / 86_400_000);

    if (dayDiff === 1) {
      return 'Yesterday';
    }
    return this.datePipe.transform(created, 'dd.MM.yyyy') ?? '';
  }
}
