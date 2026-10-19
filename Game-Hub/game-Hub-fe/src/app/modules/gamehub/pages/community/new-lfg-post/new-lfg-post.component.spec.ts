import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewLfgPostComponent } from './new-lfg-post.component';

describe('NewLfgPostComponent', () => {
  let component: NewLfgPostComponent;
  let fixture: ComponentFixture<NewLfgPostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewLfgPostComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewLfgPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
