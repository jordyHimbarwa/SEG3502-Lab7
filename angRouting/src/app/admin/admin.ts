import { Component, inject } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, ReactiveFormsModule, Validators } from '@angular/forms';
import { Author, Book } from '../books/model/book';
import { BooksService } from '../books/service/books-service';

function categoryValidator(control: AbstractControl): { [s: string]: boolean } | null {
  const validCategories = ['Kids', 'Tech', 'Cook'];
  if (!validCategories.includes(control.value)) {
    return {invalidCategory: true};
  }
  return null;
}

@Component({
  selector: 'app-admin',
  templateUrl: './admin.html',
  imports: [ReactiveFormsModule]
})
export class Admin {
  private builder: FormBuilder = inject(FormBuilder);
  private booksService: BooksService = inject(BooksService);
  message = '';
  messageType: 'info' | 'error' = 'info';

  bookForm = this.builder.group({
    category: ['', [Validators.required, categoryValidator]],
    title: ['', Validators.required],
    cost: ['', [Validators.required, Validators.pattern('\\d+(\\.\\d{1,2})?')]],
    authors: this.builder.array([]),
    year: [''],
    description: ['']
  });

  get category(): AbstractControl<string> { return <AbstractControl>this.bookForm.get('category'); }
  get title(): AbstractControl<string> { return <AbstractControl>this.bookForm.get('title'); }
  get cost(): AbstractControl<string> { return <AbstractControl>this.bookForm.get('cost'); }
  get authors(): FormArray {
    return this.bookForm.get('authors') as FormArray;
  }

  onSubmit(): void {
    // id 0: the server generates the id
    const book = new Book(0,
      <string>this.bookForm.value.category,
      <string>this.bookForm.value.title,
      Number(this.bookForm.value.cost),
      [],
      Number(this.bookForm.value.year),
      <string>this.bookForm.value.description);
    const authors = <Author[]>this.bookForm.value.authors;
    this.booksService.addBook(book).subscribe({
      next: response => {
        authors
          .filter(a => a.firstName || a.lastName)
          .forEach(author => this.booksService.addBookAuthor(response.id, author).subscribe());
        this.showMessage('info', `The book was successfully added with id ${response.id}`);
      },
      error: _ => this.showMessage('error', 'Unable to add the book')
    });
    this.bookForm.reset();
    this.authors.clear();
  }

  addAuthor(): void {
    this.authors.push(
      this.builder.group({
        firstName: [''],
        lastName: ['']
      })
    );
  }

  removeAuthor(i: number): void {
    this.authors.removeAt(i);
  }

  private showMessage(type: 'info' | 'error', msg: string): void {
    this.messageType = type;
    this.message = msg;
    setTimeout(() => this.message = '', 3500);
  }
}
