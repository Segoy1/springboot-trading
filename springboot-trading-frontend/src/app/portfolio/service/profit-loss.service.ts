import {Injectable} from "@angular/core";
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {ProfitAndLoss} from "../../model/profit-and-loss.model";
import {BehaviorSubject, map, Observable} from "rxjs";

@Injectable({providedIn: "root"})
export class ProfitLossService {
  private profitLoss: ProfitAndLoss[] = [];
  profitLossChanged = new BehaviorSubject<ProfitAndLoss[]>(null);
  private url: string = 'http://localhost:8080/websocket';
  client:any;

  constructor() {
    this.connection();
  }

  connection(){
    let webSocket = new SockJS(this.url);
    this.client = Stomp.over(webSocket);
    let that = this;

    this.client.connect({}, function (frame: any){
      that.client.subscribe("/topic/singlePnL", (message:any)=>{
        if(message.body){
          that.updateOrAdd(<ProfitAndLoss>JSON.parse(message.body));
          that.profitLossChange();
        }
      })
    });
  }

  updateOrAdd(pnl: ProfitAndLoss){
    const index =this.profitLoss.findIndex(inArray => inArray.id === pnl.id);
    if(index>-1){
      this.profitLoss[index]=pnl;
    }else {
      this.profitLoss.push(pnl);
    }
  }

  profitLossChange() {
    this.profitLossChanged.next(this.profitLoss);
  }
  getForPosition(id:number):Observable<ProfitAndLoss>{
    return this.profitLossChanged.pipe(map<ProfitAndLoss[], ProfitAndLoss>(profitLoss=>
    {
      if(profitLoss){
        return profitLoss.filter(pnl=> pnl.id === id).pop();
      }
      return null;
    }));
  }

}
