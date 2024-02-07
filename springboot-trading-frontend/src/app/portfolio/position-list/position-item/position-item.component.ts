import {Component, Input, OnInit} from '@angular/core';
import {CurrencyPipe, DecimalPipe, NgClass, NgIf} from "@angular/common";
import {NotAvailablePipe} from "../../../shared/not-available.pipe";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Position} from "../../../model/position.model";
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";
import {ProfitLossWebsocketService} from "../../service/profit-loss-websocket.service";
import {Subscription} from "rxjs";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {MarketDataService} from "../../../shared/market-data/market-data.service";
import {ComboPositionItemComponent} from "./combo-position-item/combo-position-item.component";

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
    ComboPositionItemComponent,
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
  state = 'exists';
  @Input() position: Position;
  profitAndLoss: ProfitAndLoss[] = [];
  profitAndLossSub: Subscription;
  isDailyProfit = false;
  daily:number;
  unrealized:number;
  marketValue:number;

  constructor(private profitLossWebsocketService: ProfitLossWebsocketService,
              private marketDataService: MarketDataService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    const contractIds: number[] = [];

    this.position.contractData.comboLegs.forEach(leg =>
      contractIds.push(leg.contractId));
    if (contractIds.length === 0) {
      contractIds.push(this.position.contractData.contractId);
    }
    this.profitAndLossSub = this.profitLossWebsocketService.getForPosition(contractIds).subscribe((pnl) => {

    let daily = 0;
    let unrealized = 0;
    let mkt = 0;
      pnl.forEach(singlePnl => {
        const index = this.profitAndLoss.findIndex((item) => singlePnl.id === item.id)
        if (index > -1) {
          this.profitAndLoss[index] = singlePnl;
        } else {
          this.profitAndLoss.push(singlePnl);
        }
        daily+=singlePnl.dailyPnL;
        unrealized+=singlePnl.unrealizedPnL;
        mkt+=singlePnl.currentValue;
      });
        this.daily = daily;
        this.unrealized = unrealized;
        this.marketValue = mkt;
        this.isDailyProfit = daily>0;
    });

  }

  isPnLready() {
    return this.profitAndLoss.length>0;
  }

  onClick() {
    this.marketDataService.openMarketDataIfNew(this.position.contractData);
    this.router.navigate([this.position.contractData.id], {relativeTo: this.route});
  }
}
