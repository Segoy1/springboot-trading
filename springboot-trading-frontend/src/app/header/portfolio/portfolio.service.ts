import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Position} from "../model/position.model";

@Injectable()
export class PortfolioService {

  private positions: Position[] = [];
  private errorMessage:string;
  private url:string = 'http://localhost:8080/portfolio';

  constructor(private httpClient: HttpClient){
  };

  initPortfolio(){
    const httpOptions = {
      headers: new HttpHeaders({
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      })
    };

    this.httpClient.get<Position[]>(this.url, httpOptions).subscribe(
      positions => {
        positions.forEach(position => {
          this.positions.push(position);
        })
      },
      error => {this.errorMessage}
    )
  }

  getPortfolio(){
    return this.positions;
  }
}
