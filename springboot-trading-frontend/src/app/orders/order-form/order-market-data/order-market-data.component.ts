import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {NgIf} from "@angular/common";
import {
  StandardMarketDataItemComponent
} from "../../../market-data/standard-market-data-item/standard-market-data-item.component";
import {StandardTicker} from "../../../model/market-data/standard-ticker.model";
import {ActivatedRoute, Params} from "@angular/router";
import {StandardMarketDataWebsocketService} from "../../../market-data/service/standard-market-data-websocket.service";
import {Contract} from "../../../model/contract.model";
import {MarketDataService} from "../../../shared/market-data/market-data.service";

@Component({
  selector: 'app-order-market-data',
  standalone: true,
  imports: [
    NgIf,
    StandardMarketDataItemComponent
  ],
  templateUrl: './order-market-data.component.html',
  styleUrl: './order-market-data.component.css'
})
export class OrderMarketDataComponent implements OnInit,OnChanges{
  @Input() contract: Contract;
  standardTicker: StandardTicker;

  constructor(private standardMarketDataWebsocketService: StandardMarketDataWebsocketService,
              private marketDataService: MarketDataService) {
  }
  ngOnInit() {
    this.setValues();
  }
  ngOnChanges(){
    this.setValues();
  }

  private setValues() {
    this.marketDataService.openMarketDataIfNew(this.contract);
    this.standardMarketDataWebsocketService.getForContract(this.contract.id)
      .subscribe(ticker => {
        this.standardTicker = ticker;
      })
  }
}
