import {NgModule} from "@angular/core";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {LoginInterceptorService} from "./login/login-interceptor.service";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@NgModule({
  imports: [HttpClientModule, BrowserAnimationsModule],
  providers:[  {provide: HTTP_INTERCEPTORS, useClass: LoginInterceptorService, multi: true}]
})
export class AppHttpModule {
}
