import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
  name:'notavailable'
})
export class NotAvailablePipe implements PipeTransform{

  transform(value: any): any {
    return value === null ? 'n/a' : value;
  }
}
