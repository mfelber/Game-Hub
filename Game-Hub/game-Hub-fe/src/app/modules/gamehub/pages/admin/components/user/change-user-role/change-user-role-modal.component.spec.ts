import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeUserRoleModalComponent } from './change-user-role-modal.component';

describe('ChangeUserRoleModalComponent', () => {
  let component: ChangeUserRoleModalComponent;
  let fixture: ComponentFixture<ChangeUserRoleModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChangeUserRoleModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangeUserRoleModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
