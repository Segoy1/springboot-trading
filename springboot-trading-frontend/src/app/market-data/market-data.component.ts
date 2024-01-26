import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {OptionMarketData} from "../model/market-data/option-market-data.model";
import {OptionMarketDataWebsocketService} from "./service/option-market-data-websocket.service";
import {StandardMarketDataItemComponent} from "./standard-market-data-item/standard-market-data-item.component";
import {OptionMarketDataItemComponent} from "./option-market-data-item/option-market-data-item.component";
import {NgForOf} from "@angular/common";
import {StandardTicker} from "../model/market-data/standard-ticker.model";
import {StandardMarketDataWebsocketService} from "./service/standard-market-data-websocket.service";
import {OptionTicker} from "../model/market-data/option-ticker.model";

@Component({
  standalone: true,
  selector: 'app-market-data',
  templateUrl: './market-data.component.html',
  imports: [
    StandardMarketDataItemComponent,
    OptionMarketDataItemComponent,
    NgForOf
  ],
  styleUrl: './market-data.component.css'
})
export class MarketDataComponent implements OnInit, OnDestroy {
  standardMarketData: StandardTicker[];
  standardSub: Subscription;



  constructor(private standardMarketDataWebsocketService: StandardMarketDataWebsocketService) {
  }

  ngOnInit() {
    this.standardSub = this.standardMarketDataWebsocketService.responseChangedSubject.subscribe(ticker => {
      this.standardMarketData = ticker;
    });

  }

  ngOnDestroy() {
    this.standardSub.unsubscribe();
  }
}
