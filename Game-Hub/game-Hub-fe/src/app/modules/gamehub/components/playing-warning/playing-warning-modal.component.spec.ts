import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayingWarningModalComponent } from './playing-warning-modal.component';

describe('PlayingWarningModalComponent', () => {
  let component: PlayingWarningModalComponent;
  let fixture: ComponentFixture<PlayingWarningModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayingWarningModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlayingWarningModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
