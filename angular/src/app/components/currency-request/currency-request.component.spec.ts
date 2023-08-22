import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CurrencyRequestComponent } from './currency-request.component';

describe('CurrencyRequestComponent', () => {
  let component: CurrencyRequestComponent;
  let fixture: ComponentFixture<CurrencyRequestComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CurrencyRequestComponent]
    });
    fixture = TestBed.createComponent(CurrencyRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
