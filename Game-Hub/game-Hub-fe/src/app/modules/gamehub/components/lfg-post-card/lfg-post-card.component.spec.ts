import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LfgPostCardComponent } from './lfg-post-card.component';

describe('LfgPostCardComponent', () => {
  let component: LfgPostCardComponent;
  let fixture: ComponentFixture<LfgPostCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LfgPostCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LfgPostCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
