import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
  standalone: true,
  name:'fieldname'
})
export class MarketDataFieldNamePipe implements PipeTransform{

  transform(value: any): any {
    switch (value){
      case 'askOptComp':
        return 'Ask';
      case 'bidOptComp':
        return 'Bid';
      case 'lastOptComp':
        return 'Last';
      case 'modelOptComp':
        return 'Model';
      case 'bidPrice':
        return 'Bid Price';
      case 'askPrice':
        return 'Ask Price';
      default:
        return value;
    }
  }
}
