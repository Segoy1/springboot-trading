import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {PortfolioService} from "./service/portfolio.service";
import {Portfolio} from "../model/portfolio.model";

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css'
})
export class PortfolioComponent implements OnInit {

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
