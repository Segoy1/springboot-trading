import {Component, OnInit} from '@angular/core';
import {PortfolioService} from "./portfolio.service";
import {Position} from "../model/position.model";

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css'
})
export class PortfolioComponent implements OnInit{

  positions:Position[];


  constructor(private portfolioService: PortfolioService) {
  }

  ngOnInit() {
    this.portfolioService.initPortfolio();
    this.positions = this.portfolioService.getPortfolio();
  }


}
