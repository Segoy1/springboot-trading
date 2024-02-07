import {Component, Input, OnInit} from '@angular/core';
import {ProfitAndLoss} from "../../../../model/profit-and-loss.model";
import {Position} from "../../../../model/position.model";
import {DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {Subscription} from "rxjs";
import {ProfitLossWebsocketService} from "../../../service/profit-loss-websocket.service";
import {NotAvailablePipe} from "../../../../shared/not-available.pipe";

@Component({
  selector: 'app-combo-position-item',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    DecimalPipe,
    NotAvailablePipe
  ],
  templateUrl: './combo-position-item.component.html',
  styleUrl: './combo-position-item.component.css'
})
export class ComboPositionItemComponent implements OnInit{
  @Input() position: Position;
  profitAndLossSub: Subscription;
  profitAndLoss: ProfitAndLoss[];

  constructor(private profitLossWebsocketService:ProfitLossWebsocketService) {
  }

  ngOnInit() {
    const ids= []
    this.position.contractData.comboLegs.forEach(leg=>ids.push(leg.contractId))
    this.profitAndLossSub = this.profitLossWebsocketService.getForPosition(ids).subscribe((pnl) => {
      this.profitAndLoss = pnl;
    });
  }

  getPnl(id:number){
    const index = this.profitAndLoss.findIndex(pnl => pnl.id ===id);
    if(index>-1){
      return this.profitAndLoss[index];
    }else{
      return null;
    }
  }
}
