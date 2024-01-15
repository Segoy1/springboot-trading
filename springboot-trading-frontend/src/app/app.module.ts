import {ApplicationRef, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';


import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {OrdersComponent} from './header/orders/orders.component';
import {PortfolioComponent} from './header/portfolio/portfolio.component';
import {MarketDataComponent} from './header/market-data/market-data.component';
import {DropdownDirective} from "./header/shared/dropdown.directive";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HistoicalDataComponent} from './header/histoical-data/histoical-data.component';
import {LoginComponent} from './header/login/login.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {CommonModule} from "@angular/common";
import {PositionItemComponent} from './header/portfolio/position-list/position-item/position-item.component';
import {OpenOrderItemComponent} from './header/orders/open-order-item/open-order-item.component';
import {OrderFormComponent} from './header/orders/order-form/order-form.component';
import {AppRoutingModule} from "./app-routing.module";
import { OrderStartComponent } from './header/orders/order-start/order-start.component';
import { ComboLegsComponent } from './header/orders/order-form/combo-legs/combo-legs.component';
import { StrategyBuilderComponent } from './header/orders/order-form/strategy-builder/strategy-builder.component';
import { LoadingSpinnerComponent } from './header/shared/loading-spinner/loading-spinner.component';
import {LoginInterceptorService} from "./header/login/login-interceptor.service";
import {AccountComponent} from "./header/portfolio/account/account.component";
import {NotAvailablePipe} from "./header/shared/not-available.pipe";
import { PositionListComponent } from './header/portfolio/position-list/position-list.component';


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
    OrderFormComponent,
    OrderStartComponent,
    ComboLegsComponent,
    StrategyBuilderComponent,
    LoadingSpinnerComponent,
    NotAvailablePipe,
    PositionListComponent
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
  providers: [ {provide: HTTP_INTERCEPTORS, useClass: LoginInterceptorService, multi:true }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
