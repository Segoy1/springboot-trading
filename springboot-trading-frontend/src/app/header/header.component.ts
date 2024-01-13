import {Component, EventEmitter, OnDestroy, OnInit, Output} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HttpService} from "./http.service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {LoginService} from "./login/login.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {
  collapsed: boolean = false;
  isConnected: any = true;
  isLoggedIn = true;
  private userSubscription: Subscription;

  constructor(private httpService: HttpService, private loginService: LoginService) {
  }

  ngOnInit() {
    this.userSubscription = this.loginService.user.subscribe((user) => {
      this.isLoggedIn = !!user;
    })
  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
  }

  onConnect(connect: boolean) {
    if (connect) {
      this.httpService.getConnect().subscribe(
        response => {
          console.log(response)
          this.isConnected = response;
        },
        (error) => {
          console.log(error);
        });
    }
    if (!connect) {
      this.httpService.getDisconnect().subscribe((data) => {
        console.log(data);
        this.isConnected = data;
      })
    }
  }
  onLogout(){
    this.loginService.logout();
  }

}
