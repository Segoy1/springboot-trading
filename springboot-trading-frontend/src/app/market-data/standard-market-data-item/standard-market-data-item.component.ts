import {Component, Input, OnInit} from '@angular/core';
import {StandardMarketData} from "../../model/standard-market-data.model";
import {CurrencyPipe} from "@angular/common";
import {MarketDataOpenCloseService} from "../service/market-data-open-close.service";
import {Contract} from "../../model/contract.model";

@Component({
  selector: 'app-standard-market-data-item',
  standalone: true,
  imports: [
    CurrencyPipe
  ],
  templateUrl: './standard-market-data-item.component.html',
  styleUrl: './standard-market-data-item.component.css'
})
export class StandardMarketDataItemComponent implements OnInit{
  @Input() ticker: StandardMarketData;
  contract: Contract;

  constructor(private marketDataOpenCloseService: MarketDataOpenCloseService) {
  }

  ngOnInit() {
    this.contract = this.marketDataOpenCloseService.getContractById(this.ticker.tickerId);
  }
}
