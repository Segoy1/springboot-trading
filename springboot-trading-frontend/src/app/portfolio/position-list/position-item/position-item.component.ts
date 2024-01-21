import {Component, Input, OnInit} from '@angular/core';
import {Portfolio} from "../../../model/portfolio.model";
import {CurrencyPipe, DecimalPipe, NgIf} from "@angular/common";
import {NotAvailablePipe} from "../../../shared/not-available.pipe";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Position} from "../../../model/position.model";
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";

@Component({
  standalone: true,
  selector: 'app-position-item',
  templateUrl: './position-item.component.html',
  imports: [
    DecimalPipe,
    NotAvailablePipe,
    CurrencyPipe,
    NgIf
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

export class PositionItemComponent implements OnInit{
  @Input() position: Position;
  state='exists';
  profitAndLoss: ProfitAndLoss;

  ngOnInit(){
    this.profitAndLoss= null;
  }

}
