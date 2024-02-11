import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";


@Injectable({providedIn:"root"})
export class AccountSummaryOpenCloseService {
  private summaryUrl: string = environment.apiUrl+'account-summary';
  private pnLUrl: string = environment.apiUrl+'account-summary/pnl';
  private cancelSuffix = '/cancel';
  private storageItemName = 'isAccountSummaryOpen';


  constructor(private httpClient: HttpClient) {
  };

  initAccountSummary(){
    //Close first if Open Call exists
    if(this.isAccountSummaryOpen()){
      this.closeAccountSummary();
    }
    this.httpClient.get(this.summaryUrl).subscribe();
    this.httpClient.get(this.pnLUrl).subscribe();
    localStorage.setItem(this.storageItemName, JSON.stringify(true));
  }

  closeAccountSummary(){
    this.httpClient.get(this.summaryUrl+this.cancelSuffix).subscribe();
    this.httpClient.get(this.pnLUrl+this.cancelSuffix).subscribe();
    localStorage.setItem(this.storageItemName, JSON.stringify(false));
  }


  private isAccountSummaryOpen(){
    return JSON.parse(localStorage.getItem(this.storageItemName));
  }

}
