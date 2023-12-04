import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  @Output() headerSelectEvent: EventEmitter<string> = new EventEmitter<string>();
  collapsed: boolean = true;

  onHeaderClick(category:string){
    this.headerSelectEvent.emit(category)
  }

}
