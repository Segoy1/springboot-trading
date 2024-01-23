import {Component, OnDestroy, OnInit} from '@angular/core';
import {AccountSummaryOpenCloseService} from "../service/account-summary-open-close.service";
import {AccountSummary} from "../../model/account-summary.model";
import {ProfitAndLoss} from "../../model/profit-and-loss.model";
import {Subscription} from "rxjs";
import {AccountMarginComponent} from "./account-margin/account-margin.component";
import {AccountPnlComponent} from "./account-pnl/account-pnl.component";
import {NgIf} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css',
  imports: [AccountMarginComponent, AccountPnlComponent, NgIf]
})
export class AccountComponent implements OnInit, OnDestroy{

  constructor(private accountSummaryOpenCloseService: AccountSummaryOpenCloseService) {
  }

  ngOnInit() {
    this.accountSummaryOpenCloseService.initAccountSummary();
  }
  ngOnDestroy() {
    this.accountSummaryOpenCloseService.closeAccountSummary();
  }
}
