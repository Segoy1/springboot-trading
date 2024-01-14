import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Position} from "../../model/position.model";

@Injectable({providedIn:"root"})
export class PortfolioService {

  private positions: Position[] = [];
  private errorMessage: string;
  private url: string = 'http://localhost:8080/portfolio';

  constructor(private httpClient: HttpClient) {
  };

  initPortfolio() {
    this.positions = [];

    this.httpClient.get<Position[]>(this.url).subscribe({
      next: (positions) => {
        positions.forEach((position) => {
          this.positions.push(position)
        });
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
  }

  getPortfolio() {
    return this.positions;
  }
}
