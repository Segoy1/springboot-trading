import {bootstrapApplication} from "@angular/platform-browser";
import {AppComponent} from "./app/app.component";
import {importProvidersFrom} from "@angular/core";
import {AppRoutingModule} from "./app/app-routing.module";
import {HTTP_INTERCEPTORS, HttpClientModule, provideHttpClient, withInterceptors} from "@angular/common/http";
import {LoginInterceptorService} from "./app/login/login-interceptor.service";
import {AppHttpModule} from "./app/app-http.module";


bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(AppRoutingModule, AppHttpModule)]

})
// platformBrowserDynamic().bootstrapModule(AppModule)
//   .catch(err => console.error(err));
