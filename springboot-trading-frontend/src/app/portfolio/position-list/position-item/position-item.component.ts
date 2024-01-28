import {Component, Input, OnInit} from '@angular/core';
import {CurrencyPipe, DecimalPipe, NgClass, NgIf} from "@angular/common";
import {NotAvailablePipe} from "../../../shared/not-available.pipe";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Position} from "../../../model/position.model";
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";
import {ProfitLossWebsocketService} from "../../service/profit-loss-websocket.service";
import {Subscription} from "rxjs";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {MarketDataOpenCloseService} from "../../../market-data/service/market-data-open-close.service";
import {StandardMarketDataWebsocketService} from "../../../market-data/service/standard-market-data-websocket.service";
import {StandardTicker} from "../../../model/market-data/standard-ticker.model";
import {MarketDataService} from "../../../shared/market-data/market-data.service";

@Component({
  standalone: true,
  selector: 'app-position-item',
  templateUrl: './position-item.component.html',
  imports: [
    DecimalPipe,
    NotAvailablePipe,
    CurrencyPipe,
    NgIf,
    NgClass,
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

  constructor(private profitLossWebsocketService: ProfitLossWebsocketService,
              private marketDataService: MarketDataService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.profitAndLossSub = this.profitLossWebsocketService.getForPosition(this.position.contractData.contractId).subscribe((pnl) => {
      if (pnl) {
        this.profitAndLoss = pnl;
        this.isDailyProfit = pnl.dailyPnL > 0;
      }
    })
  }

  isPnLready() {
    return !!this.profitAndLoss;
  }

  onClick() {
    this.marketDataService.openMarketDataIfNew(this.position.contractData);
    this.router.navigate([this.position.contractData.id], {relativeTo: this.route});
  }
}
