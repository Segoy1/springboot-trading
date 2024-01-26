import {Component, Input, OnInit} from '@angular/core';
import {OptionMarketData} from "../../model/market-data/option-market-data.model";
import {MarketDataOpenCloseService} from "../service/market-data-open-close.service";
import {Contract} from "../../model/contract.model";
import {DecimalPipe, NgForOf} from "@angular/common";
import {RouterLinkActive} from "@angular/router";
import {StandardTicker} from "../../model/market-data/standard-ticker.model";
import {OptionTicker} from "../../model/market-data/option-ticker.model";
import {NotAvailablePipe} from "../../shared/not-available.pipe";

@Component({
  selector: 'app-option-market-data-item',
  standalone: true,
  imports: [
    NgForOf,
    RouterLinkActive,
    DecimalPipe,
    NotAvailablePipe
  ],
  templateUrl: './option-market-data-item.component.html',
  styleUrl: './option-market-data-item.component.css'
})
export class OptionMarketDataItemComponent implements OnInit{
@Input() optionTicker: OptionTicker;
contract: Contract;

constructor(private marketDataOpenCloseService: MarketDataOpenCloseService) {
}

ngOnInit() {
  this.contract = this.marketDataOpenCloseService.getContractById(this.optionTicker.tickerId);
}
  onCancelMarketData(){
    this.marketDataOpenCloseService.stopMarketData(this.optionTicker.tickerId);
  }
}
