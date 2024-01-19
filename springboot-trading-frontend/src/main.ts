import {bootstrapApplication} from "@angular/platform-browser";
import {AppComponent} from "./app/app.component";
import {importProvidersFrom} from "@angular/core";
import {AppRoutingModule} from "./app/app-routing.module";
import {AppHttpModule} from "./app/app-http.module";
import { provideStore } from '@ngrx/store';
import {ordersReducer} from "./app/store/orders/orders.reducer";
import { provideEffects } from '@ngrx/effects';
import {ordersStrategyModeReducer} from "./app/store/orders/modes/strategy/orders-strategy-mode.reducer";


bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(AppRoutingModule, AppHttpModule),
    provideStore({ orders: ordersReducer, strategyMode: ordersStrategyModeReducer }),
    provideEffects()
]

})
// platformBrowserDynamic().bootstrapModule(AppModule)
//   .catch(err => console.error(err));
