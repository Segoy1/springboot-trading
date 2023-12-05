import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  @Output() headerSelectEvent: EventEmitter<string> = new EventEmitter<string>();
  collapsed: boolean = false;
  isConnected:boolean = false;

  onHeaderClick(category:string){
    this.headerSelectEvent.emit(category)
  }

  onConnect(connect:boolean){
    if(connect){
    // this.http.get('http://localhost:8080/connect').subscribe(() => {
    //   this.isConnected=true;
    // })
    // }if(!connect){
    // }if(!connect){
    // this.http.get('http://localhost:8080/connect').subscribe(() => {
    //   this.isConnected=true;
    // })
    }
  }

}
