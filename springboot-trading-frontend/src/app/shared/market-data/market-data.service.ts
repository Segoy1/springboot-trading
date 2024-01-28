import {Injectable} from "@angular/core";
import {MarketDataOpenCloseService} from "../../market-data/service/market-data-open-close.service";
import {StandardMarketDataWebsocketService} from "../../market-data/service/standard-market-data-websocket.service";
import {StandardTicker} from "../../model/market-data/standard-ticker.model";
import {Contract} from "../../model/contract.model";


@Injectable({providedIn:'root'})
export class MarketDataService{

  constructor(private marketDataOpenCloseService:MarketDataOpenCloseService,
              private standardMarketDataWebsocketService:StandardMarketDataWebsocketService){

  }

  openMarketDataIfNew(contract: Contract){
    let ticker : StandardTicker;
    this.standardMarketDataWebsocketService.getForContract(contract.id).subscribe((openCall)=>{
      ticker = openCall;
    }).unsubscribe();
    if(!ticker){
      this.marketDataOpenCloseService.startMarketData(contract);
    }

  }
}
