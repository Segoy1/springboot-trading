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
import {Subscription} from "rxjs";
import {ErrorMessage} from "./model/error-message.model";
import {ErrorMessageWebsocketService} from "./shared/error-message/error-message-websocket.service";
import {ErrorMessageComponent} from "./shared/error-message/error-message.component";
import {NgForOf} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [HeaderComponent, RouterModule, ErrorMessageComponent, NgForOf]
})
export class AppComponent implements OnInit {
  errorMessages: ErrorMessage[] = [];
  errorMessageSub: Subscription;

  constructor(private loginService: LoginService,
              private openOrderWebsocketService: OpenOrdersWebsocketService,
              private positionsWebsocketService: PositionsWebsocketService,
              private accountMarginWebsocketService: AccountMarginWebsocketService,
              private accountPnlWebsocketService: AccountPnlWebsocketService,
              private optionMarketDataWebsocketService: OptionMarketDataWebsocketService,
              private standardMarketDataWebsocketService: StandardMarketDataWebsocketService,
              private errorMessageWebsocketService: ErrorMessageWebsocketService
  ) {
  }

  ngOnInit() {
    this.loginService.autoLogin();
    this.errorMessageSub = this.errorMessageWebsocketService.responseChangedSubject
      .subscribe(
        errors => this.errorMessages = errors
      );
  }
}
