import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ProfitAndLoss} from "../../../../model/profit-and-loss.model";
import {DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {Subscription} from "rxjs";
import {ProfitLossWebsocketService} from "../../../service/profit-loss-websocket.service";
import {NotAvailablePipe} from "../../../../shared/not-available.pipe";
import {ComboLegDataDisplayComponent} from "../../../../shared/combo-leg-data-display/combo-leg-data-display.component";
import {ComboLeg} from "../../../../model/comboleg.model";

@Component({
  selector: 'app-combo-position-item',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    DecimalPipe,
    NotAvailablePipe,
    ComboLegDataDisplayComponent
  ],
  templateUrl: './combo-position-item.component.html',
  styleUrl: './combo-position-item.component.css'
})
export class ComboPositionItemComponent implements OnInit, OnDestroy {
  @Input() comboLegs: ComboLeg[];
  profitAndLossSub: Subscription;
  profitAndLoss: ProfitAndLoss[];

  constructor(private profitLossWebsocketService: ProfitLossWebsocketService) {
  }

  ngOnInit() {
    const ids: number[] = []
    this.comboLegs.forEach(leg => ids.push(leg.contractId))
    this.profitAndLossSub = this.profitLossWebsocketService.getForPosition(ids).subscribe((pnl) => {
      this.profitAndLoss = pnl;
    });

  }

  getPnl(id: number) {
    const index = this.profitAndLoss.findIndex(pnl => pnl.id === id);
    if (index > -1) {
      return this.profitAndLoss[index];
    } else {
      return null;
    }
  }

  ngOnDestroy() {
    this.profitAndLossSub.unsubscribe();
  }
}
