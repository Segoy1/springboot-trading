import {Component, Input, OnInit} from '@angular/core';
import {AccountSummary} from "../../../model/account-summary.model";
import {CommonModule, NgForOf} from "@angular/common";
import {Subscription} from "rxjs";
import {AccountMarginWebsocketService} from "./account-margin-websocket.service";

@Component({
  standalone: true,
  selector: 'app-account-margin',
  templateUrl: './account-margin.component.html',
  styleUrl: './account-margin.component.css',
  imports: [
    NgForOf
  ]
})
export class AccountMarginComponent implements OnInit {
  accountSummary: AccountSummary[];
  accountSummarySub: Subscription;


  constructor(private accountMarginWebsocketService: AccountMarginWebsocketService) {
  }

  ngOnInit() {
    this.accountSummarySub = this.accountMarginWebsocketService.responseChangedSubject.subscribe(
      summary => {
        this.accountSummary = summary;
      }
    );
  }
  isAccountSummaryReady(){
    return !!this.accountSummary;
  }

}
