import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Injectable({providedIn: "root"})
export class PositionsOpenCloseService {


  private pnlUrl = environment.apiUrl+'portfolio/pnl';
  private positionsUrl = environment.apiUrl+'portfolio/positions';
  private cancelSuffix = '/cancel';
  private storageItemName = 'isPortfolioProfitLossCallOpen';

  constructor(private httpClient: HttpClient) {
  };

  initPositions() {
    if(this.isPortfolioPnlCallOpen()){
      this.cancelPositions();
    }
    this.httpClient.get(this.positionsUrl).subscribe();
    this.httpClient.get(this.pnlUrl).subscribe();
    localStorage.setItem(this.storageItemName, JSON.stringify(true));
  }

  cancelPositions(){
    this.httpClient.get(this.pnlUrl+this.cancelSuffix).subscribe();
    localStorage.setItem(this.storageItemName, JSON.stringify(false));
  }
  private isPortfolioPnlCallOpen(){
    return JSON.parse(localStorage.getItem(this.storageItemName))
  }
}
