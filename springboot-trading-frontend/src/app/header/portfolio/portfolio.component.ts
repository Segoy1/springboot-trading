import {Component, OnInit} from '@angular/core';
import {PortfolioService} from "./service/portfolio.service";
import {Position} from "../model/position.model";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css'
})
export class PortfolioComponent implements OnInit {

  positions: Position[];
  positionsSub: Subscription;


  constructor(private portfolioService: PortfolioService) {
  }

  ngOnInit() {
    this.positionsSub = this.portfolioService.positionsChanged.subscribe(positions => {
      this.positions = positions;
    });
    this.portfolioService.initPortfolio();
  }


}
