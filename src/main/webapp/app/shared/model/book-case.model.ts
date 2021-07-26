import { Moment } from 'moment';
import { IBook } from 'app/shared/model/book.model';

export interface IBookCase {
  id?: number;
  bookCaseName?: string;
  dateCreate?: Moment;
  dateUpdate?: Moment;
  userCreate?: string;
  bookCases?: IBook[];
}

export class BookCase implements IBookCase {
  constructor(
    public id?: number,
    public bookCaseName?: string,
    public dateCreate?: Moment,
    public dateUpdate?: Moment,
    public userCreate?: string,
    public bookCases?: IBook[]
  ) {}
}
