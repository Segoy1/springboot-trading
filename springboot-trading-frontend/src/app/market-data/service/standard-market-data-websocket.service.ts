import {Injectable} from "@angular/core";
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {StandardMarketData} from "../../model/standard-market-data.model";
import {BehaviorSubject, map, Observable} from "rxjs";
import {environmentDevelopment} from "../../../environments/environment.development";
import {environment} from "../../../environments/environment.production";
import {StandardTicker} from "../../model/standard-ticker.model";


@Injectable({providedIn: 'root'})
export class StandardMarketDataWebsocketService {
  private response: StandardTicker[] = [];
  responseChangedSubject = new BehaviorSubject<StandardTicker[]>(null);
  private url: string = environmentDevelopment.apiUrl+'websocket';
  client:any;

  constructor() {
    this.connection(environment.standardMarketDataTopic);
  }

  connection(topicName:string){
    let webSocket = new SockJS(this.url);
    this.client = Stomp.over(webSocket);
    let that = this;

    this.client.connect({}, function (frame: any){
      that.client.subscribe("/topic/"+topicName, (message:any)=>{
        if(message.body){
          that.updateOrAdd(<StandardMarketData>JSON.parse(message.body));
          that.responseChange();
        }
      })
    });
  }

  updateOrAdd(marketData: StandardMarketData){
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

  getForContract(id:number):Observable<StandardTicker>{
    return this.responseChangedSubject.pipe(map<StandardTicker[], StandardTicker>(ticker=>
    {
      if(ticker){
        return ticker.find(tick=> tick.tickerId === id);
      }
      return null;
    }));
  }

}
