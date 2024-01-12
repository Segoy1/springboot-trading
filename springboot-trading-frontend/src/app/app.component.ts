import {Component, OnInit} from '@angular/core';
import {AuthResponse, LoginService} from "./header/login/login.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{

  constructor(private loginService: LoginService) {
  }

  ngOnInit() {
    this.loginService.autoLogin();
  }

}
