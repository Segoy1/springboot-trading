import {Injectable} from "@angular/core";
import {AbstractWebsocketService} from "../../shared/abstract-websocket.service";
import {environment} from "../../../environments/environment.production";
import {map, Observable} from "rxjs";
import {ProfitAndLoss} from "../../model/profit-and-loss.model";
import {OptionMarketData} from "../../model/option-market-data.model";

@Injectable({providedIn: 'root'})
export class OptionMarketDataWebsocketService extends AbstractWebsocketService<OptionMarketData> {

  constructor() {
    super(environment.optionMarketDataTopic);
  }

  updateOrAdd(marketData: any): void {
    const index = this.response.findIndex(inArray => inArray.tickerId === marketData.tickerId);
    if (index > -1) {
      this.response[index] = marketData;
    } else {
      this.response.push(marketData);
    }
  }
}
