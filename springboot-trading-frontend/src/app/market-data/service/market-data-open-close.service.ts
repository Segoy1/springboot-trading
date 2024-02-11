import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Contract} from "../../model/contract.model";
import {OptionMarketDataWebsocketService} from "./option-market-data-websocket.service";
import {StandardMarketDataWebsocketService} from "./standard-market-data-websocket.service";

@Injectable({providedIn: 'root'})
export class MarketDataOpenCloseService {

  private marketDataStartUrl = environment.apiUrl + 'market-data/start';
  private marketDataStopUrl = environment.apiUrl + 'market-data/stop';
  private marketDataStopAllUrl = environment.apiUrl + 'market-data/stopAll';

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
