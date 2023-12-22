import {Component, Input} from '@angular/core';
import {Position} from "../../model/position.model";

@Component({
  selector: 'app-position-item',
  templateUrl: './position-item.component.html',
  styleUrl: './position-item.component.css'
})
export class PositionItemComponent {
  @Input() position: Position;

  constructor() {
  }
}
