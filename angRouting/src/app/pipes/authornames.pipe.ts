import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'authornames'
})
export class AuthornamesPipe implements PipeTransform {
  transform(authors: {firstName: string, lastName: string}[] | null | undefined): string {
    if (!authors || authors.length === 0) {
      return 'Unknown';
    }
    return authors.map(a => `${a.firstName} ${a.lastName}`).join(', ');
  }
}
