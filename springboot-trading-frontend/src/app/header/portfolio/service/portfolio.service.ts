import {Injectable} from "@angular/core";
import {PositionService} from "./position.service";
import {ProfitLossService} from "./profit-loss.service";
import {BehaviorSubject, Subscription} from "rxjs";
import {Position} from "../../model/position.model";
import {ProfitAndLoss} from "../../model/profit-and-loss.model";
import {Portfolio} from "../../model/portfolio.model";

@Injectable({providedIn:"root"})
export class PortfolioService{

  positionsSub: Subscription;
  profitLossSub: Subscription;
  positions: Position[];
  profitLoss: ProfitAndLoss[];
  portfolioChanged = new BehaviorSubject<Portfolio[]>(null);
  portfolio: Portfolio[];



  constructor(private positionService: PositionService, private profitLossService: ProfitLossService) {
  }

  initPortfolio(){
    this.positionsSub = this.positionService.positionsChanged.subscribe(positions => {
      this.positions = positions;
      this.portfolioChange();
    });
    this.profitLossSub = this.profitLossService.profitLossChanged.subscribe(pnl=>{
      this.profitLoss = pnl;
      this.portfolioChange();
    });
    this.positionService.initPositions();
    this.profitLossService.initProfitLoss();
  }

  joinPortfolio(){
    const portfolio:Portfolio[] = [];
    this.profitLoss.forEach(pnl=>{
      this.positions.forEach(position=>{
        if(pnl.id === position.contractData.contractId){
          portfolio.push({position: position, profitAndLoss: pnl});
        }
      })
    });
    this.portfolio = portfolio;
  }

  portfolioChange() {
    if (this.positions && this.profitLoss) {
    this.joinPortfolio();
  }
    this.portfolioChanged.next(this.portfolio);
  }
}
