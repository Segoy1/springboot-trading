import {Injectable} from "@angular/core";
import {AbstractWebsocketService} from "../shared/abstract-websocket.service";
import {environment} from "../../environments/environment.production";
import {StandardMarketData} from "../model/standard-market-data.model";
import {map, Observable} from "rxjs";
import {OptionMarketData} from "../model/option-market-data.model";

@Injectable({providedIn:'root'})
export class StandardMarketDataWebsocketService extends AbstractWebsocketService<StandardMarketData>{

  constructor() {
    super(environment.standardMarketDataTopic);
  }

  updateOrAdd(marketData: any): void {
    const index =this.response.findIndex(inArray => inArray.tickerId === marketData.tickerId);
    if(index>-1){
      this.response[index]=marketData;
    }else {
      this.response.push(marketData);
    }
  }

  getForTicker(id: number): Observable<StandardMarketData> {
    return this.responseChangedSubject.pipe(map<StandardMarketData[], StandardMarketData>(ticker => {
        if (ticker) {
          return ticker.filter(tick => tick.tickerId === id).pop();
        } else {
          return null;
        }
      })
    );
  }

}
