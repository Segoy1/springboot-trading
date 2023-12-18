import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoicalDataComponent } from './histoical-data.component';

describe('HistoicalDataComponent', () => {
  let component: HistoicalDataComponent;
  let fixture: ComponentFixture<HistoicalDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HistoicalDataComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HistoicalDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
