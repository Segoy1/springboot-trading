import {Component, OnInit} from '@angular/core';
import {AccountDetailsService} from "../service/account-details.service";
import {AccountSummary} from "../../model/account-summary.model";
import {ProfitAndLoss} from "../../model/profit-and-loss.model";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent implements OnInit{
  accountSummary: AccountSummary[];
  profitAndLoss: ProfitAndLoss;
  summarySub:Subscription;
  pnlSub:Subscription;

  constructor(private accountDetailService: AccountDetailsService) {
  }

  ngOnInit() {
    this.accountDetailService.initAccountSummary();
    this.summarySub = this.accountDetailService.accountSummaryChanged.subscribe(summary =>{
      this.accountSummary = summary;
    })
    this.accountDetailService.initPnL();
    this.pnlSub = this.accountDetailService.pnlChanged.subscribe(pnl=>{
      this.profitAndLoss = pnl;
    })
  }
}
