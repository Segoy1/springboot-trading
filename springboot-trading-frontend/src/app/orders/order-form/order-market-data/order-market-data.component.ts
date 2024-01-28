import {Component, Input} from '@angular/core';
import {NgIf} from "@angular/common";
import {
  StandardMarketDataItemComponent
} from "../../../market-data/standard-market-data-item/standard-market-data-item.component";
import {StandardTicker} from "../../../model/market-data/standard-ticker.model";
import {ActivatedRoute, Params} from "@angular/router";
import {StandardMarketDataWebsocketService} from "../../../market-data/service/standard-market-data-websocket.service";

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
export class OrderMarketDataComponent {
  @Input() id:number;
  standardTicker: StandardTicker;

  constructor(private standardMarketDataWebsocketService: StandardMarketDataWebsocketService) {
  }

  ngOnInit() {
        this.standardMarketDataWebsocketService.getForContract(this.id)
          .subscribe(ticker =>{
            this.standardTicker = ticker;
          })
      }
}
