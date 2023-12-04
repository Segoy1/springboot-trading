import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketDataComponent } from './market-data.component';

describe('MarketDataComponent', () => {
  let component: MarketDataComponent;
  let fixture: ComponentFixture<MarketDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MarketDataComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MarketDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
