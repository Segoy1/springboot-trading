import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {StandardMarketData} from "../model/standard-market-data.model";
import {StandardMarketDataWebsocketService} from "./service/standard-market-data-websocket.service";
import {OptionMarketData} from "../model/option-market-data.model";
import {OptionMarketDataWebsocketService} from "./service/option-market-data-websocket.service";
import {StandardMarketDataItemComponent} from "./standard-market-data-item/standard-market-data-item.component";
import {OptionMarketDataItemComponent} from "./option-market-data-item/option-market-data-item.component";
import {NgForOf} from "@angular/common";

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
  standardMarketData: StandardMarketData[];
  standardSub: Subscription;
  optionMarketData: OptionMarketData[];
  optionSub: Subscription;


  constructor(private standardMarketDataWebsocketService: StandardMarketDataWebsocketService,
              private optionMarketDataWebsocketService: OptionMarketDataWebsocketService) {
  }

  ngOnInit() {
    this.standardSub = this.standardMarketDataWebsocketService.responseChangedSubject.subscribe(ticker => {
      this.standardMarketData = ticker;
    });
    this.optionSub = this.optionMarketDataWebsocketService.responseChangedSubject.subscribe(ticker=>{
      this.optionMarketData = ticker;
    });
  }

  ngOnDestroy() {
    this.optionSub.unsubscribe();
    this.standardSub.unsubscribe();
  }

}
