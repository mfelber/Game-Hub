import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviewSuspendedUserModalComponent } from './preview-suspended-user-modal.component';

describe('PreviewSuspendedUserModalComponent', () => {
  let component: PreviewSuspendedUserModalComponent;
  let fixture: ComponentFixture<PreviewSuspendedUserModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PreviewSuspendedUserModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PreviewSuspendedUserModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
