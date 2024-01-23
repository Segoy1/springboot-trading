import {Component, OnDestroy, OnInit} from '@angular/core';
import {ConnectionService} from "./connection.service";
import {Subscription} from "rxjs";
import {LoginService} from "../login/login.service";
import {RouterLinkActive, RouterModule} from "@angular/router";
import {DropdownDirective} from "../shared/dropdown.directive";
import {CommonModule} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-header',
  templateUrl: './header.component.html',
  imports: [
    RouterLinkActive,
    RouterModule,
    DropdownDirective,
    CommonModule
  ],
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {
  collapsed: boolean = false;
  isConnected: any = true;
  isLoggedIn = true;
  private userSubscription: Subscription;

  constructor(private httpService: ConnectionService, private loginService: LoginService) {
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

  onLogout() {
    this.loginService.logout();
  }

}
