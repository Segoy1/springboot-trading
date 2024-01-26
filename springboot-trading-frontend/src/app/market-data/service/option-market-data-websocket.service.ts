import {Injectable} from "@angular/core";
import {environment} from "../../../environments/environment.production";
import {StandardTicker} from "../../model/standard-ticker.model";
import {BehaviorSubject, map, Observable} from "rxjs";
import {environmentDevelopment} from "../../../environments/environment.development";
import {StandardMarketData} from "../../model/standard-market-data.model";
import {OptionTicker} from "../../model/option-ticker.model";
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {OptionMarketData} from "../../model/option-market-data.model";

@Injectable({providedIn: 'root'})
export class OptionMarketDataWebsocketService {
  private response: OptionTicker[] = [];
  responseChangedSubject = new BehaviorSubject<OptionTicker[]>(null);
  private url: string = environmentDevelopment.apiUrl+'websocket';
  client:any;

  constructor() {
    this.connection(environment.optionMarketDataTopic);
  }

  connection(topicName:string){
    let webSocket = new SockJS(this.url);
    this.client = Stomp.over(webSocket);
    let that = this;

    this.client.connect({}, function (frame: any){
      that.client.subscribe("/topic/"+topicName, (message:any)=>{
        if(message.body){
          that.updateOrAdd(<OptionMarketData>JSON.parse(message.body));
          that.responseChange();
        }
      })
    });
  }

  updateOrAdd(marketData: OptionMarketData){
    const index = this.response.findIndex(obj=> obj.tickerId === marketData.tickerId)
    if(index>-1){
      const index2 = this.response[index].data.findIndex(data => data.field === marketData.field);
      if(index2>-1){
        this.response[index].data[index2] = marketData;
      }else{
        this.response[index].data.push(marketData);
      }
    }else{
      this.response.push({tickerId: marketData.tickerId, data: [marketData]});
    }

  }

  responseChange() {
    this.responseChangedSubject.next(this.response);
  }

  getForContract(id:number):Observable<OptionTicker>{
    return this.responseChangedSubject.pipe(map<OptionTicker[], OptionTicker>(ticker=>
    {
      if(ticker){
        return ticker.find(tick=> tick.tickerId === id);
      }
      return null;
    }));
  }
}
