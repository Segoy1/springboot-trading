import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environmentDevelopment} from "../../../environments/environment.development";

@Injectable({providedIn: "root"})
export class PositionsOpenCloseService {


  private pnlUrl = environmentDevelopment.apiUrl+'portfolio/pnl';
  private positionsUrl = environmentDevelopment.apiUrl+'portfolio/positions';
  private cancelSuffix = '/cancel';
  private storageItemName = 'isPortfolioCallOpen';

  constructor(private httpClient: HttpClient) {
  };

  initPositions() {
    this.httpClient.get(this.pnlUrl).subscribe();
    this.httpClient.get(this.positionsUrl).subscribe();
    localStorage.setItem(this.storageItemName, JSON.stringify(true));
  }

  cancelPositions(){
    this.httpClient.get(this.pnlUrl+this.cancelSuffix).subscribe();
    localStorage.setItem(this.storageItemName, JSON.stringify(false));
  }
}
