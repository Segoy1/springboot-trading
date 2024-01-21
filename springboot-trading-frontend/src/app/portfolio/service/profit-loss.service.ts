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

  connection(){
    let webSocket = new SockJS(this.url);
    this.client = Stomp.over(webSocket);
    let that = this;

    this.client.connect({}, function (frame: any){
      console.log("Connection Started");
      that.client.subscribe("/topic/singlePnL", (message:ProfitAndLoss[])=>{
        console.log("Websocket works");
        if(message){
          console.log("message arrives: " + message);
          that.profitLoss = message;
          that.profitLossChange();
        }
      })
    });
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
