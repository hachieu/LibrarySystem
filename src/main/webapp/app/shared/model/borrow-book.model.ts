import { Moment } from 'moment';
import { IBook } from 'app/shared/model/book.model';
import { ICard } from 'app/shared/model/card.model';

export interface IBorrowBook {
  id?: number;
  dateCreate?: Moment;
  dateUpdate?: Moment;
  userCreate?: string;
  status?: number;
  borrowBooks?: IBook[];
  card?: ICard;
}

export class BorrowBook implements IBorrowBook {
  constructor(
    public id?: number,
    public dateCreate?: Moment,
    public dateUpdate?: Moment,
    public userCreate?: string,
    public status?: number,
    public borrowBooks?: IBook[],
    public card?: ICard
  ) {}
}
