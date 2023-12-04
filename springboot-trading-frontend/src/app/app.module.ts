import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { OrdersComponent } from './header/orders/orders.component';
import { PortfolioComponent } from './header/portfolio/portfolio.component';
import { MarketDataComponent } from './header/market-data/market-data.component';
import {DropdownDirective} from "./header/shared/dropdown.directive";

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
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
