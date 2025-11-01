import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserPrivateProfileComponent } from './user-private-profile.component';

describe('UserProfileComponent', () => {
  let component: UserPrivateProfileComponent;
  let fixture: ComponentFixture<UserPrivateProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserPrivateProfileComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserPrivateProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
