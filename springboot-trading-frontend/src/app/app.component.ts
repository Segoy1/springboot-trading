import {Component, OnInit} from '@angular/core';
import {LoginService} from "./login/login.service";
import {HeaderComponent} from "./header/header.component";
import {RouterModule} from "@angular/router";
import {OpenOrdersWebsocketService} from "./orders/service/open-orders-websocket.service";
import {PositionsOpenCloseService} from "./portfolio/service/positions-open-close.service";
import {AccountMarginWebsocketService} from "./portfolio/account/account-margin/account-margin-websocket.service";
import {PositionsWebsocketService} from "./portfolio/service/positions-websocket.service";
import {AccountPnlWebsocketService} from "./portfolio/account/account-pnl/account-pnl-websocket.service";

@Component({
  standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports:[HeaderComponent, RouterModule]
})
export class AppComponent implements OnInit{

  constructor(private loginService: LoginService,
              private openOrderWebsocketService: OpenOrdersWebsocketService,
              private positionsWebsocketService: PositionsWebsocketService,
              private accountMarginWebsocketService:AccountMarginWebsocketService,
              private accountPnlWebsocketService:AccountPnlWebsocketService) {
  }

  ngOnInit() {
    this.loginService.autoLogin();
  }

}
