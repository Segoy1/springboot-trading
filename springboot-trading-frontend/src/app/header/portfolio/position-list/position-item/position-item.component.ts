import {Component, Input, OnInit} from '@angular/core';
import {Position} from "../../../model/position.model";
import {ProfitAndLoss} from "../../../model/profit-and-loss.model";
import {ProfitLossService} from "../../service/profit-loss.service";
import {Portfolio} from "../../../model/portfolio.model";

@Component({
  selector: 'app-position-item',
  templateUrl: './position-item.component.html',
  styleUrl: './position-item.component.css'
})
export class PositionItemComponent{
  @Input() portfolio: Portfolio;

}
