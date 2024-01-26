import {Component, OnInit} from '@angular/core';
import {StandardTicker} from "../../model/standard-ticker.model";
import {ActivatedRoute, Params} from "@angular/router";
import {StandardMarketDataWebsocketService} from "../../market-data/service/standard-market-data-websocket.service";
import {
  StandardMarketDataItemComponent
} from "../../market-data/standard-market-data-item/standard-market-data-item.component";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-position-market-data',
  standalone: true,
  imports: [
    StandardMarketDataItemComponent,
    NgIf
  ],
  templateUrl: './position-market-data.component.html',
  styleUrl: './position-market-data.component.css'
})
export class PositionMarketDataComponent implements OnInit {

  standardTicker: StandardTicker;

  constructor(private route: ActivatedRoute,
              private standardMarketDataWebsocketService: StandardMarketDataWebsocketService) {
  }

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.standardMarketDataWebsocketService.getForContract(+params['id'])
        .subscribe(ticker =>{
          this.standardTicker = ticker
        })
    }
    );
  }
}
