import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardMarketDataItemComponent } from './standard-market-data-item.component';

describe('StandardMarketDataItemComponent', () => {
  let component: StandardMarketDataItemComponent;
  let fixture: ComponentFixture<StandardMarketDataItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StandardMarketDataItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StandardMarketDataItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
