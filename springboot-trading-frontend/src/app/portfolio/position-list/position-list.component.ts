import {Component, OnInit} from '@angular/core';
import {Portfolio} from "../../model/portfolio.model";
import {Subscription} from "rxjs";
import {PortfolioService} from "../service/portfolio.service";

@Component({
  selector: 'app-position-list',
  templateUrl: './position-list.component.html',
  styleUrl: './position-list.component.css'
})
export class PositionListComponent implements OnInit {

  portfolio: Portfolio[];
  portfolioSub: Subscription;


  constructor(private portfolioService: PortfolioService) {
  }
  ngOnInit() {
    this.portfolioSub = this.portfolioService.portfolioChanged.subscribe(positions => {
      this.portfolio = positions;
    });
    this.portfolioService.initPortfolio();
  }

}
