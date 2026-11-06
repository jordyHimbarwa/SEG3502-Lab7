import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthorDetails } from '../books/model/book';
import { BooksService } from '../books/service/books-service';

@Component({
  selector: 'app-authors',
  templateUrl: './authors.html',
  imports: [FormsModule]
})
export class Authors {
  authorId: number | null = null;
  author: AuthorDetails | null = null;
  message = '';
  private booksService: BooksService = inject(BooksService);

  onSubmit(): void {
    const id = this.authorId;
    this.author = null;
    this.message = '';
    if (id === null || !Number.isInteger(id) || id < 1) {
      this.message = 'Please enter a valid author id (a positive whole number).';
      return;
    }
    this.booksService.getAuthor(id).subscribe({
      next: author => this.author = author,
      error: (err: HttpErrorResponse) => {
        this.message = err.status === 404
          ? `No author found with id ${id}.`
          : 'Unable to retrieve the author. Please check that the server is running.';
      }
    });
  }
}
