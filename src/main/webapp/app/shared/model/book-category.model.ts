import { Moment } from 'moment';
import { IBookTitle } from 'app/shared/model/book-title.model';

export interface IBookCategory {
  id?: number;
  bookCategoryName?: string;
  dateCreate?: Moment;
  dateUpdate?: Moment;
  userCreate?: string;
  bookCategories?: IBookTitle[];
}

export class BookCategory implements IBookCategory {
  constructor(
    public id?: number,
    public bookCategoryName?: string,
    public dateCreate?: Moment,
    public dateUpdate?: Moment,
    public userCreate?: string,
    public bookCategories?: IBookTitle[]
  ) {}
}
