import {NgModule} from "@angular/core";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {LoginInterceptorService} from "./login/login-interceptor.service";

@NgModule({
  imports: [HttpClientModule],
  providers:[  {provide: HTTP_INTERCEPTORS, useClass: LoginInterceptorService, multi: true}]
})
export class AppHttpModule {
}
