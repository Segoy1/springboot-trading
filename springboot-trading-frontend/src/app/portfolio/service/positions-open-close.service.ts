import {Injectable} from "@angular/core";
import {Position} from "../../model/position.model";
import {BehaviorSubject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Injectable({providedIn: "root"})
export class PositionsOpenCloseService {


  private pnlUrl = environment.apiUrl+'portfolio/pnl';
  private cancelSuffix = '/cancel';
  constructor(private httpClient: HttpClient) {
  };

  initPositions() {
    this.httpClient.get(this.pnlUrl).subscribe();
  }

  cancelPositions(){
    this.httpClient.get(this.pnlUrl+this.cancelSuffix).subscribe();
  }
}
