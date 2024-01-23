import {Injectable} from "@angular/core";
import {ProfitAndLoss} from "../../model/profit-and-loss.model";
import {BehaviorSubject, map, Observable} from "rxjs";
import {AbstractWebsocketService} from "../../shared/abstract-websocket.service";

@Injectable({providedIn: "root"})
export class ProfitLossWebsocketService extends AbstractWebsocketService<ProfitAndLoss>{

  constructor() {
    super('singlePnL');
  }

  updateOrAdd(pnl: ProfitAndLoss){
    const index =this.response.findIndex(inArray => inArray.id === pnl.id);
    if(index>-1){
      this.response[index]=pnl;
    }else {
      this.response.push(pnl);
    }
  }
  getForPosition(id:number):Observable<ProfitAndLoss>{
    return this.responseChangedSubject.pipe(map<ProfitAndLoss[], ProfitAndLoss>(profitLoss=>
    {
      if(profitLoss){
        return profitLoss.filter(pnl=> pnl.id === id).pop();
      }
      return null;
    }));
  }

}
