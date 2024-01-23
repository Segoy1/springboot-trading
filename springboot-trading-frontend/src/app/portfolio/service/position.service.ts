import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Position} from "../../model/position.model";
import {BehaviorSubject} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable({providedIn: "root"})
export class PositionService {

  private positions: Position[] = [];
  private errorMessage: string;
  private url: string = environment.apiUrl+'portfolio';
  positionsChanged =  new BehaviorSubject<Position[]>(null);

  constructor(private httpClient: HttpClient) {
  };

  initPositions() {
    this.httpClient.get<Position[]>(this.url).subscribe({
      next: (positions) => {
        this.positions = [];
        positions.forEach((position) => {
          this.positions.push(position)
          this.positionsChange();
        });
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
  }
  positionsChange(){
    this.positionsChanged.next(this.positions);
  }
}
