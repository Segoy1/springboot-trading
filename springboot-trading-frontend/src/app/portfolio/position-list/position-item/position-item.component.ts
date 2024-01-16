import {Component, Input} from '@angular/core';
import {Portfolio} from "../../../model/portfolio.model";
import {CurrencyPipe, DecimalPipe} from "@angular/common";
import {NotAvailablePipe} from "../../../shared/not-available.pipe";

@Component({
  standalone: true,
  selector: 'app-position-item',
  templateUrl: './position-item.component.html',
  imports: [
    DecimalPipe,
    NotAvailablePipe,
    CurrencyPipe
  ],
  styleUrl: './position-item.component.css'
})
export class PositionItemComponent{
  @Input() portfolio: Portfolio;

}
