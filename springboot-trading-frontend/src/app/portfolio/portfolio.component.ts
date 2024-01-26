import {Component} from '@angular/core';
import {AccountComponent} from "./account/account.component";
import {PositionListComponent} from "./position-list/position-list.component";
import {ActivatedRoute, RouterOutlet} from "@angular/router";
import {StandardMarketDataWebsocketService} from "../market-data/service/standard-market-data-websocket.service";
import {StandardTicker} from "../model/standard-ticker.model";

@Component({
  standalone: true,
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css',
  imports: [
    AccountComponent,
    PositionListComponent,
    RouterOutlet
  ]
})
export class PortfolioComponent{}
