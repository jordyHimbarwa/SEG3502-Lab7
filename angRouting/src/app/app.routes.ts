import { Routes } from '@angular/router';
import { Home } from './home/home';
import { About } from './about/about';
import { Contact } from './contact/contact';
import { Login } from './login/login';
import { Admin } from './admin/admin';
import { Authors } from './authors/authors';
import { Books } from './books/books';
import { booksRoutes } from './books/books.routes';
import { loggedInGuard } from './logged-in.guard';

export const routes: Routes = [
  {path: 'home', component: Home},
  {path: 'about', component: About},
  {path: 'contact', component: Contact},
  {path: 'login', component: Login},
  {
    path: 'admin',
    component: Admin,
    canActivate: [loggedInGuard]
  },
  {path: 'books', component: Books,
    children: booksRoutes
  },
  {path: 'authors', component: Authors},
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: '**', component: Home}
];
