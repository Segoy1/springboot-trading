import {Component, Input, OnInit} from '@angular/core';
import {CurrencyPipe, NgForOf} from "@angular/common";
import {MarketDataOpenCloseService} from "../service/market-data-open-close.service";
import {Contract} from "../../model/contract.model";
import {RouterLinkActive} from "@angular/router";
import {StandardTicker} from "../../model/standard-ticker.model";

@Component({
  selector: 'app-standard-market-data-item',
  standalone: true,
  imports: [
    CurrencyPipe,
    RouterLinkActive,
    NgForOf
  ],
  templateUrl: './standard-market-data-item.component.html',
  styleUrl: './standard-market-data-item.component.css'
})
export class StandardMarketDataItemComponent implements OnInit{
  @Input() ticker: StandardTicker;
  contract: Contract;

  constructor(private marketDataOpenCloseService: MarketDataOpenCloseService) {
  }

  ngOnInit() {
    this.contract = this.marketDataOpenCloseService.getContractById(this.ticker.tickerId);
  }
  onCancelMarketData(){
    this.marketDataOpenCloseService.stopMarketData(this.ticker.tickerId);
  }
}
