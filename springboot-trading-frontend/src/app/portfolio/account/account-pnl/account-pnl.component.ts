import {Component, Input} from '@angular/core';
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";

@Component({
  selector: 'app-account-pnl',
  templateUrl: './account-pnl.component.html',
  styleUrl: './account-pnl.component.css'
})
export class AccountPnlComponent {
  @Input() profitAndLoss: ProfitAndLoss;
}
