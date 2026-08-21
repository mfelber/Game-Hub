import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviewReportModalComponent } from './preview-report-modal.component';

describe('PreviewReportModalComponent', () => {
  let component: PreviewReportModalComponent;
  let fixture: ComponentFixture<PreviewReportModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PreviewReportModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PreviewReportModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
