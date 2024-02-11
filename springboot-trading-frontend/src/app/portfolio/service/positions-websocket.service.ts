import {Injectable} from '@angular/core';
import {Position} from "../../model/position.model";
import {environment} from "../../../environments/environment";
import {AbstractWebsocketService} from "../../shared/abstract-websocket.service";

@Injectable({providedIn: "root"})
export class PositionsWebsocketService extends AbstractWebsocketService<Position>{

  constructor() {
    super(environment.positionsTopic);
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
