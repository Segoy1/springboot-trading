import {ApplicationRef, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';


import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {OrdersComponent} from './header/orders/orders.component';
import {PortfolioComponent} from './header/portfolio/portfolio.component';
import {MarketDataComponent} from './header/market-data/market-data.component';
import {DropdownDirective} from "./header/shared/dropdown.directive";
import {HttpClientModule} from "@angular/common/http";
import {HistoicalDataComponent} from './header/histoical-data/histoical-data.component';
import {AccountComponent} from './header/account/account.component';
import {LoginComponent} from './header/login/login.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterLink, RouterLinkActive, RouterModule, Routes} from "@angular/router";
import {LoginService} from "./header/shared/login.service";
import {CommonModule} from "@angular/common";
import {PositionItemComponent} from './header/portfolio/position-item/position-item.component';
import {PortfolioService} from "./header/portfolio/portfolio.service";
import {OpenOrderItemComponent} from './header/orders/open-order-item/open-order-item.component';
import {OpenOrderService} from "./header/orders/open-order.service";
import {OrderFormComponent} from './header/orders/order-form/order-form.component';
import {AppRoutingModule} from "./app-routing.module";
import {OrderFormValidationService} from "./header/orders/order-form/order-form-validation.service";


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    OrdersComponent,
    PortfolioComponent,
    MarketDataComponent,
    DropdownDirective,
    HistoicalDataComponent,
    AccountComponent,
    LoginComponent,
    PositionItemComponent,
    OpenOrderItemComponent,
    OrderFormComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterLink,
    RouterLinkActive,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  providers: [LoginService, PortfolioService, OpenOrderService, OrderFormValidationService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
