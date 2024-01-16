import {Component, OnInit} from '@angular/core';
import {Portfolio} from "../../model/portfolio.model";
import {Subscription} from "rxjs";
import {PortfolioService} from "../service/portfolio.service";
import {PositionItemComponent} from "./position-item/position-item.component";
import {NgForOf} from "@angular/common";

@Component({
  standalone: true,
  selector: 'app-position-list',
  templateUrl: './position-list.component.html',
  imports: [
    PositionItemComponent,
    NgForOf
  ],
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
