import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {AsyncPipe, CurrencyPipe, NgForOf, NgIf} from "@angular/common";
import {MarketDataOpenCloseService} from "../service/market-data-open-close.service";
import {Contract} from "../../model/contract.model";
import {RouterLinkActive} from "@angular/router";
import {StandardTicker} from "../../model/market-data/standard-ticker.model";
import {MarketDataFieldNamePipe} from "../../shared/market-data-field-name.pipe";
import {OptionMarketDataItemComponent} from "../option-market-data-item/option-market-data-item.component";
import {OptionTicker} from "../../model/market-data/option-ticker.model";
import {Subscription} from "rxjs";
import {OptionMarketDataWebsocketService} from "../service/option-market-data-websocket.service";
import {ContractDataRestService} from "../service/contract-data-rest.service";
import {ComboLegDataDisplayComponent} from "../../shared/combo-leg-data-display/combo-leg-data-display.component";

@Component({
  selector: 'app-standard-market-data-item',
  standalone: true,
  imports: [
    CurrencyPipe,
    RouterLinkActive,
    NgForOf,
    MarketDataFieldNamePipe,
    OptionMarketDataItemComponent,
    NgIf,
    AsyncPipe,
    ComboLegDataDisplayComponent
  ],
  templateUrl: './standard-market-data-item.component.html',
  styleUrl: './standard-market-data-item.component.css'
})
export class StandardMarketDataItemComponent implements OnInit, OnDestroy, OnChanges {
  @Input() ticker: StandardTicker;
  contract: Contract;
  contractSub: Subscription;
  optionTicker: OptionTicker;
  optionSub: Subscription;

  constructor(private marketDataOpenCloseService: MarketDataOpenCloseService,
              private optionMarketDataWebsocketService: OptionMarketDataWebsocketService,
              private contractDataRestService:ContractDataRestService) {
  }

  ngOnInit() {
    this.setUp();
  }

  ngOnChanges(changes: SimpleChanges) {
   this.setUp();
  }

  onCancelMarketData() {
    this.marketDataOpenCloseService.stopMarketData(this.ticker.tickerId);
  }

  ngOnDestroy() {
    this.optionSub.unsubscribe();
  }
  private setUp(){

    this.contractSub = this.contractDataRestService.getForId(this.ticker.tickerId).subscribe(contract=>
    this.contract = contract);

    this.contractDataRestService.requestContractData(this.ticker.tickerId);


    this.optionSub = this.optionMarketDataWebsocketService.getForContract(this.ticker.tickerId).subscribe(ticker => {
      this.optionTicker = ticker;
    });
  }
}
