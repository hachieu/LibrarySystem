import { Moment } from 'moment';
import { IBook } from 'app/shared/model/book.model';
import { IBookCategory } from 'app/shared/model/book-category.model';

export interface IBookTitle {
  id?: number;
  bookTitleName?: string;
  author?: string;
  publicationDate?: Moment;
  page?: number;
  priceOfBook?: number;
  prireOfBorrow?: number;
  image?: string;
  dateCreate?: Moment;
  dateUpdate?: Moment;
  userCreate?: string;
  bookTitles?: IBook[];
  bookCategory?: IBookCategory;
}

export class BookTitle implements IBookTitle {
  constructor(
    public id?: number,
    public bookTitleName?: string,
    public author?: string,
    public publicationDate?: Moment,
    public page?: number,
    public priceOfBook?: number,
    public prireOfBorrow?: number,
    public image?: string,
    public dateCreate?: Moment,
    public dateUpdate?: Moment,
    public userCreate?: string,
    public bookTitles?: IBook[],
    public bookCategory?: IBookCategory
  ) {}
}
