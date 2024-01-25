import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketDataStartComponent } from './market-data-start.component';

describe('MarketDataStartComponent', () => {
  let component: MarketDataStartComponent;
  let fixture: ComponentFixture<MarketDataStartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MarketDataStartComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MarketDataStartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
