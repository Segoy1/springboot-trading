import {Injectable} from "@angular/core";
import {LoginService} from "./login.service";
import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {exhaustMap, Observable, take} from "rxjs";

@Injectable()
export class LoginInterceptorService implements HttpInterceptor{

  constructor(private loginService: LoginService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return this.loginService.user.pipe(
      take(1),
      exhaustMap(user => {
        if(!user){
          return next.handle(req);
        }
        const modifiedReq = req.clone({
          headers: new HttpHeaders().set('Cookie', 'JSESSIONID='+user.token)
        });
        return next.handle(modifiedReq);
      })
    )
  }

}
