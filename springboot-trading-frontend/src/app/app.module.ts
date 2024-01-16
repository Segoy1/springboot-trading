import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';


import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {MarketDataComponent} from './market-data/market-data.component';
import {DropdownDirective} from "./shared/dropdown.directive";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {AppRoutingModule} from "./app-routing.module";
import {LoginInterceptorService} from "./login/login-interceptor.service";
import {LoginComponent} from "./login/login.component";
import {HistoricalDataComponent} from "./historical-data/histoical-data.component";


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterLink,
    RouterLinkActive,
    AppRoutingModule,
    LoginComponent,
    MarketDataComponent,
    HistoricalDataComponent,
    DropdownDirective
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: LoginInterceptorService, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
