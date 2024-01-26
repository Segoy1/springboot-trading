import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PositionMarketDataComponent } from './position-market-data.component';

describe('PositionMarketDataComponent', () => {
  let component: PositionMarketDataComponent;
  let fixture: ComponentFixture<PositionMarketDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PositionMarketDataComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PositionMarketDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
