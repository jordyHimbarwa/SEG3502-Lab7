import { Component, inject, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Book } from './model/book';
import { BooksService } from './service/books-service';

@Component({
  selector: 'app-books',
  templateUrl: './books.html',
  imports: [RouterLink, RouterLinkActive, RouterOutlet]
})
export class Books implements OnInit {
  books: Book[] = [];
  error = '';
  private booksService: BooksService = inject(BooksService);

  ngOnInit(): void {
    this.booksService.getBooks().subscribe({
      next: books => this.books = books,
      error: _ => this.error = 'Unable to load the books. Is the server running?'
    });
  }
}
