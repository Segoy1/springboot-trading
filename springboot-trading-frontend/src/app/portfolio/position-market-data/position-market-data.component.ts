import {Component, OnInit} from '@angular/core';
import {StandardTicker} from "../../model/market-data/standard-ticker.model";
import {ActivatedRoute, Params} from "@angular/router";
import {StandardMarketDataWebsocketService} from "../../market-data/service/standard-market-data-websocket.service";
import {
  StandardMarketDataItemComponent
} from "../../market-data/standard-market-data-item/standard-market-data-item.component";
import {NgIf} from "@angular/common";
import {OptionTicker} from "../../model/market-data/option-ticker.model";
import {OptionMarketDataWebsocketService} from "../../market-data/service/option-market-data-websocket.service";
import {
  OptionMarketDataItemComponent
} from "../../market-data/option-market-data-item/option-market-data-item.component";

@Component({
  selector: 'app-position-market-data',
  standalone: true,
  imports: [
    StandardMarketDataItemComponent,
    NgIf,
    OptionMarketDataItemComponent
  ],
  templateUrl: './position-market-data.component.html',
  styleUrl: './position-market-data.component.css'
})
export class PositionMarketDataComponent implements OnInit {

  standardTicker: StandardTicker;
  optionTicker: OptionTicker;

  constructor(private route: ActivatedRoute,
              private standardMarketDataWebsocketService: StandardMarketDataWebsocketService,
              private optionMarketDataWebsocketService:OptionMarketDataWebsocketService) {
  }

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.standardMarketDataWebsocketService.getForContract(+params['id'])
        .subscribe(ticker =>{
          this.standardTicker = ticker;
        })
    }
    );
    this.route.params.subscribe((params: Params) => {
        this.optionMarketDataWebsocketService.getForContract(+params['id'])
          .subscribe(ticker =>{
            this.optionTicker = ticker;
          })
      }
    );
  }
}
