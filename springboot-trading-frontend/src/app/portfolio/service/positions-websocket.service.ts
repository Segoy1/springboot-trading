import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Position} from "../../model/position.model";
import {BehaviorSubject} from "rxjs";
import {environmentDevelopment} from "../../../environments/environment.development";
import {AbstractWebsocketService} from "../../shared/abstract-websocket.service";

@Injectable({providedIn: "root"})
export class PositionsWebsocketService extends AbstractWebsocketService<Position>{

  constructor() {
    super(environmentDevelopment.positionsTopic);
  }

  updateOrAdd(position: Position): void {
    const index =this.response.findIndex(inArray => inArray.id === position.id);
    if(index>-1){
      this.response[index]=position;
    }else {
      this.response.push(position);
    }
  }
}
