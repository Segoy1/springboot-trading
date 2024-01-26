import {Pipe, PipeTransform} from "@angular/core";

@Pipe({
  standalone: true,
  name:'notavailable'
})
export class NotAvailablePipe implements PipeTransform{

  transform(value: any): any {
    //randum magic number because I cant figure out to compare agains Number.maxValue
    return value === null || value > 900000000000 ? 'n/a' : value;
  }
}
