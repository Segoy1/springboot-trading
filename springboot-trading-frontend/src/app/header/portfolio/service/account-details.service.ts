import {Injectable} from "@angular/core";
import {Position} from "../../model/position.model";
import {HttpClient} from "@angular/common/http";
import {AccountSummary} from "../../model/account-summary.model";
import {ProfitAndLoss} from "../../model/profit-and-loss.model";
import {BehaviorSubject} from "rxjs";


@Injectable({providedIn:"root"})
export class AccountDetailsService{
  private accountSummaries: AccountSummary[] = [];
  private profitAndLoss: ProfitAndLoss;
  private errorMessage: string;
  private summaryUrl: string = 'http://localhost:8080/account-summary';
  private pnLUrl: string = 'http://localhost:8080/account-summary/pnl';
  pnlChanged = new BehaviorSubject<ProfitAndLoss>(null);
  accountSummaryChanged = new BehaviorSubject<AccountSummary[]>(null);

  constructor(private httpClient: HttpClient) {
  };

  initAccountSummary(){
    this.httpClient.get<AccountSummary[]>(this.summaryUrl).subscribe({
      next: (positions) => {
        this.accountSummaries = [];
        positions.forEach((summary) => {
          this.accountSummaries.push(summary)
          this.accountSummaryChange();
        });
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
  }
  initPnL(){
    this.httpClient.get<ProfitAndLoss>(this.pnLUrl).subscribe({
      next: (pnl) => {
        this.profitAndLoss = pnl
        this.profitAndLossChange();
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
  }
 accountSummaryChange(){
    this.accountSummaryChanged.next(this.accountSummaries);
  }
  profitAndLossChange(){
    this.pnlChanged.next(this.profitAndLoss);
  }
}
