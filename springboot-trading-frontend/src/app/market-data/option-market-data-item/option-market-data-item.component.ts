import {Component, Input, OnInit} from '@angular/core';
import {OptionMarketData} from "../../model/option-market-data.model";
import {MarketDataOpenCloseService} from "../service/market-data-open-close.service";
import {Contract} from "../../model/contract.model";

@Component({
  selector: 'app-option-market-data-item',
  standalone: true,
  imports: [],
  templateUrl: './option-market-data-item.component.html',
  styleUrl: './option-market-data-item.component.css'
})
export class OptionMarketDataItemComponent implements OnInit{
@Input() optionTicker: OptionMarketData;
contract: Contract;

constructor(private marketDataOpenCloseService: MarketDataOpenCloseService) {
}

ngOnInit() {
  this.contract = this.marketDataOpenCloseService.getContractById(this.optionTicker.tickerId);
}

}
