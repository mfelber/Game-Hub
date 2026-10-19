import { TestBed } from '@angular/core/testing';

import { TimeAgoPipe } from './time-ago-pipe';

describe('TimeAgoPipe', () => {
  let service: TimeAgoPipe;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TimeAgoPipe);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
