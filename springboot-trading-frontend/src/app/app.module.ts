import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';


import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {MarketDataComponent} from './header/market-data/market-data.component';
import {DropdownDirective} from "./header/shared/dropdown.directive";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {AppRoutingModule} from "./app-routing.module";
import {LoginInterceptorService} from "./header/login/login-interceptor.service";
import {LoginComponent} from "./header/login/login.component";
import {HistoricalDataComponent} from "./header/historical-data/histoical-data.component";


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    DropdownDirective
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterLink,
    RouterLinkActive,
    AppRoutingModule,
    LoginComponent,
    MarketDataComponent,
    HistoricalDataComponent
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: LoginInterceptorService, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
