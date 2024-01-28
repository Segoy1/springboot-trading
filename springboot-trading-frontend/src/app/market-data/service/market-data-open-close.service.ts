import {Injectable} from "@angular/core";
import {environmentDevelopment} from "../../../environments/environment.development";
import {HttpClient} from "@angular/common/http";
import {Contract} from "../../model/contract.model";
import {OptionMarketDataWebsocketService} from "./option-market-data-websocket.service";
import {StandardMarketDataWebsocketService} from "./standard-market-data-websocket.service";

@Injectable({providedIn: 'root'})
export class MarketDataOpenCloseService {

  private marketDataStartUrl = environmentDevelopment.apiUrl + 'market-data/start';
  private marketDataStopUrl = environmentDevelopment.apiUrl + 'market-data/stop';
  private marketDataStopAllUrl = environmentDevelopment.apiUrl + 'market-data/stopAll';

  constructor(private http: HttpClient,
              private optionMarketDataWebsocketService:OptionMarketDataWebsocketService,
              private standardMarketDataWebsocketService:StandardMarketDataWebsocketService) {
  }

  startMarketData(contract: Contract) {
    this.http.post<Contract>(this.marketDataStartUrl, contract).subscribe();
  }

  stopMarketData(id: number) {
    this.http.get(this.marketDataStopUrl + '?id=' + id).subscribe();
    this.standardMarketDataWebsocketService.remove(id);
    this.optionMarketDataWebsocketService.remove(id);
  }
}
