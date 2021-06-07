import { Moment } from 'moment';
import { IBookTitle } from 'app/shared/model/book-title.model';
import { IBookCase } from 'app/shared/model/book-case.model';
import { IBorrowBook } from 'app/shared/model/borrow-book.model';

export interface IBook {
  id?: number;
  bookBarCode?: string;
  status?: number;
  dateCreate?: Moment;
  dateUpdate?: Moment;
  userCreate?: string;
  bookTitle?: IBookTitle;
  bookCase?: IBookCase;
  borrowBook?: IBorrowBook;
}

export class Book implements IBook {
  constructor(
    public id?: number,
    public bookBarCode?: string,
    public status?: number,
    public dateCreate?: Moment,
    public dateUpdate?: Moment,
    public userCreate?: string,
    public bookTitle?: IBookTitle,
    public bookCase?: IBookCase,
    public borrowBook?: IBorrowBook
  ) {}
}
