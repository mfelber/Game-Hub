import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnbanModalComponent } from './unban-modal.component';

describe('UnbanModalComponent', () => {
  let component: UnbanModalComponent;
  let fixture: ComponentFixture<UnbanModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UnbanModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UnbanModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
