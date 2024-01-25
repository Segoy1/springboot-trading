import {Component, OnInit} from '@angular/core';
import {LoginService} from "./login/login.service";
import {HeaderComponent} from "./header/header.component";
import {RouterModule} from "@angular/router";
import {OpenOrdersWebsocketService} from "./orders/service/open-orders-websocket.service";
import {AccountMarginWebsocketService} from "./portfolio/account/account-margin/account-margin-websocket.service";
import {PositionsWebsocketService} from "./portfolio/service/positions-websocket.service";
import {AccountPnlWebsocketService} from "./portfolio/account/account-pnl/account-pnl-websocket.service";
import {OptionMarketDataWebsocketService} from "./market-data/service/option-market-data-websocket.service";
import {StandardMarketDataWebsocketService} from "./market-data/service/standard-market-data-websocket.service";
import {MarketDataOpenCloseService} from "./market-data/service/market-data-open-close.service";

@Component({
  standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports:[HeaderComponent, RouterModule]
})
export class AppComponent implements OnInit{

  constructor(private loginService: LoginService,
              private marketDataOpenCloseService:MarketDataOpenCloseService,
              private openOrderWebsocketService: OpenOrdersWebsocketService,
              private positionsWebsocketService: PositionsWebsocketService,
              private accountMarginWebsocketService:AccountMarginWebsocketService,
              private accountPnlWebsocketService:AccountPnlWebsocketService,
              private optionMarketDataWebsocketService:OptionMarketDataWebsocketService,
              private standardMarketDataWebsocketService: StandardMarketDataWebsocketService) {
  }

  ngOnInit() {
    this.loginService.autoLogin();
    this.marketDataOpenCloseService.initContracts();
  }

}
