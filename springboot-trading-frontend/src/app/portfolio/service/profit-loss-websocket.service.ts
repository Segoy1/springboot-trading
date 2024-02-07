import {Injectable} from "@angular/core";
import {ProfitAndLoss} from "../../model/profit-and-loss.model";
import {BehaviorSubject, map, Observable} from "rxjs";
import {AbstractWebsocketService} from "../../shared/abstract-websocket.service";
import {environment} from "../../../environments/environment.production";

@Injectable({providedIn: "root"})
export class ProfitLossWebsocketService extends AbstractWebsocketService<ProfitAndLoss>{

  constructor() {
    super(environment.singlePnLTopic);
  }

  updateOrAdd(pnl: ProfitAndLoss){
    const index =this.response.findIndex(inArray => inArray.id === pnl.id);
    if(index>-1){
      this.response[index]=pnl;
    }else {
      this.response.push(pnl);
    }
  }
  getForPosition(ids:number[]):Observable<ProfitAndLoss[]>{
    return this.responseChangedSubject.pipe(map(profitLoss=>
    {
      if(profitLoss){
        return profitLoss.filter(pnl=> ids.includes(pnl.id) );
      }
      return [];
    }));
  }

}
