import {Injectable} from "@angular/core";
import {BehaviorSubject, map, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {ProfitAndLoss} from "../../model/profit-and-loss.model";

@Injectable({providedIn: "root"})
export class ProfitLossService {
  private profitLoss: ProfitAndLoss[] = [];
  private errorMessage: string;
  private url: string = 'http://localhost:8080/portfolio/pnl';
  profitLossChanged = new BehaviorSubject<ProfitAndLoss[]>(null);

  constructor(private httpClient: HttpClient) {
  };

  initProfitLoss() {
    this.httpClient.get<ProfitAndLoss[]>(this.url).subscribe({
      next: (pnl) => {
        this.profitLoss = [];
        pnl.forEach((pnlValue) => {
          this.profitLoss.push(pnlValue)
          this.profitLossChange();
        });
      },
      error: (error) => {
        this.errorMessage = error;
      }
    })
  }

  profitLossChange() {
    this.profitLossChanged.next(this.profitLoss);
  }

  getForPosition(id:number):Observable<ProfitAndLoss>{
    return this.profitLossChanged.pipe(map<ProfitAndLoss[], ProfitAndLoss>(profitLoss=>
    {
      if(profitLoss){
      return profitLoss.filter(pnl=> pnl.id === id).pop();
      }
      return null;
    }));
  }
}
