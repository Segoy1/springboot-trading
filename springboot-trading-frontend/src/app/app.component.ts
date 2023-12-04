import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  isOrders: boolean = false;
  isMarketData: boolean = false ;
  isPortfolio: boolean = false;

  activeCategory(category: string){
    this.isMarketData = category === "marketData";
    this.isOrders = category === "orders";
    this.isPortfolio = category === "portfolio";
  }
}
