import {Component, Input, OnInit} from '@angular/core';
import {CurrencyPipe, DecimalPipe, NgClass, NgIf} from "@angular/common";
import {NotAvailablePipe} from "../../../shared/not-available.pipe";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Position} from "../../../model/position.model";
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";
import {ProfitLossService} from "../../service/profit-loss.service";
import {BehaviorSubject, Subscription} from "rxjs";

@Component({
  standalone: true,
  selector: 'app-position-item',
  templateUrl: './position-item.component.html',
  imports: [
    DecimalPipe,
    NotAvailablePipe,
    CurrencyPipe,
    NgIf,
    NgClass
  ],
  styleUrl: './position-item.component.css',
  animations: [
    trigger('position',
      [
        state('exists', style({})),
        transition('void => *', [
            style(
              {
                transform: 'translateX(300px)'
              }
            ), animate(300)
          ]
        )
      ]
    )
  ]
})

export class PositionItemComponent implements OnInit {
  @Input() position: Position;
  state = 'exists';
  profitAndLoss: ProfitAndLoss;
  profitAndLossSub: Subscription;
  isDailyProfit = false

  constructor(private profitLossService: ProfitLossService) {
  }

  ngOnInit() {
    this.profitAndLossSub = this.profitLossService.getForPosition(this.position.contractData.contractId).subscribe((pnl) => {
      if (pnl) {
        this.profitAndLoss = pnl;
        this.isDailyProfit = pnl.dailyPnL > 0;
      }
    })
  }

  isPnLready() {
    return !!this.profitAndLoss;
  }
}
