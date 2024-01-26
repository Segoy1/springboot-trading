import {environment} from "../../../environments/environment.production";
import {BehaviorSubject, map, Observable} from "rxjs";
import {environmentDevelopment} from "../../../environments/environment.development";
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {BaseTicker} from "../../model/market-data/base-ticker.model";
import {BaseMarketData} from "../../model/market-data/base-market-data.model";

export abstract class AbstractMarketDataWebsocketService<Ticker extends BaseTicker, Data extends BaseMarketData> {
  protected contractTicks: Ticker[] = [];
  responseChangedSubject = new BehaviorSubject<Ticker[]>(null);
  private url: string = environmentDevelopment.apiUrl+'websocket';
  client:any;

  constructor(topic: string) {
    this.connection(topic);
  }

  connection(topicName:string){
    let webSocket = new SockJS(this.url);
    this.client = Stomp.over(webSocket);
    let that = this;

    this.client.connect({}, function (frame: any){
      that.client.subscribe("/topic/"+topicName, (message:any)=>{
        if(message.body){
          that.updateOrAdd(<Data>JSON.parse(message.body));
          that.responseChange();
        }
      })
    });
  }

  updateOrAdd(marketData: Data){
    const index = this.contractTicks.findIndex(obj=> obj.tickerId === marketData.tickerId)
    if(index>-1){
      const index2 = this.contractTicks[index].data.findIndex(data => data.field === marketData.field);
      if(index2>-1){
        this.contractTicks[index].data[index2] = marketData;
      }else{
        this.contractTicks[index].data.push(marketData);
      }
    }else{
      this.add(marketData.tickerId, [marketData]);
    }

  }
  abstract add(tickerId: number, data: [Data]);

  remove(tickerId: number){
    const index = this.contractTicks.findIndex(tick=> tick.tickerId ===tickerId);
    if(index>-1){
    this.contractTicks.splice(index,1);
    this.responseChange();
    }
  }

  responseChange() {
    this.responseChangedSubject.next(this.contractTicks);
  }

  getForContract(id:number):Observable<Ticker>{
    return this.responseChangedSubject.pipe(map<Ticker[], Ticker>(ticker=>
    {
      if(ticker){
        return ticker.find(tick=> tick.tickerId === id);
      }
      return null;
    }));
  }
}
