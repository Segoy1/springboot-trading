import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';


import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { OrdersComponent } from './header/orders/orders.component';
import { PortfolioComponent } from './header/portfolio/portfolio.component';
import { MarketDataComponent } from './header/market-data/market-data.component';
import {DropdownDirective} from "./header/shared/dropdown.directive";
import {HttpClientModule} from "@angular/common/http";
import { HistoicalDataComponent } from './header/histoical-data/histoical-data.component';
import { AccountComponent } from './header/account/account.component';
import { LoginComponent } from './header/login/login.component';
import {FormsModule} from "@angular/forms";
import {RouterModule, Routes} from "@angular/router";
import {LoginService} from "./header/shared/login.service";
import {CommonModule} from "@angular/common";
import { PositionItemComponent } from './header/portfolio/position-item/position-item.component';
import {PortfolioService} from "./header/portfolio/portfolio.service";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home'},
  { path: 'home', component: HeaderComponent},
  { path: 'login', component: LoginComponent}
];

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
    PositionItemComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [LoginService, PortfolioService],
  bootstrap: [AppComponent]
})
export class AppModule { }
