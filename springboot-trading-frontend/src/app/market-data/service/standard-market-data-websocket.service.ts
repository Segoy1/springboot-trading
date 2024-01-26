import {Injectable} from "@angular/core";
import {StandardMarketData} from "../../model/market-data/standard-market-data.model";
import {environment} from "../../../environments/environment.production";
import {StandardTicker} from "../../model/market-data/standard-ticker.model";
import {AbstractMarketDataWebsocketService} from "./abstract-market-data-websocket.service";


@Injectable({providedIn: 'root'})
export class StandardMarketDataWebsocketService extends AbstractMarketDataWebsocketService<StandardTicker, StandardMarketData>{

  constructor() {
    super(environment.standardMarketDataTopic);
  }

  add(tickerId: number, data: [StandardMarketData]) {
    this.contractTicks.push({tickerId: tickerId, data: data});
  }
}
