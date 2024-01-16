import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {PortfolioService} from "./service/portfolio.service";
import {Portfolio} from "../model/portfolio.model";
import {AccountComponent} from "./account/account.component";
import {PositionListComponent} from "./position-list/position-list.component";

@Component({
  standalone: true,
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css',
  imports: [
    AccountComponent,
    PositionListComponent
  ]
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
