import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LfgPostsComponent } from './lfg-posts.component';

describe('LfgPostsComponent', () => {
  let component: LfgPostsComponent;
  let fixture: ComponentFixture<LfgPostsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LfgPostsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LfgPostsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
