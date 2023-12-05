import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';


import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { OrdersComponent } from './header/orders/orders.component';
import { PortfolioComponent } from './header/portfolio/portfolio.component';
import { MarketDataComponent } from './header/market-data/market-data.component';
import {DropdownDirective} from "./header/shared/dropdown.directive";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    OrdersComponent,
    PortfolioComponent,
    MarketDataComponent,
    DropdownDirective
  ],
  imports: [
    BrowserModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
