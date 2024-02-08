import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ProfitAndLoss} from "../../../../model/profit-and-loss.model";
import {Position} from "../../../../model/position.model";
import {DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {Subscription} from "rxjs";
import {ProfitLossWebsocketService} from "../../../service/profit-loss-websocket.service";
import {NotAvailablePipe} from "../../../../shared/not-available.pipe";
import {ContractDataIdResolverRestService} from "../../../service/contract-data-id-resolver-rest.service";
import {Contract} from "../../../../model/contract.model";

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
export class ComboPositionItemComponent implements OnInit, OnDestroy {
  @Input() position: Position;
  profitAndLossSub: Subscription;
  profitAndLoss: ProfitAndLoss[];
  contracts: Contract[] = [];
  contractsSub: Subscription;

  constructor(private profitLossWebsocketService: ProfitLossWebsocketService,
              private contractDataRestService: ContractDataIdResolverRestService) {
  }

  ngOnInit() {
    const ids: number[] = []
    this.position.contractData.comboLegs.forEach(leg => ids.push(leg.contractId))
    this.profitAndLossSub = this.profitLossWebsocketService.getForPosition(ids).subscribe((pnl) => {
      this.profitAndLoss = pnl;
    });
    ids.forEach(id =>
      this.contractsSub = this.contractDataRestService.getContractData(id).subscribe(contract => {

        console.log(contract);

        this.contracts.push(contract);
      }))
  }

  getPnl(id: number) {
    const index = this.profitAndLoss.findIndex(pnl => pnl.id === id);
    if (index > -1) {
      return this.profitAndLoss[index];
    } else {
      return null;
    }
  }

  getContract(id: number) {
    const index = this.contracts.findIndex(contract => contract.contractId === id);
    if (index > -1) {
      return this.contracts[index];
    } else {
      return null;
    }
  }

  getDisplayString(id: number) {
    const contract = this.getContract(id);
    return contract.strike + " " + contract.right;
  }

  ngOnDestroy() {
    this.contractsSub.unsubscribe();
    this.profitAndLossSub.unsubscribe();
  }
}
