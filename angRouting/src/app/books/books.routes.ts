import { Routes } from '@angular/router';
import { BookDetail } from './book/book';

export const booksRoutes: Routes = [
  {path: ':id', component: BookDetail}
];
