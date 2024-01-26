import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {MarketDataOpenCloseService} from "../service/market-data-open-close.service";
import {Contract} from "../../model/contract.model";
import {RouterLinkActive} from "@angular/router";
import {StandardTicker} from "../../model/market-data/standard-ticker.model";
import {MarketDataFieldNamePipe} from "../../shared/market-data-field-name.pipe";
import {OptionMarketDataItemComponent} from "../option-market-data-item/option-market-data-item.component";
import {OptionTicker} from "../../model/market-data/option-ticker.model";
import {Subscription} from "rxjs";
import {OptionMarketDataWebsocketService} from "../service/option-market-data-websocket.service";

@Component({
  selector: 'app-standard-market-data-item',
  standalone: true,
  imports: [
    CurrencyPipe,
    RouterLinkActive,
    NgForOf,
    MarketDataFieldNamePipe,
    OptionMarketDataItemComponent,
    NgIf
  ],
  templateUrl: './standard-market-data-item.component.html',
  styleUrl: './standard-market-data-item.component.css'
})
export class StandardMarketDataItemComponent implements OnInit, OnDestroy{
  @Input() ticker: StandardTicker;
  contract: Contract;
  optionTicker: OptionTicker;
  optionSub: Subscription;

  constructor(private marketDataOpenCloseService: MarketDataOpenCloseService,
              private optionMarketDataWebsocketService: OptionMarketDataWebsocketService) {
  }

  ngOnInit() {
    this.contract = this.marketDataOpenCloseService.getContractById(this.ticker.tickerId);

    this.optionSub = this.optionMarketDataWebsocketService.getForContract(this.contract.id).subscribe(ticker => {
      this.optionTicker = ticker;
    });
  }
  onCancelMarketData(){
    this.marketDataOpenCloseService.stopMarketData(this.ticker.tickerId);
  }
  ngOnDestroy() {
    this.optionSub.unsubscribe();
  }
}
