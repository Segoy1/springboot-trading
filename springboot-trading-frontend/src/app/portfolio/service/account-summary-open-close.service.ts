import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {AccountSummary} from "../../model/account-summary.model";
import {ProfitAndLoss} from "../../model/profit-and-loss.model";
import {BehaviorSubject} from "rxjs";


@Injectable({providedIn:"root"})
export class AccountSummaryOpenCloseService {
  private summaryUrl: string = 'http://localhost:8080/account-summary';
  private pnLUrl: string = 'http://localhost:8080/account-summary/pnl';
  private cancelSuffix = '/cancel';


  constructor(private httpClient: HttpClient) {
  };

  initAccountSummary(){
    this.httpClient.get(this.summaryUrl).subscribe();
    this.httpClient.get(this.pnLUrl).subscribe();
  }
  closeAccountSummary(){
    this.httpClient.get(this.summaryUrl+this.cancelSuffix).subscribe();
    this.httpClient.get(this.pnLUrl+this.cancelSuffix).subscribe();
  }

}
