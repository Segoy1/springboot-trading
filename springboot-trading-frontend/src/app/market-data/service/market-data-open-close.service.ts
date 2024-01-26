import {Injectable} from "@angular/core";
import {environmentDevelopment} from "../../../environments/environment.development";
import {HttpClient} from "@angular/common/http";
import {Contract} from "../../model/contract.model";

@Injectable({providedIn: 'root'})
export class MarketDataOpenCloseService {

  private marketDataStartUrl = environmentDevelopment.apiUrl + 'market-data/start';
  private marketDataStopUrl = environmentDevelopment.apiUrl + 'market-data/stop';
  private marketDataStopAllUrl = environmentDevelopment.apiUrl + 'market-data/stopAll';
  private contracts: Contract[] = [];
  private localStorageKey = 'openMarketDataCalls';

  constructor(private http: HttpClient) {
  }

  startMarketData(contract: Contract) {
    this.http.post<Contract>(this.marketDataStartUrl, contract).subscribe({
      next:
        response => {
          if(!this.contracts){
            this.contracts = [response];
          }
          this.contracts.push(response);
          console.log(this.contracts);
          localStorage.setItem(this.localStorageKey, JSON.stringify(this.contracts));
        }
      , error:
        err => {
          console.log(err);
        }
    });
  }

  stopMarketData(id: number) {
    this.http.get(this.marketDataStopUrl + '?id=' + id).subscribe();
    const index = this.contracts.findIndex(contract => contract.id ===id);
    this.contracts.splice(index,1);
    localStorage.setItem(this.localStorageKey, JSON.stringify(this.contracts));
  }


  initContracts() {
    this.contracts = JSON.parse(localStorage.getItem(this.localStorageKey));
  }

  getContracts() {
    return this.contracts.slice();
  }

  getContractById(id: number) {
    return this.contracts.find(contract => contract.id === id);

  }

}
