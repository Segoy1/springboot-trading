import {Component, OnInit} from '@angular/core';
import {LoginService} from "./login/login.service";
import {HeaderComponent} from "./header/header.component";
import {RouterModule} from "@angular/router";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {LoginInterceptorService} from "./login/login-interceptor.service";

@Component({
  standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports:[HeaderComponent, RouterModule]
})
export class AppComponent implements OnInit{

  constructor(private loginService: LoginService) {
  }

  ngOnInit() {
    this.loginService.autoLogin();
  }

}
