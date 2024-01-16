import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';


import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {MarketDataComponent} from './header/market-data/market-data.component';
import {DropdownDirective} from "./header/shared/dropdown.directive";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HistoicalDataComponent} from './header/histoical-data/histoical-data.component';
import {RouterLink, RouterLinkActive} from "@angular/router";
import {AppRoutingModule} from "./app-routing.module";
import {LoadingSpinnerComponent} from './header/shared/loading-spinner/loading-spinner.component';
import {LoginInterceptorService} from "./header/login/login-interceptor.service";


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    MarketDataComponent,
    DropdownDirective,
    HistoicalDataComponent,
    LoadingSpinnerComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterLink,
    RouterLinkActive,
    AppRoutingModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: LoginInterceptorService, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
