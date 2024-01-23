import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {AccountSummary} from "../../model/account-summary.model";
import {ProfitAndLoss} from "../../model/profit-and-loss.model";
import {BehaviorSubject} from "rxjs";
import {environment} from "../../../environments/environment";


@Injectable({providedIn:"root"})
export class AccountSummaryOpenCloseService {
  private summaryUrl: string = environment.apiUrl+'account-summary';
  private pnLUrl: string = environment.apiUrl+'account-summary/pnl';
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
