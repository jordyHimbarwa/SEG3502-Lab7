import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Book } from '../model/book';
import { BooksService } from '../service/books-service';
import { AuthornamesPipe } from '../../pipes/authornames.pipe';

@Component({
  selector: 'app-book',
  templateUrl: './book.html',
  imports: [AuthornamesPipe]
})
export class BookDetail implements OnInit, OnDestroy {
  selectedBook!: Book | null;
  private subscription?: Subscription;
  private route: ActivatedRoute = inject(ActivatedRoute);
  private booksService: BooksService = inject(BooksService);

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
      this.subscription?.unsubscribe();
      this.subscription = this.booksService.getBook(id).subscribe({
        next: (data: Book) => {
          this.selectedBook = data;
        },
        error: (_: any) => {
          this.selectedBook = null;
        }
      });
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}
