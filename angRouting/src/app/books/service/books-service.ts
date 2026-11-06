import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Author, AuthorDetails, Book } from '../model/book';

const Url = 'http://localhost:8080/books-api/';

@Injectable({
  providedIn: 'root'
})
export class BooksService {
  private http: HttpClient = inject(HttpClient);

  public getBooks(): Observable<Book[]> {
    return this.http.get<any>(Url + 'books').pipe(
      map(response => response._embedded ? response._embedded.books : [])
    );
  }

  public getBook(id: string): Observable<Book> {
    return this.http.get<Book>(Url + 'books/' + id);
  }

  public addBook(book: Book): Observable<Book> {
    return this.http.post<Book>(Url + 'books', book);
  }

  /**
   * The server's Author entity owns the author-book link, so a book's author
   * is added by creating the author with a reference to the book.
   */
  public addBookAuthor(bookId: number, author: Author): Observable<Author> {
    const body = {firstName: author.firstName, lastName: author.lastName, books: [{id: bookId}]};
    return this.http.post<Author>(Url + 'authors', body);
  }

  public getAuthor(id: number): Observable<AuthorDetails> {
    return this.http.get<AuthorDetails>(Url + 'authors/' + id);
  }
}
