import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment.production";
import {OptionTicker} from "../../model/market-data/option-ticker.model";
import {OptionMarketData} from "../../model/market-data/option-market-data.model";
import {AbstractMarketDataWebsocketService} from "./abstract-market-data-websocket.service";

@Injectable({providedIn: 'root'})
export class OptionMarketDataWebsocketService extends AbstractMarketDataWebsocketService<OptionTicker, OptionMarketData>{

  constructor() {
    super(environment.optionMarketDataTopic);
  }

  add(tickerId: number, data: [OptionMarketData]){
    this.contractTicks.push({tickerId: tickerId, data: data});
  }
}
