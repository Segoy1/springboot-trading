import {Component, Input} from '@angular/core';
import {ProfitAndLoss} from "../../../../model/profit-and-loss.model";
import {Position} from "../../../../model/position.model";

@Component({
  selector: 'app-combo-position-item',
  standalone: true,
  imports: [],
  templateUrl: './combo-position-item.component.html',
  styleUrl: './combo-position-item.component.css'
})
export class ComboPositionItemComponent {
  @Input() profitAndLoss: ProfitAndLoss[]
  @Input() position: Position;

}
