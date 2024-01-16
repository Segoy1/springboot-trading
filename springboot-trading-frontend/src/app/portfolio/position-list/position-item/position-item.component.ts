import {Component, Input} from '@angular/core';
import {Portfolio} from "../../../model/portfolio.model";

@Component({
  selector: 'app-position-item',
  templateUrl: './position-item.component.html',
  styleUrl: './position-item.component.css'
})
export class PositionItemComponent{
  @Input() portfolio: Portfolio;

}
