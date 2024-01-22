import {Injectable} from "@angular/core";
import {Position} from "../../model/position.model";
import {BehaviorSubject} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({providedIn: "root"})
export class PositionsOpenCloseService {


  private pnlUrl = 'http://localhost:8080/portfolio/pnl';
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
