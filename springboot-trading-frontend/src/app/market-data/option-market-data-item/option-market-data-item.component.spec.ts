import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OptionMarketDataItemComponent } from './option-market-data-item.component';

describe('OptionMarketDataItemComponent', () => {
  let component: OptionMarketDataItemComponent;
  let fixture: ComponentFixture<OptionMarketDataItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OptionMarketDataItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OptionMarketDataItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
