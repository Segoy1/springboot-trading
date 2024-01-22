import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {BehaviorSubject} from "rxjs";

export abstract class AbstractWebsocketService<T> {
  protected response: T[] = [];
  responseChangedSubject = new BehaviorSubject<T[]>(null);
  private url: string = 'http://localhost:8080/websocket';
  client:any;

  constructor(topicName:string) {
    this.connection(topicName);
  }

  connection(topicName:string){
    let webSocket = new SockJS(this.url);
    this.client = Stomp.over(webSocket);
    let that = this;

    this.client.connect({}, function (frame: any){
      that.client.subscribe("/topic/"+topicName, (message:any)=>{
        if(message.body){
          that.updateOrAdd(<T>JSON.parse(message.body));
          that.responseChange();
        }
      })
    });
  }

  abstract updateOrAdd(response: T):void;

  responseChange() {
    this.responseChangedSubject.next(this.response);
  }

}
