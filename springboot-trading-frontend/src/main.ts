import {bootstrapApplication} from "@angular/platform-browser";
import {AppComponent} from "./app/app.component";
import {importProvidersFrom} from "@angular/core";
import {AppRoutingModule} from "./app/app-routing.module";
import {AppHttpModule} from "./app/app-http.module";
import { provideStore } from '@ngrx/store';
import {ordersReducer} from "./app/store/orders.reducer";
import { provideEffects } from '@ngrx/effects';


bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(AppRoutingModule, AppHttpModule),
    provideStore({ orders: ordersReducer }),
    provideEffects()
]

})
// platformBrowserDynamic().bootstrapModule(AppModule)
//   .catch(err => console.error(err));
