import {Component, Input} from '@angular/core';
import {Portfolio} from "../../../model/portfolio.model";
import {CurrencyPipe, DecimalPipe} from "@angular/common";
import {NotAvailablePipe} from "../../../shared/not-available.pipe";
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
  standalone: true,
  selector: 'app-position-item',
  templateUrl: './position-item.component.html',
  imports: [
    DecimalPipe,
    NotAvailablePipe,
    CurrencyPipe
  ],
  styleUrl: './position-item.component.css',
  animations: [
    trigger('position',
      [
        state('exists', style({
      })),
        transition('void => *',[
          style(
            {
              transform: 'translateX(300px)'
            }
          ),animate(300)
        ]
        )
      ]
    )
  ]
})

export class PositionItemComponent {
  @Input() portfolio: Portfolio;
  state='exists';

}
