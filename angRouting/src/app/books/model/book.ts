export class Author {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string
  ) {}
}

export class Book {
  constructor(
    public id: number,
    public category: string,
    public title: string,
    public cost: number,
    public authors: Author[],
    public year: number,
    public description: string
  ) {}
}

/** Author as returned by GET /books-api/authors/{id} */
export interface AuthorDetails {
  id: number;
  firstName: string;
  lastName: string;
  books: {id: number, title: string}[];
}
