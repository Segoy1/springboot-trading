import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Position} from "../../model/position.model";
import {BehaviorSubject} from "rxjs";

@Injectable({providedIn: "root"})
export class PortfolioService {

  private positions: Position[] = [];
  private errorMessage: string;
  private url: string = 'http://localhost:8080/portfolio';
  positionsChanged =  new BehaviorSubject<Position[]>(null);

  constructor(private httpClient: HttpClient) {
  };

  initPortfolio() {
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
