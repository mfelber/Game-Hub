import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminGameDetailsComponent } from './admin-game-details.component';

describe('AdminGameDetailsComponent', () => {
  let component: AdminGameDetailsComponent;
  let fixture: ComponentFixture<AdminGameDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminGameDetailsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminGameDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
